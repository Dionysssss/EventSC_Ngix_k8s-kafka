package com.steve.mapper;

import com.steve.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO User (email, password, isAuthenticated) VALUES (#{email}, #{password}, false)")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    void insertUser(User user);

    @Select("SELECT * FROM User WHERE user_id = #{id}")
    User findUserById(int id);

    @Select("SELECT * FROM User WHERE email = #{email}")
    User findUserByEmail(String email);

    @Update("UPDATE User SET password = #{password} WHERE user_id = #{userId}")
    void updatePassword(@Param("userId") int userId, @Param("password") String password);

    // Additional database interaction methods can be added here
}
