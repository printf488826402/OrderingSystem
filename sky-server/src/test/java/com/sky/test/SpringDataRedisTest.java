package com.sky.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

//@SpringBootTest//不测试时注释此注解减少性能开销
public class SpringDataRedisTest {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void testRedisTemplate(){
        //测试redisTemplate是否为空
        System.out.println(redisTemplate);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        HashOperations hashOperations = redisTemplate.opsForHash();
        //redisTemplate.ops回车.var回车
        ListOperations listOperations = redisTemplate.opsForList();
        SetOperations setOperations = redisTemplate.opsForSet();
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
    }

    /**
     *操作string字符串类型的数据
     */
    @Test
    public void testString(){
        //set get setex setnx//插入 获取 插入并设置过期时间 插入不存在数据
        redisTemplate.opsForValue().set("city","北京");
        String city = (String) redisTemplate.opsForValue().get("city");
        System.out.println(city);
        //时效3min的验证码
        redisTemplate.opsForValue().set("code","1234",3, TimeUnit.MINUTES);
        //redis中String数据类型与java中String不完全一至，所以需要通过Object数据类型转换
        redisTemplate.opsForValue().setIfAbsent("lock","1");
        redisTemplate.opsForValue().setIfAbsent("lock","2");
    }
    /**
     * 操作hash类型数据
     */
    public void testHash(){
        //hset hget hdel hkeys hvals
        HashOperations hashOperations = redisTemplate.opsForHash();

        hashOperations.put("100","name","tom");
        hashOperations.put("100","age","20");

        String name = (String) hashOperations.get("100","name");
        System.out.println(name);


        Set keys = hashOperations.keys("100");
        System.out.println(keys);

        List values = hashOperations.values("100");
        System.out.println(values);

        hashOperations.delete("100","age");
    }
    /**
     * 操作列表类型的数据
     */
    @Test
    public void testList(){
        //Lpush Lrange rpush llen
        ListOperations listOperations = redisTemplate.opsForList();

        listOperations.leftPushAll("mylist","1","2","3");
        listOperations.leftPush("mylist","d");

        List mylist = listOperations.range("mylist",0,-1);
        System.out.println(mylist);

        listOperations.rightPop("mylist");

        long size = listOperations.size("mylist");
        System.out.println(size);
    }
    /**
     * 操作集合类型数据
     */
    @Test
    public void testSet(){
        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.add("mySet","1","2","3");
        setOperations.add("mySet","4");
        Set mySet = setOperations.members("mySet");
        System.out.println(mySet);
        Long size= setOperations.size("mySet");
        System.out.println(size);
        //求集合交集
        Set intersect = setOperations.intersect("mySet", "mySet1");
        System.out.println(intersect);
        //求集合并集
        Set union = setOperations.union("mySet", "mySet1");
        System.out.println(union);
        //移除集合元素
        setOperations.remove("mySet","1");
    }
    /**
     * 操作有序集合类型数据
     */
    @Test
    public void testZSet(){

        ZSetOperations zSetOperations = redisTemplate.opsForZSet();

        zSetOperations.add("myzset", "a", 10);
        zSetOperations.add("myzset", "b", 12);
        zSetOperations.add("myzset", "c", 9);
        //排序
        Set myzset = zSetOperations.range("myzset", 0, -1);
        System.out.println(myzset);
        //给c加10分
        zSetOperations.incrementScore("myzset","c" , 10);

        zSetOperations.remove("myzset","a");
    }
    @Test
    public void testCommon(){
        //keys exists type del
        Set keys = redisTemplate.keys("*");
        System.out.println(keys);

        Boolean name = redisTemplate.hasKey("name");
        Boolean set1 = redisTemplate.hasKey("set1");

        for (Object key : keys) {
            DataType type = redisTemplate.type(key);
            System.out.println(type.name());
        }
        redisTemplate.delete("mylist");
    }
}















