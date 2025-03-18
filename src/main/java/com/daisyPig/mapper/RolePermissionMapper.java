package com.daisyPig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daisyPig.entity.Permission;
import com.daisyPig.entity.RolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {
    @Select("SELECT p.* FROM permissions p JOIN role_permissions rp ON p.id = rp.permission_id WHERE rp.role_id = #{roleId}")
    List<Permission> getPermissionsByRoleId(@Param("roleId") int roleId);
}