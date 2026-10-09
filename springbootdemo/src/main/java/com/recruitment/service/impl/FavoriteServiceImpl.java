package com.recruitment.service.impl;

import com.recruitment.common.Result;
import com.recruitment.data.MockDataStore;
import com.recruitment.model.Favorite;
import com.recruitment.model.Job;
import com.recruitment.model.User;
import com.recruitment.service.FavoriteService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 收藏服务实现（内存版）
 * @author Web架构技术教学组
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Override
    public Result<String> add(Long userId, Long jobId) {
        if (userId == null || jobId == null) {
            return Result.error("用户ID和职位ID不能为空");
        }

        User user = MockDataStore.getUserById(userId);
        Job job = MockDataStore.getJobById(jobId);
        if (user == null || job == null) {
            return Result.error("用户或职位不存在");
        }

        // 检查是否已收藏
        for (Favorite favorite : MockDataStore.getFavorites()) {
            if (favorite.getUserId().equals(userId)
                    && favorite.getJobId().equals(jobId)) {
                return Result.error("已收藏");
            }
        }

        Favorite favorite = new Favorite();
        favorite.setId(MockDataStore.nextFavoriteId());
        favorite.setUserId(userId);
        favorite.setJobId(jobId);
        favorite.setCreateTime(LocalDateTime.now());
        MockDataStore.getFavorites().add(favorite);

        return Result.success("收藏成功", null);
    }

    @Override
    public Result<String> remove(Long userId, Long jobId) {
        if (userId == null || jobId == null) {
            return Result.error("用户ID和职位ID不能为空");
        }

        Favorite target = null;
        for (Favorite favorite : MockDataStore.getFavorites()) {
            if (favorite.getUserId().equals(userId) && favorite.getJobId().equals(jobId)) {
                target = favorite;
                break;
            }
        }

        if (target == null) {
            return Result.error("收藏记录不存在");
        }

        MockDataStore.getFavorites().remove(target);
        return Result.success("取消收藏成功", null);
    }

    @Override
    public Result<List<Job>> listByUser(Long userId) {
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }

        if (MockDataStore.getUserById(userId) == null) {
            return Result.error("用户不存在");
        }

        List<Job> jobs = new ArrayList<>();
        for (Favorite favorite : MockDataStore.getFavorites()) {
            if (favorite.getUserId().equals(userId)) {
                Job job = MockDataStore.getJobById(favorite.getJobId());
                if (job != null) {
                    jobs.add(job);
                }
            }
        }
        return Result.success(jobs);
    }
}
