package com.recruitment.service;

import com.recruitment.common.Result;
import com.recruitment.model.Job;
import java.util.List;

/**
 * 收藏服务接口
 * @author Web架构技术教学组
 */
public interface FavoriteService {
    Result<String> add(Long userId, Long jobId);
    Result<String> remove(Long userId, Long jobId);
    Result<List<Job>> listByUser(Long userId);
}
