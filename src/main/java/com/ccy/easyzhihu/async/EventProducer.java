package com.ccy.easyzhihu.async;

import com.alibaba.fastjson.JSONObject;
import com.ccy.easyzhihu.util.JedisAdapter;
import com.ccy.easyzhihu.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {

   @Autowired
   JedisAdapter jedisAdapter;

   public boolean fireEvent(EventModel eventModel)
   {
       try {

          String json= JSONObject.toJSONString(eventModel);
          String key= RedisKeyUtil.getEventQueueKey();
          jedisAdapter.lpush(key,json);
          return true;
       }
       catch (Exception e)
       {
          return false;
       }
   }
}
