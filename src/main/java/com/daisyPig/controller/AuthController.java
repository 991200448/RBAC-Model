package com.daisyPig.controller;

import com.daisyPig.dto.ApiResponse;
import com.daisyPig.dto.LoginRequest;
import com.daisyPig.dto.RegisterRequest;
import com.daisyPig.entity.User;
import com.daisyPig.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 认证相关接口的控制器，提供用户注册、登录、退出登录和获取当前用户信息的功能。
 */
@RestController
@RequestMapping("/api/auth")
@Slf4j
@Tag(name = "认证管理", description = "用户认证相关的API，包括注册、登录、登出等操作")
public class AuthController {
    @Autowired
    private UserService userService;

    /**
     * 用户注册接口。
     * 请求方式：POST
     * 接口路径：/api/auth/register
     * 参数：
     * - @RequestBody RegisterRequest request：包含用户注册信息的请求体，如用户名、密码等。
     * 返回值：
     * - 若注册成功，返回包含成功消息 "注册成功" 以及注册用户信息的 ApiResponse<User> 对象。
     * - 若注册过程中出现异常，返回包含异常信息的错误响应。
     */
    @Operation(summary = "用户注册", description = "新用户注册接口，需要提供用户名、密码等信息")
    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.register(request);
            return ApiResponse.success("注册成功", user);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 用户登录接口。
     * 请求方式：POST
     * 接口路径：/api/auth/login
     * 参数：
     * - @RequestBody LoginRequest request：包含用户登录信息的请求体，如用户名、密码等。
     * - HttpSession session：用于存储登录用户信息的会话对象。
     * 返回值：
     * - 若登录成功，将用户信息存入 session 中，并返回包含成功消息 "登录成功" 以及登录用户信息的 ApiResponse<String> 对象。
     * - 若登录过程中出现异常，返回包含异常信息的错误响应。
     */
    @Operation(summary = "用户登录", description = "用户登录接口，登录成功后会在session中保存用户信息")
    @PostMapping("/login")
    public ApiResponse<String> login(HttpSession session, @RequestBody LoginRequest requestBody ) {
        try {
            User user = userService.login(requestBody);
            // 将用户信息存入session
            session.setAttribute("currentUser", user);
            System.out.println("login:session ID - " + session.getId());
            return ApiResponse.success("登录成功", session.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 用户退出登录接口。
     * 请求方式：GET
     * 接口路径：/api/auth/logout
     * 参数：
     * - HttpSession session：当前用户的会话对象。
     * 返回值：
     * - 使会话失效，并返回包含成功消息 "退出登录成功" 的 ApiResponse<Void> 对象。
     */
    @GetMapping("/logout")
    public ApiResponse<Void> logout(HttpSession session) {
        try {
            System.out.println("Before session invalidate, session ID: " + session.getId());
            session.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResponse.success("退出登录成功", null);
    }

    /**
     * 获取当前登录用户信息的接口。
     * 请求方式：GET
     * 接口路径：/api/auth/current-user
     * 参数：
     * - HttpSession session：当前用户的会话对象。
     * 返回值：
     * - 若用户已登录，从 session 中获取用户 ID，再通过该 ID 获取完整的用户信息（包括角色），并返回包含该用户信息的 ApiResponse<User> 对象。
     * - 若用户未登录，返回包含错误消息 "未登录" 的错误响应。
     */
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息，包括用户角色等")
    @GetMapping("/current-user")
    public ApiResponse<User> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return ApiResponse.error("未登录");
        }

        // 获取完整的用户信息（包括角色）
        User fullUser = userService.getUserWithRoles(user.getId());
        return ApiResponse.success(fullUser);
    }
}