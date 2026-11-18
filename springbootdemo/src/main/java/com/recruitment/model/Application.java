package com.recruitment.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 投递记录
 *
 * @author Web架构技术教学组
 */
@Data
@TableName("application")
@Schema(description = "投递记录")
public class Application {
    public static final int STATUS_APPLIED = 0;   // 已投递
    public static final int STATUS_CANCELED = 1;  // 已取消

    @Schema(description = "投递ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "职位ID")
    @NotNull(message = "职位ID不能为空")
    private Long jobId;

    @Schema(description = "状态：0-已投递，1-已取消")
    @Min(value = 0, message = "状态只能是0或1")
    @Max(value = 1, message = "状态只能是0或1")
    private int status;

    @Schema(description = "投递时间")
    private LocalDateTime applyTime;

    @Schema(description = "取消时间")
    private LocalDateTime cancelTime;
}
