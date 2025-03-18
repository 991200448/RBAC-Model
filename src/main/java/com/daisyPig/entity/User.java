package com.daisyPig.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("users") // 注意表名是users
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    private String username;
    private String password;
    private String email;
    
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    @TableField(exist = false)
    private List<Role> roles;
}