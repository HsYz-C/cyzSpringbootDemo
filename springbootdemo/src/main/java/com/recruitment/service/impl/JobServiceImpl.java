package com.recruitment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.recruitment.common.Result;
import com.recruitment.mapper.JobMapper;
import com.recruitment.model.Job;
import com.recruitment.service.JobService;
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

    @Override
    public Result<List<Job>> list(String city, String keyword) {
        // 构建查询条件
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
        return Result.success(result);
    }

    @Override
    public Result<Job> detail(Long id) {
        Job job = jobMapper.selectById(id);
        if (job == null) {
            return Result.error("职位不存在");
        }

        // 浏览次数 +1
        job.setViewCount(job.getViewCount() + 1);
        jobMapper.updateById(job);

        return Result.success(job);
    }
}
