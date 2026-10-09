package com.recruitment.controller;

import com.recruitment.common.Result;
import com.recruitment.model.Job;
import com.recruitment.service.JobService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 职位接口
 *
 * @author Web架构技术教学组
 */
@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    /**
     * 职位列表（支持按城市、关键词筛选）
     * GET /api/jobs/list?city=杭州&keyword=Java
     */
    @GetMapping("/list")
    public Result<List<Job>> list(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String keyword) {
        return jobService.list(city, keyword);
    }

    /**
     * 职位详情
     * GET /api/jobs/1
     */
    @GetMapping("/{id}")
    public Result<Job> detail(@PathVariable Long id) {
        return jobService.detail(id);
    }
}
