package com.daisyPig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daisyPig.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    @Select("SELECT * FROM roles WHERE role_name = #{roleName}")
    Role getRoleByRoleName(String roleName);
}