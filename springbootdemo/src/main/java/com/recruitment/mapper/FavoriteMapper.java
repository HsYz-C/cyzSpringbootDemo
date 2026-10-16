package com.recruitment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.recruitment.model.Favorite;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收藏Mapper
 *
 * @author Web架构技术教学组
 */
@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {
}
