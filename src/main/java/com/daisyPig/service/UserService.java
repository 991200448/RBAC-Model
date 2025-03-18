package com.daisyPig.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daisyPig.dto.LoginRequest;
import com.daisyPig.dto.RegisterRequest;
import com.daisyPig.entity.Role;
import com.daisyPig.entity.User;
import com.daisyPig.entity.UserRole;
import com.daisyPig.mapper.RolePermissionMapper;
import com.daisyPig.mapper.UserMapper;
import com.daisyPig.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserRoleMapper userRoleMapper;
    
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;
    
    @Transactional
    public User register(RegisterRequest request) {
        // 检查用户名是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", request.getUsername());
        if (userMapper.selectCount(queryWrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setCreatedAt(LocalDateTime.now());
        
        userMapper.insert(user);
        
        // 默认分配普通用户角色
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        Role regularUserRole = roleService.getRoleByRoleName("RegularUser");
        userRole.setRoleId(regularUserRole.getId()); // 普通用户角色ID
        userRoleMapper.insert(userRole);
        
        return user;
    }
    
    public User login(LoginRequest request) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", request.getUsername());
        User user = userMapper.selectOne(queryWrapper);
        
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        return user;
    }
    
    public User getUserWithRoles(int userId) {
        User user = userMapper.selectById(userId);
        if (user != null) {
            List<Role> roles = userRoleMapper.getRolesByUserId(userId);
            // 为每个角色加载权限
            for (Role role : roles) {
                role.setPermissions(rolePermissionMapper.getPermissionsByRoleId(role.getId()));
            }
            user.setRoles(roles);
        }
        return user;
    }
    
    public List<User> getAllUsers() {
        return userMapper.selectList(null);
    }
    
    @Transactional
    public void updateUser(User user) {
        // 不更新密码，如果需要更新密码应该有单独的方法
        User existingUser = userMapper.selectById(user.getId());
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }
        
        user.setPassword(existingUser.getPassword());
        userMapper.updateById(user);
    }
    
    @Transactional
    public void deleteUser(int userId) {
        userMapper.deleteById(userId);
        // 关联表的数据会通过外键级联删除
    }
    
    @Transactional
    public void assignRoleToUser(int userId, int roleId) {
        // 检查用户是否已有该角色
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("role_id", roleId);
        if (userRoleMapper.selectCount(queryWrapper) > 0) {
            return; // 已有该角色，无需重复分配
        }
        
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRoleMapper.insert(userRole);
    }
    
    @Transactional
    public void removeRoleFromUser(int userId, int roleId) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("role_id", roleId);
        userRoleMapper.delete(queryWrapper);
    }
}