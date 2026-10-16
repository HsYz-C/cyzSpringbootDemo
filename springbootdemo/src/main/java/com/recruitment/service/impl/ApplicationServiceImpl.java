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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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

        return Result.success("取消投递成功", null);
    }

    @Override
    public Result<List<Map<String, Object>>> listByUser(Long userId) {
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }

        if (userMapper.selectById(userId) == null) {
            return Result.error("用户不存在");
        }

        // 查询投递记录
        LambdaQueryWrapper<Application> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Application::getUserId, userId);
        List<Application> applications = applicationMapper.selectList(wrapper);

        List<Map<String, Object>> result = new ArrayList<>();
        for (Application application : applications) {
            Job job = jobMapper.selectById(application.getJobId());
            if (job == null) {
                continue;
            }
            Map<String, Object> info = new HashMap<>();
            info.put("jobId", job.getId());
            info.put("jobTitle", job.getJobTitle());
            info.put("companyName", job.getCompanyName());
            info.put("status", application.getStatus());
            info.put("applyTime", application.getApplyTime());
            info.put("cancelTime", application.getCancelTime());
            result.add(info);
        }

        return Result.success(result);
    }
}
