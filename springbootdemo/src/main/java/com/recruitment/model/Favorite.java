package com.recruitment.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 收藏记录
 *
 * @author Web架构技术教学组
 */
@Data
@TableName("favorite")
@Schema(description = "收藏记录")
public class Favorite {
    @Schema(description = "收藏ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "职位ID")
    @NotNull(message = "职位ID不能为空")
    private Long jobId;

    @Schema(description = "收藏时间")
    private LocalDateTime createTime;
}
