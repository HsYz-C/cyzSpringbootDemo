package com.recruitment.controller;

import com.recruitment.common.Result;
import com.recruitment.model.Job;
import com.recruitment.service.FavoriteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 收藏接口
 *
 * @author Web架构技术教学组
 */
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
    @DeleteMapping("/remove")
    public Result<String> remove(@RequestParam Long userId, @RequestParam Long jobId) {
        return favoriteService.remove(userId, jobId);
    }

    /**
     * 获取收藏列表
     * GET /api/favorites/list?userId=1
     */
    @GetMapping("/list")
    public Result<List<Job>> list(@RequestParam Long userId) {
        return favoriteService.listByUser(userId);
    }
}
