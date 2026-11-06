package com.recruitment.controller;

import com.recruitment.common.Result;
import com.recruitment.model.Job;
import com.recruitment.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "职位管理", description = "职位查询相关接口")
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
    @Operation(summary = "职位列表", description = "查询职位列表，支持按城市和关键词筛选")
    @GetMapping("/list")
    public Result<List<Job>> list(
            @Parameter(description = "工作城市", example = "杭州")
            @RequestParam(required = false) String city,
            @Parameter(description = "搜索关键词（公司名或职位名）", example = "Java")
            @RequestParam(required = false) String keyword) {
        return jobService.list(city, keyword);
    }

    /**
     * 职位详情
     * GET /api/jobs/1
     */
    @Operation(summary = "职位详情", description = "根据职位ID查询详情，同时浏览次数加1")
    @GetMapping("/{id}")
    public Result<Job> detail(
            @Parameter(description = "职位ID", example = "1")
            @PathVariable Long id) {
        return jobService.detail(id);
    }
}
