package com.daisyPig.interceptor;

import com.daisyPig.annotation.RequirePermission;
import com.daisyPig.entity.Permission;
import com.daisyPig.entity.Role;
import com.daisyPig.entity.User;
import com.daisyPig.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    
    @Autowired
    private UserService userService;
    


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法，直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        
        // 获取方法上的RequirePermission注解
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequirePermission requirePermission = handlerMethod.getMethodAnnotation(RequirePermission.class);
        
        // 如果没有注解，则不需要权限验证
        if (requirePermission == null) {
            return true;
        }
        
        // 获取当前登录用户
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");
        
        // 如果未登录，则拒绝访问
        if (user == null) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"success\":false,\"message\":\"请先登录\",\"data\":null}");
            return false;
        }
        
        // 获取用户的完整信息（包括角色和权限）
        User fullUser = userService.getUserWithRoles(user.getId());
        
        // 获取需要的权限
        String requiredPermission = requirePermission.value();
        
        // 检查用户是否有所需权限
        if (hasPermission(fullUser, requiredPermission)) {
            return true;
        } else {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"success\":false,\"message\":\"权限不足\",\"data\":null}");
            return false;
        }
    }
    
    private boolean hasPermission(User user, String requiredPermission) {
        if (user.getRoles() == null) {
            return false;
        }
        
        // 遍历用户的所有角色
        for (Role role : user.getRoles()) {
            if (role.getPermissions() == null) {
                continue;
            }
            
            // 遍历角色的所有权限
            for (Permission permission : role.getPermissions()) {
                if (permission.getPermissionName().equals(requiredPermission)) {
                    return true;
                }
            }
        }
        
        return false;
    }
}