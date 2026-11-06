package com.recruitment.controller;

import com.recruitment.common.Result;
import com.recruitment.model.Job;
import com.recruitment.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 收藏接口
 *
 * @author Web架构技术教学组
 */
@Tag(name = "收藏管理", description = "职位收藏相关接口")
@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    /**
     * 添加收藏
     * POST /api/favorites/add
     */
    @Operation(summary = "添加收藏", description = "请求体需包含 userId 和 jobId")
    @PostMapping("/add")
    public Result<String> add(@RequestBody Map<String, Long> request) {
        Long userId = request == null ? null : request.get("userId");
        Long jobId = request == null ? null : request.get("jobId");
        return favoriteService.add(userId, jobId);
    }

    /**
     * 取消收藏
     * DELETE /api/favorites/remove?userId=1&jobId=1
     */
    @Operation(summary = "取消收藏", description = "根据用户ID和职位ID取消收藏")
    @DeleteMapping("/remove")
    public Result<String> remove(
            @Parameter(description = "用户ID", example = "1")
            @RequestParam Long userId,
            @Parameter(description = "职位ID", example = "1")
            @RequestParam Long jobId) {
        return favoriteService.remove(userId, jobId);
    }

    /**
     * 获取收藏列表
     * GET /api/favorites/list?userId=1
     */
    @Operation(summary = "收藏列表", description = "查询指定用户收藏的职位")
    @GetMapping("/list")
    public Result<List<Job>> list(
            @Parameter(description = "用户ID", example = "1")
            @RequestParam Long userId) {
        return favoriteService.listByUser(userId);
    }
}
