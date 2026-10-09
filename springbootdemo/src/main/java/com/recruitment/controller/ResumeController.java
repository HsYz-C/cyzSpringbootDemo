package com.recruitment.controller;

import com.recruitment.common.Result;
import com.recruitment.model.Resume;
import com.recruitment.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 简历控制器
 * 提供简历的增删改查接口
 *
 * @author Web架构技术教学组
 */
@RestController
@RequestMapping("/resumes")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    /**
     * 创建简历
     * POST /api/resumes
     */
    @PostMapping
    public Result<Resume> create(@RequestBody Resume resume) {
        return resumeService.create(resume);
    }

    /**
     * 获取简历详情
     * GET /api/resumes/{id}
     */
    @GetMapping("/{id}")
    public Result<Resume> getById(@PathVariable Long id) {
        return resumeService.getById(id);
    }

    /**
     * 更新简历
     * PUT /api/resumes/{id}
     */
    @PutMapping("/{id}")
    public Result<Resume> update(@PathVariable Long id, @RequestBody Resume resume) {
        return resumeService.update(id, resume);
    }

    /**
     * 删除简历
     * DELETE /api/resumes/{id}
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        return resumeService.delete(id);
    }

    /**
     * 获取用户的简历列表
     * GET /api/resumes/list?userId=1
     */
    @GetMapping("/list")
    public Result<List<Resume>> listByUserId(@RequestParam Long userId) {
        return resumeService.listByUserId(userId);
    }
}
