package com.recruitment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.recruitment.common.Result;
import com.recruitment.mapper.FavoriteMapper;
import com.recruitment.mapper.JobMapper;
import com.recruitment.mapper.UserMapper;
import com.recruitment.model.Favorite;
import com.recruitment.model.Job;
import com.recruitment.model.User;
import com.recruitment.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 收藏服务实现
 *
 * @author Web架构技术教学组
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JobMapper jobMapper;

    @Override
    public Result<String> add(Long userId, Long jobId) {
        if (userId == null || jobId == null) {
            return Result.error("用户ID和职位ID不能为空");
        }

        User user = userMapper.selectById(userId);
        Job job = jobMapper.selectById(jobId);
        if (user == null || job == null) {
            return Result.error("用户或职位不存在");
        }

        // 检查是否已收藏
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId);
        wrapper.eq(Favorite::getJobId, jobId);
        if (favoriteMapper.selectOne(wrapper) != null) {
            return Result.error("已收藏");
        }

        // 添加收藏
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setJobId(jobId);
        favorite.setCreateTime(LocalDateTime.now());
        favoriteMapper.insert(favorite);

        return Result.success("收藏成功", null);
    }

    @Override
    public Result<String> remove(Long userId, Long jobId) {
        if (userId == null || jobId == null) {
            return Result.error("用户ID和职位ID不能为空");
        }

        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId);
        wrapper.eq(Favorite::getJobId, jobId);
        Favorite target = favoriteMapper.selectOne(wrapper);

        if (target == null) {
            return Result.error("收藏记录不存在");
        }

        favoriteMapper.deleteById(target.getId());
        return Result.success("取消收藏成功", null);
    }

    @Override
    public Result<List<Job>> listByUser(Long userId) {
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }

        if (userMapper.selectById(userId) == null) {
            return Result.error("用户不存在");
        }

        // 查询收藏记录
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId);
        List<Favorite> favorites = favoriteMapper.selectList(wrapper);

        // 获取职位详情
        List<Job> jobs = new ArrayList<>();
        for (Favorite favorite : favorites) {
            Job job = jobMapper.selectById(favorite.getJobId());
            if (job != null) {
                jobs.add(job);
            }
        }
        return Result.success(jobs);
    }
}
