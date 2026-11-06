package com.recruitment.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
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
    private Long userId;

    @Schema(description = "职位ID")
    private Long jobId;

    @Schema(description = "状态：0-已投递，1-已取消")
    private int status;

    @Schema(description = "投递时间")
    private LocalDateTime applyTime;

    @Schema(description = "取消时间")
    private LocalDateTime cancelTime;
}
