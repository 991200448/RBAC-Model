package com.daisyPig.service;

import com.daisyPig.entity.Permission;
import com.daisyPig.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    
    public List<Permission> getAllPermissions() {
        return permissionMapper.selectList(null);
    }
    
    public Permission getPermissionById(int id) {
        return permissionMapper.selectById(id);
    }
    
    @Transactional
    public void createPermission(Permission permission) {
        permissionMapper.insert(permission);
    }
    
    @Transactional
    public void updatePermission(Permission permission) {
        permissionMapper.updateById(permission);
    }
    
    @Transactional
    public void deletePermission(int id) {
        permissionMapper.deleteById(id);
    }
}