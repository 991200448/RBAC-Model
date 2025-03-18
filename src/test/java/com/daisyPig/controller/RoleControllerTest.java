package com.daisyPig.controller;

import com.daisyPig.entity.Role;
import com.daisyPig.service.RoleService;
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

class RoleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
    }

    /**
     * 测试获取所有角色的接口。
     * 该方法会模拟 RoleService 的 getAllRoles 方法返回两个角色的列表，
     * 然后发起 GET 请求到 /api/roles 接口，
     * 并验证响应状态码为 200，响应中的 success 字段为 true，
     * 以及返回的角色数据是否与模拟数据一致。
     *
     * @throws Exception 当请求处理过程中出现异常时抛出
     */
    @Test
    void getAllRoles() throws Exception {
        Role role1 = new Role();
        role1.setId(1);
        role1.setRoleName("角色1");

        Role role2 = new Role();
        role2.setId(2);
        role2.setRoleName("角色2");

        List<Role> roles = Arrays.asList(role1, role2);

        when(roleService.getAllRoles()).thenReturn(roles);

        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].roleName").value("角色1"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].roleName").value("角色2"));
    }

    /**
     * 测试根据角色 ID 获取角色及其权限的接口。
     * 该方法会模拟 RoleService 的 getRoleWithPermissions 方法返回一个角色，
     * 然后发起 GET 请求到 /api/roles/1 接口，
     * 并验证响应状态码为 200，响应中的 success 字段为 true，
     * 以及返回的角色数据是否与模拟数据一致。
     *
     * @throws Exception 当请求处理过程中出现异常时抛出
     */
    @Test
    void getRoleWithPermissions() throws Exception {
        Role role = new Role();
        role.setId(1);
        role.setRoleName("测试角色");

        when(roleService.getRoleWithPermissions(1)).thenReturn(role);

        mockMvc.perform(get("/api/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.roleName").value("测试角色"));
    }

    /**
     * 测试获取不存在的角色及其权限的接口。
     * 该方法会模拟 RoleService 的 getRoleWithPermissions 方法返回 null，
     * 然后发起 GET 请求到 /api/roles/999 接口，
     * 并验证响应状态码为 200，响应中的 success 字段为 false，
     * 以及返回的错误消息是否为 "角色不存在"。
     *
     * @throws Exception 当请求处理过程中出现异常时抛出
     */
    @Test
    void getRoleWithPermissionsNotFound() throws Exception {
        when(roleService.getRoleWithPermissions(999)).thenReturn(null);

        mockMvc.perform(get("/api/roles/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("角色不存在"));
    }

    /**
     * 测试创建角色的接口。
     * 该方法会模拟 RoleService 的 createRole 方法不做任何操作，
     * 然后发起 POST 请求到 /api/roles 接口，
     * 并验证响应状态码为 200，响应中的 success 字段为 true，
     * 以及返回的消息是否为 "角色创建成功"。
     *
     * @throws Exception 当请求处理过程中出现异常时抛出
     */
    @Test
    void createRole() throws Exception {
        Role role = new Role();
        role.setRoleName("新角色");

        doNothing().when(roleService).createRole(any(Role.class));

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roleName\":\"新角色\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("角色创建成功"));
    }

    /**
     * 测试更新角色的接口。
     * 该方法会模拟 RoleService 的 updateRole 方法不做任何操作，
     * 然后发起 PUT 请求到 /api/roles/1 接口，
     * 并验证响应状态码为 200，响应中的 success 字段为 true，
     * 以及返回的消息是否为 "角色更新成功"。
     *
     * @throws Exception 当请求处理过程中出现异常时抛出
     */
    @Test
    void updateRole() throws Exception {
        Role role = new Role();
        role.setId(1);
        role.setRoleName("更新角色");

        doNothing().when(roleService).updateRole(any(Role.class));

        mockMvc.perform(put("/api/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roleName\":\"更新角色\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("角色更新成功"));
    }

    /**
     * 测试删除角色的接口。
     * 该方法会模拟 RoleService 的 deleteRole 方法不做任何操作，
     * 然后发起 DELETE 请求到 /api/roles/1 接口，
     * 并验证响应状态码为 200，响应中的 success 字段为 true，
     * 以及返回的消息是否为 "角色删除成功"。
     *
     * @throws Exception 当请求处理过程中出现异常时抛出
     */
    @Test
    void deleteRole() throws Exception {
        doNothing().when(roleService).deleteRole(1);

        mockMvc.perform(delete("/api/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("角色删除成功"));
    }

    /**
     * 测试为角色添加权限的接口。
     * 该方法会模拟 RoleService 的 addPermissionToRole 方法不做任何操作，
     * 然后发起 POST 请求到 /api/roles/1/permissions/2 接口，
     * 并验证响应状态码为 200，响应中的 success 字段为 true，
     * 以及返回的消息是否为 "权限添加成功"。
     *
     * @throws Exception 当请求处理过程中出现异常时抛出
     */
    @Test
    void addPermissionToRole() throws Exception {
        doNothing().when(roleService).addPermissionToRole(1, 2);

        mockMvc.perform(post("/api/roles/1/permissions/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("权限添加成功"));
    }

    /**
     * 测试从角色中移除权限的接口。
     * 该方法会模拟 RoleService 的 removePermissionFromRole 方法不做任何操作，
     * 然后发起 DELETE 请求到 /api/roles/1/permissions/2 接口，
     * 并验证响应状态码为 200，响应中的 success 字段为 true，
     * 以及返回的消息是否为 "权限移除成功"。
     *
     * @throws Exception 当请求处理过程中出现异常时抛出
     */
    @Test
    void removePermissionFromRole() throws Exception {
        doNothing().when(roleService).removePermissionFromRole(1, 2);

        mockMvc.perform(delete("/api/roles/1/permissions/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("权限移除成功"));
    }
}