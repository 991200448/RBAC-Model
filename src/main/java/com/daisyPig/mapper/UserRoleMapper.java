package com.daisyPig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daisyPig.entity.Role;
import com.daisyPig.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
    @Select("SELECT r.* FROM roles r JOIN user_roles ur ON r.id = ur.role_id WHERE ur.user_id = #{userId}")
    List<Role> getRolesByUserId(@Param("userId") int userId);

}