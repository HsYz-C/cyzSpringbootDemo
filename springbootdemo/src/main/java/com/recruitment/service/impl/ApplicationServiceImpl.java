package com.recruitment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.recruitment.common.Result;
import com.recruitment.mapper.ApplicationMapper;
import com.recruitment.mapper.JobMapper;
import com.recruitment.mapper.UserMapper;
import com.recruitment.model.Application;
import com.recruitment.model.Job;
import com.recruitment.model.User;
import com.recruitment.service.ApplicationService;
import com.recruitment.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 投递服务实现
 *
 * @author Web架构技术教学组
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private RedisUtil redisUtil;

    // 缓存key前缀（key 后缀为 userId:status）
    private static final String CACHE_APPLICATION_LIST = "application:list:";
    // 缓存过期时间（秒）
    private static final long CACHE_EXPIRE = 300;

    @Override
    public Result<String> apply(Long userId, Long jobId) {
        if (userId == null || jobId == null) {
            return Result.error("用户ID和职位ID不能为空");
        }

        User user = userMapper.selectById(userId);
        Job job = jobMapper.selectById(jobId);
        if (user == null || job == null) {
            return Result.error("用户或职位不存在");
        }

        // 检查是否已投递
        LambdaQueryWrapper<Application> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Application::getUserId, userId);
        wrapper.eq(Application::getJobId, jobId);
        Application existing = applicationMapper.selectOne(wrapper);

        if (existing != null) {
            if (existing.getStatus() == Application.STATUS_APPLIED) {
                return Result.error("已投递");
            }
            // 重新投递
            existing.setStatus(Application.STATUS_APPLIED);
            existing.setApplyTime(LocalDateTime.now());
            existing.setCancelTime(null);
            applicationMapper.updateById(existing);

            job.setApplyCount(job.getApplyCount() + 1);
            jobMapper.updateById(job);

            // 投递状态变化，清空该用户的投递列表缓存
            clearApplicationListCache(userId);
            return Result.success("重新投递成功", null);
        }

        // 新增投递
        Application application = new Application();
        application.setUserId(userId);
        application.setJobId(jobId);
        application.setStatus(Application.STATUS_APPLIED);
        application.setApplyTime(LocalDateTime.now());
        applicationMapper.insert(application);

        // 投递次数 +1
        job.setApplyCount(job.getApplyCount() + 1);
        jobMapper.updateById(job);

        // 投递状态变化，清空该用户的投递列表缓存
        clearApplicationListCache(userId);

        return Result.success("投递成功", null);
    }

    @Override
    public Result<String> cancel(Long userId, Long jobId) {
        if (userId == null || jobId == null) {
            return Result.error("用户ID和职位ID不能为空");
        }

        LambdaQueryWrapper<Application> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Application::getUserId, userId);
        wrapper.eq(Application::getJobId, jobId);
        wrapper.eq(Application::getStatus, Application.STATUS_APPLIED);
        Application target = applicationMapper.selectOne(wrapper);

        if (target == null) {
            return Result.error("未找到可取消的投递记录");
        }

        target.setStatus(Application.STATUS_CANCELED);
        target.setCancelTime(LocalDateTime.now());
        applicationMapper.updateById(target);

        // 投递次数 -1
        Job job = jobMapper.selectById(jobId);
        if (job != null && job.getApplyCount() > 0) {
            job.setApplyCount(job.getApplyCount() - 1);
            jobMapper.updateById(job);
        }

        // 投递状态变化，清空该用户的投递列表缓存
        clearApplicationListCache(userId);

        return Result.success("取消投递成功", null);
    }

    /**
     * 查询指定用户的全部投递记录（不区分状态）
     */
    @Override
    public Result<List<Map<String, Object>>> listByUser(Long userId) {
        return listDetail(userId, null);
    }

    /**
     * 查询投递记录，可按用户和状态过滤（参数为 null 时该条件不参与筛选）
     */
    @Override
    @SuppressWarnings("unchecked")
    public Result<List<Map<String, Object>>> listDetail(Long userId, Integer status) {
        // 构建缓存key，带上 status 避免不同状态查询互相串数据
        String cacheKey = CACHE_APPLICATION_LIST + userId + ":" + status;

        // 先从缓存获取
        Object cached = redisUtil.get(cacheKey);
        if (cached instanceof List) {
            return Result.success((List<Map<String, Object>>) cached);
        }

        // 投递记录 + 用户 + 职位信息的联表查询，SQL 见 mapper/ApplicationMapper.xml
        // userId 为空查询全部用户的投递记录，status 为空不按状态过滤
        List<Map<String, Object>> list = applicationMapper.listDetail(userId, status);

        // 存入缓存
        redisUtil.set(cacheKey, list, CACHE_EXPIRE);

        return Result.success(list);
    }

    /**
     * 清空某个用户的全部投递列表缓存（不同 status 都清）
     */
    private void clearApplicationListCache(Long userId) {
        redisUtil.deleteByPattern(CACHE_APPLICATION_LIST + userId + ":*");
    }
}
