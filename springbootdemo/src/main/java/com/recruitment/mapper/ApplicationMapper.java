package com.recruitment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recruitment.model.Application;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 继承 BaseMapper 获得单表 CRUD 方法，自定义多表查询方法的 SQL 写在 XML 中
 */
@Mapper
public interface ApplicationMapper extends BaseMapper<Application> {

    List<Map<String, Object>> listDetail(
            @Param("userId") Long userId,
            @Param("status") Integer status
    );
}
