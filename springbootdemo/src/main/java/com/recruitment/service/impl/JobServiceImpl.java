package com.recruitment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.recruitment.common.Result;
import com.recruitment.mapper.JobMapper;
import com.recruitment.model.Job;
import com.recruitment.service.JobService;
import com.recruitment.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 职位服务实现
 *
 * @author Web架构技术教学组
 */
@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private RedisUtil redisUtil;

    // 缓存key前缀
    private static final String CACHE_JOB_LIST = "job:list:";
    private static final String CACHE_JOB_DETAIL = "job:detail:";
    // 缓存过期时间（秒）
    private static final long CACHE_EXPIRE = 300; // 5分钟

    @Override
    @SuppressWarnings("unchecked")
    public Result<List<Job>> list(String city, String keyword) {
        // 构建缓存key
        String cacheKey = CACHE_JOB_LIST + (city == null ? "" : city) + ":" + (keyword == null ? "" : keyword);

        // 先从缓存获取
        Object cached = redisUtil.get(cacheKey);
        if (cached instanceof List) {
            return Result.success((List<Job>) cached);
        }

        // 缓存没有，查询数据库
        LambdaQueryWrapper<Job> wrapper = new LambdaQueryWrapper<>();

        // 按城市筛选
        if (city != null && !city.trim().isEmpty()) {
            wrapper.eq(Job::getCity, city);
        }

        // 按关键词筛选（公司名或职位名）
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.and(w -> w
                    .like(Job::getCompanyName, keyword)
                    .or()
                    .like(Job::getJobTitle, keyword)
            );
        }

        List<Job> result = jobMapper.selectList(wrapper);

        // 存入缓存
        redisUtil.set(cacheKey, result, CACHE_EXPIRE);

        return Result.success(result);
    }

    @Override
    public Result<Job> detail(Long id) {
        // 先从缓存获取
        String cacheKey = CACHE_JOB_DETAIL + id;
        Object cached = redisUtil.get(cacheKey);
        if (cached instanceof Job) {
            return Result.success((Job) cached);
        }

        // 缓存没有，查询数据库
        Job job = jobMapper.selectById(id);
        if (job == null) {
            return Result.error("职位不存在");
        }

        // 浏览次数 +1
        job.setViewCount(job.getViewCount() + 1);
        jobMapper.updateById(job);

        // 存入缓存
        redisUtil.set(cacheKey, job, CACHE_EXPIRE);

        return Result.success(job);
    }

    /**
     * 新增工作岗位（新增后清空职位列表缓存，避免新职位查不到）
     */
    @Override
    public Result<Job> add(Job job) {
        jobMapper.insert(job);
        redisUtil.deleteByPattern(CACHE_JOB_LIST + "*");
        return Result.success(job);
    }

    /**
     * 删除工作岗位（删除后清空列表缓存和该职位的详情缓存）
     */
    @Override
    public Result<Void> delete(Long id) {
        int rows = jobMapper.deleteById(id);
        if (rows == 0) {
            return Result.error(404, "岗位不存在");
        }
        redisUtil.delete(CACHE_JOB_DETAIL + id);
        redisUtil.deleteByPattern(CACHE_JOB_LIST + "*");
        return Result.success(null);
    }
}
