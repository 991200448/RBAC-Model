package com.daisyPig.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.List;

@Data
@TableName("roles")
public class Role {
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    @TableField("role_name")
    private String roleName;
    
    private String description;
    
    @TableField(exist = false)
    private List<Permission> permissions;
}