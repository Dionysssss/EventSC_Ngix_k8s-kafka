package com.steve.mapper;

import com.steve.entity.Validation;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ValidationMapper {

    @Select("SELECT * FROM Validation WHERE event_id = #{eventId} AND user_id = #{userId}")
    Validation findValidation(@Param("eventId") int eventId, @Param("userId") int userId);

    @Insert("INSERT INTO Validation (event_id, user_id, is_confirmed) VALUES (#{eventId}, #{userId}, #{isConfirmed})")
    void insertValidation(@Param("eventId") int eventId, @Param("userId") int userId, @Param("isConfirmed") boolean isConfirmed);

    @Delete("DELETE FROM Validation WHERE event_id = #{eventId} AND user_id = #{userId} AND is_confirmed = #{isConfirmed}")
    int deleteValidation(@Param("eventId") int eventId, @Param("userId") int userId, @Param("isConfirmed") boolean isConfirmed);

    @Select("SELECT COUNT(*) FROM Validation WHERE event_id = #{eventId} AND is_confirmed = true")
    int countConfirmed(@Param("eventId") int eventId);

    @Select("SELECT COUNT(*) FROM Validation WHERE event_id = #{eventId} AND is_confirmed = false")
    int countFalseReports(@Param("eventId") int eventId);

    @Select("SELECT is_confirmed FROM Validation WHERE event_id = #{eventId} AND user_id = #{userId}")
    Boolean getValidationStatus(@Param("eventId") int eventId, @Param("userId") int userId);
}
