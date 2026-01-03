package com.steve.mapper;

import com.steve.entity.Comment;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Insert;


import java.util.List;

@Mapper
public interface CommentMapper {
    void insertComment(Comment comment);

    List<Comment> findCommentsByEventId(int eventId);

    //Delete Comments By Id
    @Delete("DELETE FROM Comment WHERE event_id = #{eventId}")
    int deleteCommentsByEventId(int eventId);
}
