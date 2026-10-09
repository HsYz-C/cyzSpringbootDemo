package com.recruitment.service.impl;

import com.recruitment.common.Result;
import com.recruitment.data.MockDataStore;
import com.recruitment.model.Application;
import com.recruitment.model.Job;
import com.recruitment.model.User;
import com.recruitment.service.ApplicationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投递服务实现（内存版）
 * @author Web架构技术教学组
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Override
    public Result<String> apply(Long userId, Long jobId) {
        if (userId == null || jobId == null) {
            return Result.error("用户ID和职位ID不能为空");
        }

        User user = MockDataStore.getUserById(userId);
        Job job = MockDataStore.getJobById(jobId);
        if (user == null || job == null) {
            return Result.error("用户或职位不存在");
        }

        // 检查是否已投递或取消后重新投递
        for (Application application : MockDataStore.getApplications()) {
            if (application.getUserId().equals(userId)
                    && application.getJobId().equals(jobId)) {
                if (application.getStatus() == Application.STATUS_APPLIED) {
                    return Result.error("已投递");
                }
                // 取消后重新投递
                application.setStatus(Application.STATUS_APPLIED);
                application.setApplyTime(LocalDateTime.now());
                application.setCancelTime(null);
                job.setApplyCount(job.getApplyCount() + 1);
                return Result.success("重新投递成功", null);
            }
        }

        // 新建投递记录
        Application application = new Application();
        application.setId(MockDataStore.nextApplicationId());
        application.setUserId(userId);
        application.setJobId(jobId);
        application.setStatus(Application.STATUS_APPLIED);
        application.setApplyTime(LocalDateTime.now());
        MockDataStore.getApplications().add(application);
        job.setApplyCount(job.getApplyCount() + 1);

        return Result.success("投递成功", null);
    }

    @Override
    public Result<String> cancel(Long userId, Long jobId) {
        if (userId == null || jobId == null) {
            return Result.error("用户ID和职位ID不能为空");
        }

        Application target = null;
        for (Application application : MockDataStore.getApplications()) {
            if (application.getUserId().equals(userId)
                    && application.getJobId().equals(jobId)
                    && application.getStatus() == Application.STATUS_APPLIED) {
                target = application;
                break;
            }
        }

        if (target == null) {
            return Result.error("未找到可取消的投递记录");
        }

        target.setStatus(Application.STATUS_CANCELED);
        target.setCancelTime(LocalDateTime.now());

        Job job = MockDataStore.getJobById(jobId);
        if (job != null && job.getApplyCount() > 0) {
            job.setApplyCount(job.getApplyCount() - 1);
        }

        return Result.success("取消投递成功", null);
    }

    @Override
    public Result<List<Map<String, Object>>> listByUser(Long userId) {
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }

        if (MockDataStore.getUserById(userId) == null) {
            return Result.error("用户不存在");
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Application application : MockDataStore.getApplications()) {
            if (application.getUserId().equals(userId)) {
                Job job = MockDataStore.getJobById(application.getJobId());
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
        }

        return Result.success(result);
    }
}
