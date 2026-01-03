package com.steve.mapper;

import com.steve.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO User (email, password, isAuthenticated) VALUES (#{email}, #{password}, false)")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    void insertUser(User user);

    @Select("SELECT * FROM User WHERE user_id = #{id}")
    User findUserById(int id);

    @Select("SELECT * FROM User WHERE email = #{email}")
    User findUserByEmail(String email);

    @Select("SELECT * FROM User")
    List<User> findAllUsers();

    @Update("UPDATE User SET password = #{password} WHERE user_id = #{userId}")
    void updatePassword(@Param("userId") int userId, @Param("password") String password);

    @Delete("DELETE FROM User WHERE user_id = #{userId}")
    void deleteUserById(int userId);

    @Delete("DELETE FROM User")
    void deleteAllUsers();
    // Additional database interaction methods can be added here
}
