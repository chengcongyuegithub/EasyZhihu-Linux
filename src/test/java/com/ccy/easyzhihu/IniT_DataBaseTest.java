package com.ccy.easyzhihu;


import com.ccy.easyzhihu.Dao.QuestionDAO;
import com.ccy.easyzhihu.Dao.UserDAO;
import com.ccy.easyzhihu.model.Question;
import com.ccy.easyzhihu.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IniT_DataBaseTest {

	@Autowired
	UserDAO userDao;
    @Autowired
	QuestionDAO questionDAO;
	@Test
	public void contextLoads() {
        Random random=new Random();
        for(int i=0;i<11;i++)
		{
			User user = new User();
			user.setId(i+1);
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setName(String.format("USER%d", i));
			user.setPassword("");
			user.setSalt("");
			//添加
			//userDao.addUser(user);

		    //修改
			user.setPassword("newpassword");
			userDao.updatePassword(user);
			Question question=new Question();
			question.setCommentCount(i);
			Date date=new Date();
			date.setTime(date.getTime()+i*1000*3600*5);
			question.setCreatedDate(date);
            question.setTitle(String.format("TITLE{%d}",i));
            question.setContent(String.format("huangtinahaochaojishuai %d",i));
			questionDAO.addQuestion(question);
		}
		List<Question> questions = questionDAO.selectLatestQuestions(0, 0, 5);
		System.out.println(questions);

	}

}
