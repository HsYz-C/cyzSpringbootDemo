package com.recruitment.service;

import com.recruitment.common.Result;
import com.recruitment.model.Job;
import java.util.List;

/**
 * 职位服务接口
 * @author Web架构技术教学组
 */
public interface JobService {
    Result<List<Job>> list(String city, String keyword);
    Result<Job> detail(Long id);
}
