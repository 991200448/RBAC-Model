package com.daisyPig.controller;

import com.daisyPig.dto.ApiResponse;
import com.daisyPig.dto.LoginRequest;
import com.daisyPig.dto.RegisterRequest;
import com.daisyPig.entity.User;
import com.daisyPig.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 测试用户注册成功的情况。
     * 模拟 UserService 的 register 方法返回一个用户对象，
     * 调用 AuthController 的 register 方法，
     * 验证返回的 ApiResponse 的 success 字段为 true，
     * 消息为 "注册成功"，并且返回的数据与模拟的用户对象一致。
     */
    @Test
    void register_Success() {
        // 准备测试数据
        RegisterRequest request = new RegisterRequest();
        User expectedUser = new User();
        when(userService.register(request)).thenReturn(expectedUser);

        // 执行测试
        ApiResponse<User> response = authController.register(request);

        // 验证结果
        assertTrue(response.isSuccess());
        assertEquals("注册成功", response.getMessage());
        assertEquals(expectedUser, response.getData());
    }

    /**
     * 测试用户注册失败的情况。
     * 模拟 UserService 的 register 方法抛出一个运行时异常，
     * 调用 AuthController 的 register 方法，
     * 验证返回的 ApiResponse 的 success 字段为 false，
     * 消息与抛出的异常信息一致，并且返回的数据为 null。
     */
    @Test
    void register_Failure() {
        // 准备测试数据
        RegisterRequest request = new RegisterRequest();
        String errorMessage = "用户名已存在";
        when(userService.register(request)).thenThrow(new RuntimeException(errorMessage));

        // 执行测试
        ApiResponse<User> response = authController.register(request);

        // 验证结果
        assertFalse(response.isSuccess());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
    }

    /**
     * 测试用户登录成功的情况。
     * 模拟 UserService 的 login 方法返回一个用户对象，
     * 调用 AuthController 的 login 方法并传入模拟的 HttpSession，
     * 验证返回的 ApiResponse 的 success 字段为 true，
     * 消息为 "登录成功"，返回的数据与模拟的用户对象一致，
     * 并且验证 HttpSession 的 setAttribute 方法被调用，设置了当前用户。
     */
    @Test
    void login_Success() {
        // 准备测试数据
        LoginRequest request = new LoginRequest();
        User expectedUser = new User();
        when(userService.login(request)).thenReturn(expectedUser);

        // 执行测试
        ApiResponse<User> response = authController.login(session,request);

        // 验证结果
        assertTrue(response.isSuccess());
        assertEquals("登录成功", response.getMessage());
        assertEquals(expectedUser, response.getData());
        verify(session).setAttribute("currentUser", expectedUser);
    }

    /**
     * 测试用户登录失败的情况。
     * 模拟 UserService 的 login 方法抛出一个运行时异常，
     * 调用 AuthController 的 login 方法并传入模拟的 HttpSession，
     * 验证返回的 ApiResponse 的 success 字段为 false，
     * 消息与抛出的异常信息一致，返回的数据为 null，
     * 并且验证 HttpSession 的 setAttribute 方法未被调用。
     */
    @Test
    void login_Failure() {
        // 准备测试数据
        LoginRequest request = new LoginRequest();
        String errorMessage = "用户名或密码错误";
        when(userService.login(request)).thenThrow(new RuntimeException(errorMessage));

        // 执行测试
        ApiResponse<User> response = authController.login(session,request);

        // 验证结果
        assertFalse(response.isSuccess());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(session, never()).setAttribute(eq("currentUser"), any());
    }

    /**
     * 测试用户退出登录成功的情况。
     * 调用 AuthController 的 logout 方法并传入模拟的 HttpSession，
     * 验证返回的 ApiResponse 的 success 字段为 true，
     * 消息为 "退出登录成功"，返回的数据为 null，
     * 并且验证 HttpSession 的 invalidate 方法被调用。
     */
    @Test
    void logout_Success() {
        // 执行测试
        ApiResponse<Void> response = authController.logout(session);

        // 验证结果
        assertTrue(response.isSuccess());
        assertEquals("退出登录成功", response.getMessage());
        assertNull(response.getData());
        verify(session).invalidate();
    }

    /**
     * 测试获取已登录用户信息的情况。
     * 模拟 HttpSession 的 getAttribute 方法返回一个用户对象，
     * 模拟 UserService 的 getUserWithRoles 方法返回一个完整的用户对象，
     * 调用 AuthController 的 getCurrentUser 方法并传入模拟的 HttpSession，
     * 验证返回的 ApiResponse 的 success 字段为 true，
     * 返回的数据与模拟的完整用户对象一致。
     */
    @Test
    void getCurrentUser_WhenLoggedIn() {
        // 准备测试数据
        User sessionUser = new User();
        sessionUser.setId(1);
        User fullUser = new User();
        when(session.getAttribute("currentUser")).thenReturn(sessionUser);
        when(userService.getUserWithRoles(1)).thenReturn(fullUser);

        // 执行测试
        ApiResponse<User> response = authController.getCurrentUser(session);

        // 验证结果
        assertTrue(response.isSuccess());
        assertEquals(fullUser, response.getData());
    }

    /**
     * 测试获取未登录用户信息的情况。
     * 模拟 HttpSession 的 getAttribute 方法返回 null，
     * 调用 AuthController 的 getCurrentUser 方法并传入模拟的 HttpSession，
     * 验证返回的 ApiResponse 的 success 字段为 false，
     * 消息为 "未登录"，返回的数据为 null，
     * 并且验证 UserService 的 getUserWithRoles 方法未被调用。
     */
    @Test
    void getCurrentUser_WhenNotLoggedIn() {
        // 准备测试数据
        when(session.getAttribute("currentUser")).thenReturn(null);

        // 执行测试
        ApiResponse<User> response = authController.getCurrentUser(session);

        // 验证结果
        assertFalse(response.isSuccess());
        assertEquals("未登录", response.getMessage());
        assertNull(response.getData());
        verify(userService, never()).getUserWithRoles(anyInt());
    }
}