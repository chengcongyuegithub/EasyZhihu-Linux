package com.ccy.easyzhihu.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chengcongyue
 * @version 1.0
 * @description com.ccy.easyzhihu.model
 * @date 2019/3/28
 */
public class VeiwObject {
    private Map<String,Object> objs=new HashMap<String,Object>();

    public void set(String key,Object value)
    {
        objs.put(key,value);
    }
    public Object get(String key)
    {
        return objs.get(key);
    }
}
