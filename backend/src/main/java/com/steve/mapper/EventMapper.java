package com.steve.mapper;

import com.steve.entity.Event;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EventMapper {

    void insertEvent(Event event);

    List<Event> findAllEvents(@Param("category") String category, @Param("date") String date);

    @Select("SELECT * FROM Event WHERE event_id = #{id}")
    Event findUserById(int id);
}
