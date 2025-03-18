package com.daisyPig.controller;

import com.daisyPig.dto.ApiResponse;
import com.daisyPig.entity.Permission;
import com.daisyPig.service.PermissionService;
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

class PermissionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private PermissionController permissionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(permissionController).build();
    }

    /**
     * 测试获取所有权限的接口。
     * 模拟 PermissionService 的 getAllPermissions 方法返回包含两个权限的列表，
     * 然后向 /api/permissions 发送 GET 请求，
     * 验证响应状态码为 200，响应中的 success 字段为 true，
     * 并且返回的权限数据与模拟数据一致。
     *
     * @throws Exception 当请求处理过程中出现异常时抛出
     */
    @Test
    void getAllPermissions() throws Exception {
        Permission permission1 = new Permission();
        permission1.setId(1);
        permission1.setPermissionName("权限1");

        Permission permission2 = new Permission();
        permission2.setId(2);
        permission2.setPermissionName("权限2");

        List<Permission> permissions = Arrays.asList(permission1, permission2);

        when(permissionService.getAllPermissions()).thenReturn(permissions);

        mockMvc.perform(get("/api/permissions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].permissionName").value("权限1"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].permissionName").value("权限2"));
    }

    /**
     * 测试根据权限 ID 获取单个权限的接口。
     * 模拟 PermissionService 的 getPermissionById 方法返回一个特定权限，
     * 向 /api/permissions/1 发送 GET 请求，
     * 验证响应状态码为 200，响应中的 success 字段为 true，
     * 且返回的权限数据与模拟的权限数据一致。
     *
     * @throws Exception 当请求处理过程中出现异常时抛出
     */
    @Test
    void getPermissionById() throws Exception {
        Permission permission = new Permission();
        permission.setId(1);
        permission.setPermissionName("测试权限");

        when(permissionService.getPermissionById(1)).thenReturn(permission);

        mockMvc.perform(get("/api/permissions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.permissionName").value("测试权限"));
    }

    /**
     * 测试获取不存在的权限的接口情况。
     * 模拟 PermissionService 的 getPermissionById 方法对于 ID 为 999 的权限返回 null，
     * 向 /api/permissions/999 发送 GET 请求，
     * 验证响应状态码为 200，响应中的 success 字段为 false，
     * 并且返回的错误消息为 "权限不存在"。
     *
     * @throws Exception 当请求处理过程中出现异常时抛出
     */
    @Test
    void getPermissionByIdNotFound() throws Exception {
        when(permissionService.getPermissionById(999)).thenReturn(null);

        mockMvc.perform(get("/api/permissions/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("权限不存在"));
    }

    /**
     * 测试创建权限的接口。
     * 模拟 PermissionService 的 createPermission 方法不进行实际操作（doNothing），
     * 向 /api/permissions 发送 POST 请求并携带新权限的 JSON 数据，
     * 验证响应状态码为 200，响应中的 success 字段为 true，
     * 且返回的消息为 "权限创建成功"。
     *
     * @throws Exception 当请求处理过程中出现异常时抛出
     */
    @Test
    void createPermission() throws Exception {
        Permission permission = new Permission();
        permission.setPermissionName("新权限");

        doNothing().when(permissionService).createPermission(any(Permission.class));

        mockMvc.perform(post("/api/permissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"permissionName\":\"新权限\",\"code\":\"new:permission\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("权限创建成功"));
    }

    /**
     * 测试更新权限的接口。
     * 模拟 PermissionService 的 updatePermission 方法不进行实际操作（doNothing），
     * 向 /api/permissions/1 发送 PUT 请求并携带更新后的权限 JSON 数据，
     * 验证响应状态码为 200，响应中的 success 字段为 true，
     * 且返回的消息为 "权限更新成功"。
     *
     * @throws Exception 当请求处理过程中出现异常时抛出
     */
    @Test
    void updatePermission() throws Exception {
        Permission permission = new Permission();
        permission.setId(1);
        permission.setPermissionName("更新权限");

        doNothing().when(permissionService).updatePermission(any(Permission.class));

        mockMvc.perform(put("/api/permissions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"permissionName\":\"更新权限\",\"code\":\"updated:permission\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("权限更新成功"));
    }

    /**
     * 测试删除权限的接口。
     * 模拟 PermissionService 的 deletePermission 方法不进行实际操作（doNothing），
     * 向 /api/permissions/1 发送 DELETE 请求，
     * 验证响应状态码为 200，响应中的 success 字段为 true，
     * 且返回的消息为 "权限删除成功"。
     *
     * @throws Exception 当请求处理过程中出现异常时抛出
     */
    @Test
    void deletePermission() throws Exception {
        doNothing().when(permissionService).deletePermission(1);

        mockMvc.perform(delete("/api/permissions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("权限删除成功"));
    }
}