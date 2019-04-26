package com.ccy.easyzhihu.Dao;

import com.ccy.easyzhihu.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author chengcongyue
 * @version 1.0
 * @description com.ccy.easyzhihu.Dao
 * @date 2019/4/14
 */
@Mapper
public interface CommentDAO {

    String TABLE_NAME="comment";
    String INSERT_FIELDS="content,user_id,entity_id,entity_type,created_date,status";
    String SELECT_FIELDS=" id,"+INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,") values(#{content},#{userId},#{entityId},#{entityType},#{createdDate},#{status})",})
    int addComment(Comment comment);

    @Select({"select "+SELECT_FIELDS+" from "+TABLE_NAME+" where id=#{id}"})
    Comment getCommentById(@Param("id") int id);
    @Select({"select count(id) from "+TABLE_NAME+" where user_id=#{id}"})
    int getUserCommentCount(@Param("id") int id);
    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where entity_id=#{entityId} and " +
            "entity_type=#{entityType} order by id desc"})
    List<Comment> selectByEntity(@Param("entityId")int entityId,@Param("entityType")int entityType);

    @Update({"update ", TABLE_NAME, " set status=#{status} where entity_id=#{entityId} and entity_type=#{entityType}"})
    void updateStatus(@Param("entityId") int entityId,@Param("entityType") int entityType,@Param("status")int status);

    @Select({"select count(id) from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType} "})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);


}
