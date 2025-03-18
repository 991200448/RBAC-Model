package com.daisyPig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daisyPig.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
}