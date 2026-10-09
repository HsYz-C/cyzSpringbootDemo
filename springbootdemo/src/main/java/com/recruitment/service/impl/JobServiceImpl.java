package com.recruitment.service.impl;

import com.recruitment.common.Result;
import com.recruitment.data.MockDataStore;
import com.recruitment.model.Job;
import com.recruitment.service.JobService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 职位服务实现（内存版）
 * @author Web架构技术教学组
 */
@Service
public class JobServiceImpl implements JobService {

    @Override
    public Result<List<Job>> list(String city, String keyword) {
        List<Job> result = new ArrayList<>();
        for (Job job : MockDataStore.getJobs()) {
            if (!matchCity(job, city)) {
                continue;
            }
            if (!matchKeyword(job, keyword)) {
                continue;
            }
            result.add(job);
        }
        return Result.success(result);
    }

    @Override
    public Result<Job> detail(Long id) {
        Job job = MockDataStore.getJobById(id);
        if (job == null) {
            return Result.error("职位不存在");
        }
        job.setViewCount(job.getViewCount() + 1);
        return Result.success(job);
    }

    private boolean matchCity(Job job, String city) {
        if (city == null || city.trim().isEmpty()) {
            return true;
        }
        return city.equals(job.getCity());
    }

    private boolean matchKeyword(Job job, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return true;
        }
        return job.getCompanyName().contains(keyword) || job.getJobTitle().contains(keyword);
    }
}
