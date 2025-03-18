package com.daisyPig.controller;

import com.daisyPig.entity.User;
import com.daisyPig.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    /**
     * 测试获取所有用户的接口。
     * 模拟 UserService 的 getAllUsers 方法返回包含两个用户的列表，
     * 然后向 /api/users 发送 GET 请求，
     * 验证响应状态码为 200，响应中的 success 字段为 true，
     * 并且返回的用户数据与模拟数据一致。
     *
     * @throws Exception 当请求处理过程中出现异常时抛出
     */
    @Test
    void getAllUsers() throws Exception {
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2);
        user2.setUsername("user2");

        List<User> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].username").value("user1"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].username").value("user2"));
    }

    /**
     * 测试根据用户 ID 获取单个用户的接口。
     * 模拟 UserService 的 getUserWithRoles 方法返回一个特定用户，
     * 向 /api/users/1 发送 GET 请求，
     * 验证响应状态码为 200，响应中的 success 字段为 true，
     * 且返回的用户数据与模拟的用户数据一致。
     *
     * @throws Exception 当请求处理过程中出现异常时抛出
     */
    @Test
    void getUserById() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("testUser");

        when(userService.getUserWithRoles(1)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("testUser"));
    }

    /**
     * 测试获取不存在的用户的接口情况。
     * 模拟 UserService 的 getUserWithRoles 方法对于 ID 为 999 的用户返回 null，
     * 向 /api/users/999 发送 GET 请求，
     * 验证响应状态码为 200，响应中的 success 字段为 false，
     * 并且返回的错误消息为 "用户不存在"。
     *
     * @throws Exception 当请求处理过程中出现异常时抛出
     */
    @Test
    void getUserByIdNotFound() throws Exception {
        when(userService.getUserWithRoles(999)).thenReturn(null);

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("用户不存在"));
    }

    /**
     * 测试更新用户信息的接口。
     * 模拟 UserService 的 updateUser 方法不进行实际操作（doNothing），
     * 向 /api/users/1 发送 PUT 请求并携带更新后的用户 JSON 数据，
     * 验证响应状态码为 200，响应中的 success 字段为 true，
     * 且返回的消息为 "用户更新成功"。
     *
     * @throws Exception 当请求处理过程中出现异常时抛出
     */
    @Test
    void updateUser() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("updatedUser");

        doNothing().when(userService).updateUser(any(User.class));

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"updatedUser\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("用户更新成功"));
    }

    /**
     * 测试删除用户的接口。
     * 模拟 UserService 的 deleteUser 方法不进行实际操作（doNothing），
     * 向 /api/users/1 发送 DELETE 请求，
     * 验证响应状态码为 200，响应中的 success 字段为 true，
     * 且返回的消息为 "用户删除成功"。
     *
     * @throws Exception 当请求处理过程中出现异常时抛出
     */
    @Test
    void deleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("用户删除成功"));
    }

    /**
     * 测试为用户分配角色的接口。
     * 模拟 UserService 的 assignRoleToUser 方法不进行实际操作（doNothing），
     * 向 /api/users/1/roles/2 发送 POST 请求，
     * 验证响应状态码为 200，响应中的 success 字段为 true，
     * 且返回的消息为 "角色分配成功"。
     *
     * @throws Exception 当请求处理过程中出现异常时抛出
     */
    @Test
    void assignRoleToUser() throws Exception {
        doNothing().when(userService).assignRoleToUser(1, 2);

        mockMvc.perform(post("/api/users/1/roles/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("角色分配成功"));
    }

    /**
     * 测试从用户移除角色的接口。
     * 模拟 UserService 的 removeRoleFromUser 方法不进行实际操作（doNothing），
     * 向 /api/users/1/roles/2 发送 DELETE 请求，
     * 验证响应状态码为 200，响应中的 success 字段为 true，
     * 且返回的消息为 "角色移除成功"。
     *
     * @throws Exception 当请求处理过程中出现异常时抛出
     */
    @Test
    void removeRoleFromUser() throws Exception {
        doNothing().when(userService).removeRoleFromUser(1, 2);

        mockMvc.perform(delete("/api/users/1/roles/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("角色移除成功"));
    }
}