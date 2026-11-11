package com.recruitment.controller;

import com.recruitment.annotation.RequireRole;
import com.recruitment.common.Result;
import com.recruitment.model.Job;
import com.recruitment.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 职位接口（统一使用 /job 前缀）
 *
 * <p>列表和详情在拦截器白名单内，无需登录；
 * 新增、删除不在白名单内，必须携带 Token。</p>
 *
 * @author Web架构技术教学组
 */
@Tag(name = "职位管理", description = "职位查询、发布、删除相关接口")
@RestController
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    /**
     * 职位列表（支持按城市、关键词筛选）
     * GET /api/job/list?city=杭州&keyword=Java
     */
    @Operation(summary = "职位列表", description = "查询职位列表，支持按城市和关键词筛选")
    @GetMapping("/job/list")
    public Result<List<Job>> list(
            @Parameter(description = "工作城市", example = "杭州")
            @RequestParam(required = false) String city,
            @Parameter(description = "搜索关键词（公司名或职位名）", example = "Java")
            @RequestParam(required = false) String keyword) {
        return jobService.list(city, keyword);
    }

    /**
     * 职位详情
     * GET /api/job/1
     */
    @Operation(summary = "职位详情", description = "根据职位ID查询详情，同时浏览次数加1")
    @GetMapping("/job/{id}")
    public Result<Job> detail(
            @Parameter(description = "职位ID", example = "1")
            @PathVariable Long id) {
        return jobService.detail(id);
    }

    /**
     * 新增工作岗位（需要登录）
     * POST /api/job/add
     */
    @RequireRole("ADMIN")
    @Operation(summary = "新增工作岗位", description = "发布一个新的职位，需要携带 Token 且角色为 ADMIN")
    @PostMapping("/job/add")
    public Result<Job> add(@RequestBody Job job) {
        return jobService.add(job);
    }

    /**
     * 删除工作岗位（需要登录）
     * DELETE /api/job/delete/3
     */
    @RequireRole("ADMIN")
    @Operation(summary = "删除工作岗位", description = "根据职位ID删除岗位，需要携带 Token 且角色为 ADMIN；岗位不存在返回 404")
    @DeleteMapping("/job/delete/{id}")
    public Result<Void> delete(
            @Parameter(description = "要删除的职位ID", example = "3")
            @PathVariable Long id) {
        return jobService.delete(id);
    }
}
