package com.ccy.easyzhihu.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ccy.easyzhihu.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import java.util.List;
import java.util.Set;

@Service
public class JedisAdapter implements InitializingBean{

    public static void print(int index,Object obj)
    {
        System.out.println(String.format("%d,%s",index,obj.toString()));
    }

    public static void main(String[] args) {
        Jedis  jedis = new Jedis ("localhost",6379);
        jedis.select(9);
        jedis.flushDB();
        //test01
        jedis.set("hello","world");
        print(1,jedis.get("hello"));
        jedis.rename("hello","newHello");
        print(1,jedis.get("newHello"));

        //test02
        jedis.set("pv","100");
        jedis.incr("pv");//++
        jedis.incrBy("pv",5);
        print(2,jedis.get("pv"));
        jedis.decrBy("pv",2);
        print(2,jedis.get("pv"));

        //test03
        print(3,jedis.keys("*"));//select all keys

        //test04
        String listName="list";
        jedis.del(listName);//hulue
        for (int i=0;i<10;i++)
        {
            jedis.lpush(listName,"a"+String.valueOf(i));//stack
        }
        print(4,jedis.lrange(listName,0,12));
        print(4,jedis.lrange(listName,0,3));//a9,a8,a7,a6
        print(5,jedis.llen(listName));
        print(6,jedis.lpop(listName));
        print(7,jedis.llen(listName));
        print(8,jedis.lrange(listName,2,6));
        print(9,jedis.lindex(listName,3));
        print(10,jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER,"a4","xx"));
        print(10,jedis.lrange(listName,0,12));
        print(10,jedis.linsert(listName,BinaryClient.LIST_POSITION.BEFORE,"a4","yy"));
        print(10,jedis.lrange(listName,0,12));


        //test05
        String userKey="userxx";
        jedis.hset(userKey,"name","jim");
        jedis.hset(userKey,"age","12");
        jedis.hset(userKey,"phone","18103371041");
        print(12,jedis.hget(userKey,"name"));
        print(12,jedis.hget(userKey,"age"));
        print(12,jedis.hget(userKey,"phone"));
        print(13,jedis.hgetAll(userKey));
        jedis.hdel(userKey,"phone");
        print(14,jedis.hgetAll(userKey));
        print(15,jedis.hexists(userKey,"email"));
        print(16,jedis.hexists(userKey,"age"));
        print(17,jedis.hkeys(userKey));//name age
        print(18,jedis.hvals(userKey));//jim 12
        jedis.hsetnx(userKey,"school","xupt");
        jedis.hset(userKey,"name","zhangyumemg");//cunzaijiutidai
        print(19,jedis.hgetAll(userKey));

        //test06 set
        String likeKey1="commentLike1";
        String likeKey2="commentLike2";
        for(int i=0;i<10;i++)
        {
            jedis.sadd(likeKey1,String.valueOf(i));
            jedis.sadd(likeKey2,String.valueOf(i*i));
        }
        print(20,jedis.smembers(likeKey1));// print all
        print(21,jedis.smembers(likeKey2));
        print(22,jedis.sunion(likeKey1,likeKey2));
        print(23,jedis.sdiff(likeKey1,likeKey2));
        print(24,jedis.sinter(likeKey1,likeKey2));
        print(25,jedis.sismember(likeKey1,"12"));
        print(26,jedis.sismember(likeKey2,"16"));
        jedis.srem(likeKey1,"5");
        print(27,jedis.smembers(likeKey1));
        jedis.smove(likeKey2,likeKey1,"25");
        print(28,jedis.smembers(likeKey1));
        print(29,jedis.smembers(likeKey2));
        print(29,jedis.scard(likeKey1));

        String rankKey="rankKey";
        jedis.zadd(rankKey,15,"jim");
        jedis.zadd(rankKey,60,"Ben");
        jedis.zadd(rankKey,90,"Lee");
        jedis.zadd(rankKey,75,"lucy");
        jedis.zadd(rankKey,80,"Mei");
        print(30,jedis.zcard(rankKey));//length
        print(31,jedis.zcount(rankKey,61,100));//score
        print(31,jedis.zscore(rankKey,"lucy"));
        jedis.zincrby(rankKey,2,"Luc");
        print(32,jedis.zscore(rankKey,"Luc"));
        print(35,jedis.zrange(rankKey,0,100));
        print(36,jedis.zrange(rankKey,0,10));
        print(36,jedis.zrange(rankKey,1,3));
        print(36,jedis.zrevrange(rankKey,1,3));
        for(Tuple tuple:jedis.zrangeByScoreWithScores(rankKey,"60","100"))
        {
            print(37,tuple.getElement()+":"+String.valueOf(tuple.getScore()));
        }
        print(38,jedis.zrank(rankKey,"Ben"));
        print(39,jedis.zrevrank(rankKey,"Ben"));

        String setKey="zset";
        jedis.zadd(setKey,1,"a");
        jedis.zadd(setKey,1,"b");
        jedis.zadd(setKey,1,"c");
        jedis.zadd(setKey,1,"d");
        jedis.zadd(setKey,1,"e");

        print(40,jedis.zlexcount(setKey,"-","+"));
        print(41,jedis.zlexcount(setKey,"(b","[d"));
        print(42,jedis.zlexcount(setKey,"[b","[d"));
        jedis.zrem(setKey,"b");
        print(43,jedis.zrange(setKey,0,10));
        jedis.zremrangeByLex(setKey,"(c","+");
        print(44,jedis.zrange(setKey,0,10));


        User user=new User();
        user.setName("xx");
        user.setPassword("ppp");
        user.setHeadUrl("...");
        user.setSalt("salt");
        user.setId(1);
        print(46, JSONObject.toJSONString(user));
        jedis.set("user1",JSONObject.toJSONString(user));

        String value=jedis.get("user1");
        User user2= JSON.parseObject(value,User.class);
        print(47,user2);
    }

    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool pool;
    @Override
    public void afterPropertiesSet() throws Exception {
        pool=new JedisPool("redis://localhost:6379/10");
        System.out.println("success!!!!");
    }

    public long sadd(String key,String value)
    {
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e)
        {
            logger.error("error happened "+e.getMessage());
        }finally {
            if(jedis!=null)
            {
                jedis.close();
            }
        }

        return 0;
    }

    public long srem(String key,String value)
    {
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e)
        {
            logger.error("error happened "+e.getMessage());
        }finally {
            if(jedis!=null)
            {
                jedis.close();
            }
        }

        return 0;
    }

    public long scard(String key)
    {
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.scard(key);
        }catch (Exception e)
        {
            logger.error("error happened "+e.getMessage());
        }finally {
            if(jedis!=null)
            {
                jedis.close();
            }
        }
        return 0;
    }
    public boolean sismember(String key,String value)
    {
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e)
        {
            logger.error("error happened"+e.getMessage());
        }finally {
            if(jedis!=null)
            {
                jedis.close();
            }
        }
        return false;
    }
    public List<String> brpop(int timeOut,String key)
    {
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.brpop(timeOut,key);
        }catch (Exception e)
        {
            logger.error("error happened"+e.getMessage());
        }finally {
            if(jedis!=null)
            {
                jedis.close();
            }
        }
        return null;
    }

    public long lpush(String key,String value)
    {
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.lpush(key,value);
        }catch (Exception e)
        {
            logger.error("error happened"+e.getMessage());
        }finally {
            if(jedis!=null)
            {
                jedis.close();
            }
        }
        return 0;
    }

    public Jedis getJedis()
    {
        return pool.getResource();
    }
    //start transaction
    public Transaction multi(Jedis jedis)
    {
        try {
            return jedis.multi();
        }catch (Exception e)
        {
            logger.error("error happens"+e.getMessage());
        }
        return null;
    }
    //run and end transaction
    public List<Object> exec(Transaction tx,Jedis jedis)
    {
        try {
            return tx.exec();
        }catch (Exception e)
        {
            logger.error("error happens"+e.getMessage());
            tx.discard();
        }finally {
            if(tx!=null)
            {
                try {

                }catch (Exception e)
                {
                    logger.error("error happens"+e.getMessage());
                }
            }
            if(jedis!=null)
            {
                jedis.close();
            }
        }
        return null;
    }

    public long zadd(String key,double score,String value)
    {
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.zadd(key,score,value);
        }catch (Exception e)
        {
            logger.error("error happened "+e.getMessage());
        }finally {
            if(jedis!=null)
            {
                jedis.close();
            }
        }
        return 0;
    }
    public long zrem(String key,String value)
    {
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.zrem(key,value);
        }catch (Exception e)
        {
            logger.error("error happened"+e.getMessage());
        }finally {
            if(jedis!=null)
            {
                jedis.close();
            }
        }
        return 0;
    }
    public Set<String> zrange(String key,int start,int end)
    {
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.zrange(key,start,end);
        }catch (Exception e)
        {
            logger.error("error happened"+e.getMessage());
        }finally {
            if(jedis!=null)
            {
                jedis.close();
            }
        }
        return null;
    }
    public Set<String> zrevrange(String key,int start,int end)
    {
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.zrevrange(key,start,end);
        }catch (Exception e)
        {
            logger.error("error happened"+e.getMessage());
        }finally {
            if(jedis!=null)
            {
                jedis.close();
            }
        }
        return null;
    }
    public long zcard(String key)
    {
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.zcard(key);
        }catch (Exception e)
        {
            logger.error("error happened"+e.getMessage());
        }finally {
            if(jedis!=null)
            {
                jedis.close();
            }
        }
        return 0;
    }
    public Double zscore(String key,String member)
    {
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return jedis.zscore(key, member);
        }catch (Exception e)
        {
            logger.error("error happened"+e.getMessage());
        }finally {
            if(jedis!=null)
            {
                jedis.close();
            }
        }
        return null;
    }
}
