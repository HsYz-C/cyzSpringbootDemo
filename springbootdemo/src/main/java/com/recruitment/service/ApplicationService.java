package com.recruitment.service;

import com.recruitment.common.Result;
import java.util.List;
import java.util.Map;

/**
 * 投递服务接口
 * @author Web架构技术教学组
 */
public interface ApplicationService {
    Result<String> apply(Long userId, Long jobId);
    Result<String> cancel(Long userId, Long jobId);
    Result<List<Map<String, Object>>> listByUser(Long userId);
}