package com.exmaple.demo;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import org.junit.Test;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RedisTest {

    @Test
    public void testString(){

    }

    @Test
    public void testJedis() {
        //创建Jedis链接，需要IP和端口号
        Jedis jedis = new Jedis("192.168.137.129", 6379);
        try {
            //设置键值
            jedis.set("key1", "56464");
            //获取键的值
            String key1 = jedis.get("key1");
            jedis.expire("key1", 100);
            System.out.println(key1);
        } finally {
            //关闭链接
            jedis.close();
        }
    }

    @Test
    public void testJedisPool() {
        //创建Jedis链接池
        JedisPool jedisPool = new JedisPool("192.168.137.129", 6379);
        //获取链接
        Jedis jedis = jedisPool.getResource();
        try {
            //设置键值
            jedis.set("key2", "21321");
            //获取键的值
            String key2 = jedis.get("key2");
            System.out.println(key2);
        } finally {
            //释放资源
            jedis.close();
            jedisPool.close();
        }
    }

    @Test
    public void testJedisCluster() {
        //创建Jedis链接,需要set
        HashSet<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.137.131", 7001));
        nodes.add(new HostAndPort("192.168.137.131", 7002));
        nodes.add(new HostAndPort("192.168.137.131", 7003));
        nodes.add(new HostAndPort("192.168.137.131", 7004));
        nodes.add(new HostAndPort("192.168.137.131", 7005));
        nodes.add(new HostAndPort("192.168.137.131", 7006));
        JedisCluster cluster = new JedisCluster(nodes);
        //设置键值
        cluster.set("key2", "21321");
        //获取键的值
        String key2 = cluster.get("key2");
        System.out.println(key2);
        //关闭链接
        cluster.close();
    }

    @Test
    public void testLettuce() {
        RedisClient client =
                RedisClient.create(RedisURI.create("redis://192.168.137.129:6379"));
        StatefulRedisConnection<String, String> connect = client.connect();

        /**
          * 执行同步命令
         */
//        RedisCommands<String, String> commands = connect.sync();
//        String key1 = commands.get("key1");
//        System.out.println(key1);

        /**
         * 执行异步命令
         */
        RedisAsyncCommands<String, String> asyncCommands = connect.async();
        RedisFuture<String> key2 = asyncCommands.get("key2");

        try {
            String str2 = key2.get();
            System.out.println(str2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        connect.close();
        client.shutdown();
    }

    @Test
    public void testLettuceCluster() {
        List<RedisURI> nodes = new ArrayList<>();
        nodes.add(RedisURI.create("redis://192.168.137.131:7001"));
        nodes.add(RedisURI.create("redis://192.168.137.131:7002"));
        nodes.add(RedisURI.create("redis://192.168.137.131:7003"));
        nodes.add(RedisURI.create("redis://192.168.137.131:7004"));
        nodes.add(RedisURI.create("redis://192.168.137.131:7005"));
        nodes.add(RedisURI.create("redis://192.168.137.131:7006"));
        RedisClusterClient client = RedisClusterClient.create(nodes);
        StatefulRedisClusterConnection<String, String> connect = client.connect();
        /**
         * 同步执行命令
         */
        RedisAdvancedClusterCommands<String, String> commands = connect.sync();
//        commands.set("key1","hello world");
        String str = commands.get("key1");
        System.out.println(str);

        /**
         * 异步执行命令
         */
//        RedisAdvancedClusterAsyncCommands<String,String> asyncCommands = connect.async();
//        RedisFuture<String> future = asyncCommands.get("key1");
//
//        try {
//            String str1 = future.get();
//            System.out.println(str1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        connect.close();
        client.shutdown();
    }
}
