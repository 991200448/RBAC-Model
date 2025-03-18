package com.daisyPig.controller;

import com.daisyPig.annotation.RequirePermission;
import com.daisyPig.dto.ApiResponse;
import com.daisyPig.entity.Role;
import com.daisyPig.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "角色管理", description = "角色管理相关的API，包括角色的增删改查，以及角色权限的分配和移除等操作")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 获取所有角色的接口。
     * 请求方式：GET
     * 接口路径：/api/roles
     * 权限要求：需要具备 "role:view" 权限。
     * 返回值：ApiResponse<List<Role>>，返回包含所有角色列表的成功响应，若失败则返回错误响应。
     */
    @Operation(summary = "获取所有角色", description = "获取系统中所有角色的列表信息")
    @GetMapping
    @RequirePermission("role:view")
    public ApiResponse<List<Role>> getAllRoles() {
        return ApiResponse.success(roleService.getAllRoles());
    }

    /**
     * 根据角色 ID 获取角色及其权限信息的接口。
     * 请求方式：GET
     * 接口路径：/api/roles/{id}
     * 权限要求：需要具备 "role:view" 权限。
     * 参数：@PathVariable int id，要获取信息的角色的 ID。
     * 返回值：ApiResponse<Role>，若角色存在则返回包含该角色及其权限信息的成功响应，若角色不存在则返回错误响应，提示 "角色不存在"。
     */
    @Operation(summary = "获取指定角色信息", description = "根据角色ID获取角色的详细信息，包括权限信息")
    @GetMapping("/{id}")
    @RequirePermission("role:view")
    public ApiResponse<Role> getRoleWithPermissions(
        @Parameter(description = "角色ID") @PathVariable int id) {
        Role role = roleService.getRoleWithPermissions(id);
        if (role == null) {
            return ApiResponse.error("角色不存在");
        }
        return ApiResponse.success(role);
    }

    /**
     * 创建角色的接口。
     * 请求方式：POST
     * 接口路径：/api/roles
     * 权限要求：需要具备 "role:create" 权限。
     * 参数：@RequestBody Role role，包含要创建角色信息的 Role 对象。
     * 返回值：ApiResponse<Role>，返回包含创建成功消息 "角色创建成功" 以及新创建角色信息的成功响应，若失败则返回错误响应。
     */
    @Operation(summary = "创建角色", description = "创建一个新的角色")
    @PostMapping
    @RequirePermission("role:create")
    public ApiResponse<Role> createRole(
        @Parameter(description = "角色信息") @RequestBody Role role) {
        roleService.createRole(role);
        return ApiResponse.success("角色创建成功", role);
    }

    /**
     * 更新角色信息的接口。
     * 请求方式：PUT
     * 接口路径：/api/roles/{id}
     * 权限要求：需要具备 "role:edit" 权限。
     * 参数：@PathVariable int id，要更新的角色的 ID；@RequestBody Role role，包含更新后角色信息的 Role 对象。
     * 返回值：ApiResponse<Role>，返回包含更新成功消息 "角色更新成功" 以及更新后角色信息的成功响应，若失败则返回错误响应。
     */
    @Operation(summary = "更新角色信息", description = "更新指定角色的信息")
    @PutMapping("/{id}")
    @RequirePermission("role:edit")
    public ApiResponse<Role> updateRole(
        @Parameter(description = "角色ID") @PathVariable int id,
        @Parameter(description = "更新后的角色信息") @RequestBody Role role) {
        role.setId(id);
        roleService.updateRole(role);
        return ApiResponse.success("角色更新成功", role);
    }

    /**
     * 删除角色的接口。
     * 请求方式：DELETE
     * 接口路径：/api/roles/{id}
     * 权限要求：需要具备 "role:delete" 权限。
     * 参数：@PathVariable int id，要删除的角色的 ID。
     * 返回值：ApiResponse<Void>，返回包含删除成功消息 "角色删除成功" 的成功响应，数据部分为 null，若失败则返回错误响应。
     */
    @Operation(summary = "删除角色", description = "删除指定的角色")
    @DeleteMapping("/{id}")
    @RequirePermission("role:delete")
    public ApiResponse<Void> deleteRole(
        @Parameter(description = "要删除的角色ID") @PathVariable int id) {
        roleService.deleteRole(id);
        return ApiResponse.success("角色删除成功", null);
    }

    /**
     * 为角色添加权限的接口。
     * 请求方式：POST
     * 接口路径：/api/roles/{roleId}/permissions/{permissionId}
     * 权限要求：需要具备 "role:assign_permission" 权限。
     * 参数：@PathVariable int roleId，要添加权限的角色的 ID；@PathVariable int permissionId，要添加的权限的 ID。
     * 返回值：ApiResponse<Void>，返回包含添加成功消息 "权限添加成功" 的成功响应，数据部分为 null，若失败则返回错误响应。
     */
    @Operation(summary = "为角色添加权限", description = "为指定角色添加一个权限")
    @PostMapping("/{roleId}/permissions/{permissionId}")
    @RequirePermission("role:assign_permission")
    public ApiResponse<Void> addPermissionToRole(
        @Parameter(description = "角色ID") @PathVariable int roleId,
        @Parameter(description = "要添加的权限ID") @PathVariable int permissionId) {
        roleService.addPermissionToRole(roleId, permissionId);
        return ApiResponse.success("权限添加成功", null);
    }

    /**
     * 从角色移除权限的接口。
     * 请求方式：DELETE
     * 接口路径：/api/roles/{roleId}/permissions/{permissionId}
     * 权限要求：需要具备 "role:remove_permission" 权限。
     * 参数：@PathVariable int roleId，要移除权限的角色的 ID；@PathVariable int permissionId，要移除的权限的 ID。
     * 返回值：ApiResponse<Void>，返回包含移除成功消息 "权限移除成功" 的成功响应，数据部分为 null，若失败则返回错误响应。
     */
    @Operation(summary = "移除角色的权限", description = "移除指定角色的指定权限")
    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    @RequirePermission("role:remove_permission")
    public ApiResponse<Void> removePermissionFromRole(
        @Parameter(description = "角色ID") @PathVariable int roleId,
        @Parameter(description = "要移除的权限ID") @PathVariable int permissionId) {
        roleService.removePermissionFromRole(roleId, permissionId);
        return ApiResponse.success("权限移除成功", null);
    }
}