package com.ccy.easyzhihu;

import com.ccy.easyzhihu.async.EventType;

import java.util.Arrays;
import java.util.List;

/**
 * @author chengcongyue
 * @version 1.0
 * @description com.ccy.easyzhihu
 * @date 2019/4/10
 */
public class Test {

    public static void main(String[] args) {

        List<EventType> events = Arrays.asList(EventType.LIKE);
        for(EventType event:events)
        {
            System.out.println(event);
        }
    }
}
