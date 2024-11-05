package com.steve.mapper;

import com.steve.entity.VerificationToken;
import org.apache.ibatis.annotations.*;

@Mapper
public interface VerificationTokenMapper {

    @Insert("INSERT INTO VerificationToken (token, user_id, expiration) VALUES (#{token}, #{userId}, #{expiration})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertVerificationToken(VerificationToken verificationToken);

    @Select("SELECT * FROM VerificationToken WHERE token = #{token}")
    VerificationToken findByToken(@Param("token") String token);

    @Select("SELECT * FROM VerificationToken WHERE token = #{token} AND user_id = #{userId}")
    VerificationToken findByCodeAndUserId(@Param("token") String token, @Param("userId") int userId);

    @Delete("DELETE FROM VerificationToken WHERE id = #{id}")
    void deleteVerificationToken(@Param("id") int id);

    @Delete("DELETE FROM VerificationToken WHERE expiration < NOW()")
    void deleteExpiredTokens();
}