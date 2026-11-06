package com.recruitment.controller;

import com.recruitment.common.Result;
import com.recruitment.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 投递接口
 *
 * @author Web架构技术教学组
 */
@Tag(name = "投递管理", description = "职位投递相关接口")
@RestController
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    /**
     * 投递职位
     * POST /api/applications/apply
     */
    @Operation(summary = "投递职位", description = "请求体需包含 userId 和 jobId")
    @PostMapping("/applications/apply")
    public Result<String> apply(@RequestBody Map<String, Long> request) {
        Long userId = request == null ? null : request.get("userId");
        Long jobId = request == null ? null : request.get("jobId");
        return applicationService.apply(userId, jobId);
    }

    /**
     * 取消投递
     * PUT /api/applications/cancel?userId=1&jobId=1
     */
    @Operation(summary = "取消投递", description = "根据用户ID和职位ID取消投递")
    @PutMapping("/applications/cancel")
    public Result<String> cancel(
            @Parameter(description = "用户ID", example = "1")
            @RequestParam Long userId,
            @Parameter(description = "职位ID", example = "1")
            @RequestParam Long jobId) {
        return applicationService.cancel(userId, jobId);
    }

    /**
     * 查询投递记录列表（application + user + job 三表联查）
     * GET /api/application/list                       查询全部
     * GET /api/application/list?userId=1              查询用户1的投递记录
     * GET /api/application/list?status=0              查询所有"已投递"记录
     * GET /api/application/list?userId=1&status=0     查询用户1的"已投递"记录
     */
    @Operation(summary = "查询投递记录", description = "application、user、job 三表联查，userId 和 status 均可选，都不传时查询全部")
    @GetMapping("/application/list")
    public Result<List<Map<String, Object>>> list(
            @Parameter(description = "用户ID，不传则查询所有用户", example = "1")
            @RequestParam(required = false) Long userId,
            @Parameter(description = "投递状态：0-已投递，1-已取消，不传则不过滤", example = "0")
            @RequestParam(required = false) Integer status) {
        return applicationService.listDetail(userId, status);
    }
}
