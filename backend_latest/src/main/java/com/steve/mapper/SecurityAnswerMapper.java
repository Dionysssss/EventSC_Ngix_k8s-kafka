package com.steve.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface SecurityAnswerMapper {

    @Insert("INSERT INTO SecurityAnswer (user_id, answer) VALUES (#{userId}, #{answer})")
    void insertAnswer(@Param("userId") int userId, @Param("answer") String answer);

    @Select("SELECT COUNT(*) > 0 FROM SecurityAnswer WHERE user_id = #{userId} AND answer = #{answer}")
    boolean isValidAnswer(@Param("userId") int userId, @Param("answer") String answer);
}

