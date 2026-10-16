package com.recruitment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recruitment.model.Resume;
import org.apache.ibatis.annotations.Mapper;

/**
 * 简历Mapper
 *
 * @author Web架构技术教学组
 */
@Mapper
public interface ResumeMapper extends BaseMapper<Resume> {
}
