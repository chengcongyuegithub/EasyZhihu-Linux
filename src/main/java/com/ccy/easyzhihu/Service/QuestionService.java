package com.ccy.easyzhihu.Service;

import com.ccy.easyzhihu.Dao.QuestionDAO;
import com.ccy.easyzhihu.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author chengcongyue
 * @version 1.0
 * @description com.ccy.easyzhihu.Service
 * @date 2019/3/27
 */
@Service
public class QuestionService {
     @Autowired
     QuestionDAO questionDAO;

     @Autowired
     SensitiveService sensitiveService;
     public int addQuestion(Question question)
     {
         //过滤html标签
         question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
         question.setContent(HtmlUtils.htmlEscape(question.getContent()));
         //过滤敏感词
         question.setTitle(sensitiveService.filter(question.getTitle()));
         question.setContent(sensitiveService.filter(question.getContent()));
         return questionDAO.addQuestion(question)>0?question.getId():0;
     }

     public List<Question> getSelectLatestQuestions(int userId,int offset,int limit)
     {
          return questionDAO.selectLatestQuestions(userId,offset,limit);
     }

     public Question getQuestionById(String id)
     {
         return questionDAO.getQuestionById(id);
     }

     public void updateCommentCount(int questionId,int count)
     {
          questionDAO.updateCommentCount(questionId,count);
     }
}
