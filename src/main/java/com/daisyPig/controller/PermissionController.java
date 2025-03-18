package com.daisyPig.controller;

import com.daisyPig.annotation.RequirePermission;
import com.daisyPig.dto.ApiResponse;
import com.daisyPig.entity.Permission;
import com.daisyPig.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理相关接口的控制器类，提供对权限的增删改查操作。
 */
@RestController
@RequestMapping("/api/permissions")
@Tag(name = "权限管理", description = "权限管理相关的API，包括权限的增删改查等操作")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    /**
     * 获取所有权限信息的接口。
     * 请求方式：GET
     * 接口路径：/api/permissions
     * 权限要求：需要具备 "permission:view" 权限才能调用此接口。
     * 返回值：返回一个包含所有权限信息列表的成功响应。响应数据类型为 ApiResponse<List<Permission>>，若出现异常会返回相应错误响应。
     */
    @Operation(summary = "获取所有权限", description = "获取系统中所有权限的列表信息")
    @GetMapping
    @RequirePermission("permission:view")
    public ApiResponse<List<Permission>> getAllPermissions() {
        return ApiResponse.success(permissionService.getAllPermissions());
    }

    /**
     * 根据权限 ID 获取单个权限信息的接口。
     * 请求方式：GET
     * 接口路径：/api/permissions/{id}
     * 权限要求：需要具备 "permission:view" 权限才能调用此接口。
     * 参数：
     * - @PathVariable int id：要查询的权限的 ID。
     * 返回值：如果该 ID 对应的权限存在，返回包含该权限信息的成功响应；若权限不存在，返回错误响应，提示 "权限不存在"。
     *         响应数据类型为 ApiResponse<Permission>。
     */
    @Operation(summary = "获取指定权限信息", description = "根据权限ID获取权限的详细信息")
    @GetMapping("/{id}")
    @RequirePermission("permission:view")
    public ApiResponse<Permission> getPermissionById(
        @Parameter(description = "权限ID") @PathVariable int id) {
        Permission permission = permissionService.getPermissionById(id);
        if (permission == null) {
            return ApiResponse.error("权限不存在");
        }
        return ApiResponse.success(permission);
    }

    /**
     * 创建新权限的接口。
     * 请求方式：POST
     * 接口路径：/api/permissions
     * 权限要求：需要具备 "permission:create" 权限才能调用此接口。
     * 参数：
     * - @RequestBody Permission permission：包含要创建的权限信息的对象，通过请求体传递。
     * 返回值：返回一个包含创建成功消息 "权限创建成功" 以及新创建的权限信息的成功响应。
     *         响应数据类型为 ApiResponse<Permission>，若创建过程出现异常会返回相应错误响应。
     */
    @Operation(summary = "创建权限", description = "创建一个新的权限")
    @PostMapping
    @RequirePermission("permission:create")
    public ApiResponse<Permission> createPermission(
        @Parameter(description = "权限信息") @RequestBody Permission permission) {
        permissionService.createPermission(permission);
        return ApiResponse.success("权限创建成功", permission);
    }

    /**
     * 更新指定 ID 权限信息的接口。
     * 请求方式：PUT
     * 接口路径：/api/permissions/{id}
     * 权限要求：需要具备 "permission:edit" 权限才能调用此接口。
     * 参数：
     * - @PathVariable int id：要更新的权限的 ID。
     * - @RequestBody Permission permission：包含更新后权限信息的对象，通过请求体传递。
     * 返回值：返回一个包含更新成功消息 "权限更新成功" 以及更新后的权限信息的成功响应。
     *         响应数据类型为 ApiResponse<Permission>，若更新过程出现异常会返回相应错误响应。
     */
    @Operation(summary = "更新权限信息", description = "更新指定权限的信息")
    @PutMapping("/{id}")
    @RequirePermission("permission:edit")
    public ApiResponse<Permission> updatePermission(
        @Parameter(description = "权限ID") @PathVariable int id,
        @Parameter(description = "更新后的权限信息") @RequestBody Permission permission) {
        permission.setId(id);
        permissionService.updatePermission(permission);
        return ApiResponse.success("权限更新成功", permission);
    }

    /**
     * 删除指定 ID 权限的接口。
     * 请求方式：DELETE
     * 接口路径：/api/permissions/{id}
     * 权限要求：需要具备 "permission:delete" 权限才能调用此接口。
     * 参数：
     * - @PathVariable int id：要删除的权限的 ID。
     * 返回值：返回一个包含删除成功消息 "权限删除成功" 的成功响应，响应的数据部分为 null。
     *         响应数据类型为 ApiResponse<Void>，若删除过程出现异常会返回相应错误响应。
     */
    @Operation(summary = "删除权限", description = "删除指定的权限")
    @DeleteMapping("/{id}")
    @RequirePermission("permission:delete")
    public ApiResponse<Void> deletePermission(
        @Parameter(description = "要删除的权限ID") @PathVariable int id) {
        permissionService.deletePermission(id);
        return ApiResponse.success("权限删除成功", null);
    }
}