package com.recruitment.model;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 收藏记录
 * @author Web架构技术教学组
 */
@Data
public class Favorite {
    private Long id;
    private Long userId;
    private Long jobId;
    private LocalDateTime createTime;
}
