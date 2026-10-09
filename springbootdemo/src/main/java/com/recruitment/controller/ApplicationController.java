package com.recruitment.controller;

import com.recruitment.common.Result;
import com.recruitment.service.ApplicationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 投递接口
 *
 * @author Web架构技术教学组
 */
@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    /**
     * 投递职位
     * POST /api/applications/apply
     */
    @PostMapping("/apply")
    public Result<String> apply(@RequestBody Map<String, Long> request) {
        Long userId = request == null ? null : request.get("userId");
        Long jobId = request == null ? null : request.get("jobId");
        return applicationService.apply(userId, jobId);
    }

    /**
     * 取消投递
     * PUT /api/applications/cancel?userId=1&jobId=1
     */
    @PutMapping("/cancel")
    public Result<String> cancel(@RequestParam Long userId, @RequestParam Long jobId) {
        return applicationService.cancel(userId, jobId);
    }

    /**
     * 获取投递列表
     * GET /api/applications/list?userId=1
     */
    @GetMapping("/list")
    public Result<List<Map<String, Object>>> list(@RequestParam Long userId) {
        return applicationService.listByUser(userId);
    }
}
