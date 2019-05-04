package com.ccy.easyzhihu.async.handler;

import com.ccy.easyzhihu.Service.SearchService;
import com.ccy.easyzhihu.async.EventHandler;
import com.ccy.easyzhihu.async.EventModel;
import com.ccy.easyzhihu.async.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AddQuestionHandler implements EventHandler{

    private static final Logger logger= LoggerFactory.getLogger(AddQuestionHandler.class);
    @Autowired
    SearchService searchService;

    @Override
    public void doHandler(EventModel model) {
        try {
            searchService.indexQuestion(model.getEntityId(),model.getExt("title"),
                    model.getExt("content"));
        }catch (Exception e)
        {
            logger.error("error happened!!!");
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.ADD_QUESTION);
    }
}
