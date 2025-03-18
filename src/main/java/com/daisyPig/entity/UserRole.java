package com.daisyPig.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("user_roles")
public class UserRole {
    @TableId("user_id")
    private Integer userId;
    
    @TableField("role_id")
    private int roleId;
}