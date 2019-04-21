package com.ccy.easyzhihu.Dao;

import com.ccy.easyzhihu.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author chengcongyue
 * @version 1.0
 * @description com.ccy.easyzhihu.Dao
 * @date 2019/3/24
 */
@Mapper
public interface QuestionDAO {

    //表名
    String TABLE_NAME=" question ";
    String INSERT_FIELDS=" title,content,user_id,created_date,comment_count ";
    String SELECT_FIELDS=" id,"+INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME ," (",
            INSERT_FIELDS,")values(#{title},#{content},#{userId},#{createdDate},#{commentCount})"})
    int addQuestion(Question question);

    List<Question> selectLatestQuestions(@Param("userId") int userId,@Param("offset") int offset
     ,@Param("limit") int limit);

    @Select({"select ",SELECT_FIELDS ," from ",TABLE_NAME," where id=#{id}"})
    Question getQuestionById(String id);

    @Update({"update ", TABLE_NAME, " set comment_count=#{count} where id=#{id}"})
    void updateCommentCount(@Param("id")int id,@Param("count") int count);
}
