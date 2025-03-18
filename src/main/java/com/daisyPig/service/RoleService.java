package com.daisyPig.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daisyPig.entity.Role;
import com.daisyPig.entity.RolePermission;
import com.daisyPig.mapper.RoleMapper;
import com.daisyPig.mapper.RolePermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;
    
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    
    public Role getRoleWithPermissions(int roleId) {
        Role role = roleMapper.selectById(roleId);
        if (role != null) {
            role.setPermissions(rolePermissionMapper.getPermissionsByRoleId(roleId));
        }
        return role;
    }

    public Role getRoleByRoleName(String roleName) {
        return roleMapper.getRoleByRoleName(roleName);
    }
    
    public List<Role> getAllRoles() {
        return roleMapper.selectList(null);
    }
    
    @Transactional
    public void createRole(Role role) {
        roleMapper.insert(role);
    }
    
    @Transactional
    public void updateRole(Role role) {
        roleMapper.updateById(role);
    }
    
    @Transactional
    public void deleteRole(int roleId) {
        roleMapper.deleteById(roleId);
        // 关联表的数据会通过外键级联删除
    }
    
    @Transactional
    public void addPermissionToRole(int roleId, int permissionId) {
        // 检查角色是否已有该权限
        QueryWrapper<RolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId).eq("permission_id", permissionId);
        if (rolePermissionMapper.selectCount(queryWrapper) > 0) {
            return; // 已有该权限，无需重复添加
        }
        
        RolePermission rolePermission = new RolePermission();
        rolePermission.setRoleId(roleId);
        rolePermission.setPermissionId(permissionId);
        rolePermissionMapper.insert(rolePermission);
    }
    
    @Transactional
    public void removePermissionFromRole(int roleId, int permissionId) {
        QueryWrapper<RolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId).eq("permission_id", permissionId);
        rolePermissionMapper.delete(queryWrapper);
    }
}