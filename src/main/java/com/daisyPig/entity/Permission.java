package com.daisyPig.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("permissions")
public class Permission {
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    @TableField("permission_name")
    private String permissionName;
    
    private String description;
}