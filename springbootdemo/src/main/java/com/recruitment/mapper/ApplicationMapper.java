package com.recruitment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recruitment.model.Application;
import org.apache.ibatis.annotations.Mapper;

/**
 * 投递记录Mapper
 *
 * @author Web架构技术教学组
 */
@Mapper
public interface ApplicationMapper extends BaseMapper<Application> {
}
