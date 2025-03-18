package com.daisyPig.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("role_permissions")
public class RolePermission {
    @TableId("role_id")
    private Integer roleId;
    
    @TableField("permission_id")
    private int permissionId;
}