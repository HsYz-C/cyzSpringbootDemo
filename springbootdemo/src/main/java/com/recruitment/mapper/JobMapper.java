package com.recruitment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recruitment.model.Job;
import org.apache.ibatis.annotations.Mapper;

/**
 * 职位Mapper
 *
 * @author Web架构技术教学组
 */
@Mapper
public interface JobMapper extends BaseMapper<Job> {
}
