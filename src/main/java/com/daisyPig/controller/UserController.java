package com.daisyPig.controller;

import com.daisyPig.annotation.RequirePermission;
import com.daisyPig.dto.ApiResponse;
import com.daisyPig.entity.User;
import com.daisyPig.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "用户管理相关的API，包括查询、更新、删除用户，以及用户角色分配等操作")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 获取所有用户的接口。
     * 请求方式：GET
     * 接口路径：/api/users
     * 权限要求：需要具备 "user:view" 权限。
     * 返回值：ApiResponse<List<User>>，成功时返回包含用户列表的响应，失败时返回错误信息。
     */
    @Operation(summary = "获取所有用户", description = "获取系统中所有用户的列表信息")
    @GetMapping
    @RequirePermission("user:view")
    public ApiResponse<List<User>> getAllUsers() {
        return ApiResponse.success(userService.getAllUsers());
    }

    /**
     * 根据用户 ID 获取单个用户的接口。
     * 请求方式：GET
     * 接口路径：/api/users/{id}
     * 权限要求：需要具备 "user:view" 权限。
     * 参数：@PathVariable int id，用户的 ID。
     * 返回值：ApiResponse<User>，若用户存在则返回包含用户信息的响应，若用户不存在则返回错误信息。
     */
    @Operation(summary = "获取指定用户信息", description = "根据用户ID获取用户的详细信息，包括角色信息")
    @GetMapping("/{id}")
    @RequirePermission("user:view")
    public ApiResponse<User> getUserById(@PathVariable int id) {
        User user = userService.getUserWithRoles(id);
        if (user == null) {
            return ApiResponse.error("用户不存在");
        }
        return ApiResponse.success(user);
    }

    /**
     * 更新用户信息的接口。
     * 请求方式：PUT
     * 接口路径：/api/users/{id}
     * 权限要求：需要具备 "user:edit" 权限。
     * 参数：@PathVariable int id，要更新的用户的 ID；@RequestBody User user，包含更新后用户信息的对象。
     * 返回值：ApiResponse<User>，成功时返回包含更新后用户信息的响应，并附带成功消息 "用户更新成功"，失败时返回错误信息。
     */
    @Operation(summary = "更新用户信息", description = "更新指定用户的信息")
    @PutMapping("/{id}")
    @RequirePermission("user:edit")
    public ApiResponse<User> updateUser(
        @Parameter(description = "用户ID") @PathVariable int id,
        @Parameter(description = "更新后的用户信息") @RequestBody User user) {
        user.setId(id);
        userService.updateUser(user);
        return ApiResponse.success("用户更新成功", user);
    }

    /**
     * 删除用户的接口。
     * 请求方式：DELETE
     * 接口路径：/api/users/{id}
     * 权限要求：需要具备 "user:delete" 权限。
     * 参数：@PathVariable int id，要删除的用户的 ID。
     * 返回值：ApiResponse<Void>，成功时返回包含成功消息 "用户删除成功" 的响应，且数据部分为 null，失败时返回错误信息。
     */
    @Operation(summary = "删除用户", description = "删除指定的用户")
    @DeleteMapping("/{id}")
    @RequirePermission("user:delete")
    public ApiResponse<Void> deleteUser(
        @Parameter(description = "要删除的用户ID") @PathVariable int id) {
        userService.deleteUser(id);
        return ApiResponse.success("用户删除成功", null);
    }

    /**
     * 为用户分配角色的接口。
     * 请求方式：POST
     * 接口路径：/api/users/{userId}/roles/{roleId}
     * 权限要求：需要具备 "user:assign_role" 权限。
     * 参数：@PathVariable int userId，用户的 ID；@PathVariable int roleId，要分配的角色的 ID。
     * 返回值：ApiResponse<Void>，成功时返回包含成功消息 "角色分配成功" 的响应，且数据部分为 null，失败时返回错误信息。
     */
    @Operation(summary = "分配角色给用户", description = "为指定用户分配一个角色")
    @PostMapping("/{userId}/roles/{roleId}")
    @RequirePermission("user:assign_role")
    public ApiResponse<Void> assignRoleToUser(
        @Parameter(description = "用户ID") @PathVariable int userId,
        @Parameter(description = "要分配的角色ID") @PathVariable int roleId) {
        userService.assignRoleToUser(userId, roleId);
        return ApiResponse.success("角色分配成功", null);
    }

    /**
     * 从用户移除角色的接口。
     * 请求方式：DELETE
     * 接口路径：/api/users/{userId}/roles/{roleId}
     * 权限要求：需要具备 "user:remove_role" 权限。
     * 参数：@PathVariable int userId，用户的 ID；@PathVariable int roleId，要移除的角色的 ID。
     * 返回值：ApiResponse<Void>，成功时返回包含成功消息 "角色移除成功" 的响应，且数据部分为 null，失败时返回错误信息。
     */
    @Operation(summary = "移除用户的角色", description = "移除指定用户的指定角色")
    @DeleteMapping("/{userId}/roles/{roleId}")
    @RequirePermission("user:remove_role")
    public ApiResponse<Void> removeRoleFromUser(
        @Parameter(description = "用户ID") @PathVariable int userId,
        @Parameter(description = "要移除的角色ID") @PathVariable int roleId) {
        userService.removeRoleFromUser(userId, roleId);
        return ApiResponse.success("角色移除成功", null);
    }
}