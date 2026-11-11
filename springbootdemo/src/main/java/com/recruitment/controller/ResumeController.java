package com.recruitment.controller;

import com.recruitment.annotation.RequireRole;
import com.recruitment.common.Result;
import com.recruitment.model.Resume;
import com.recruitment.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 简历控制器
 * 提供简历的增删改查接口
 *
 * @author Web架构技术教学组
 */
@Tag(name = "简历管理", description = "简历增删改查接口")
@RestController
@RequestMapping("/resumes")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    /**
     * 创建简历
     * POST /api/resumes
     */
    @Operation(summary = "创建简历", description = "为指定用户创建简历")
    @PostMapping
    public Result<Resume> create(@RequestBody Resume resume) {
        return resumeService.create(resume);
    }

    /**
     * 获取简历详情
     * GET /api/resumes/{id}
     */
    @Operation(summary = "获取简历详情", description = "根据简历ID查询简历")
    @GetMapping("/{id}")
    public Result<Resume> getById(
            @Parameter(description = "简历ID", example = "1")
            @PathVariable Long id) {
        return resumeService.getById(id);
    }

    /**
     * 更新简历
     * PUT /api/resumes/{id}
     */
    @Operation(summary = "更新简历", description = "根据简历ID更新简历信息")
    @PutMapping("/{id}")
    public Result<Resume> update(
            @Parameter(description = "简历ID", example = "1")
            @PathVariable Long id,
            @RequestBody Resume resume) {
        return resumeService.update(id, resume);
    }

    /**
     * 删除简历
     * DELETE /api/resumes/{id}
     */
    @Operation(summary = "删除简历", description = "根据简历ID删除简历")
    @DeleteMapping("/{id}")
    public Result<String> delete(
            @Parameter(description = "简历ID", example = "1")
            @PathVariable Long id) {
        return resumeService.delete(id);
    }

    /**
     * 获取用户的简历列表（课堂测试任务10：查询无简历用户）
     * GET /api/resumes/list?userId=1
     *
     * 需要登录，且角色必须为 Recruiter（ADMIN 自动放行）
     */
    @RequireRole("Recruiter")
    @Operation(summary = "获取用户简历列表", description = "查询指定用户的全部简历；需要 JWT 且角色为 Recruiter")
    @GetMapping("/list")
    public Result<List<Resume>> listByUserId(
            @Parameter(description = "用户ID", example = "1")
            @RequestParam Long userId) {
        return resumeService.listByUserId(userId);
    }
}
