package game.utils;

import redis.clients.jedis.Jedis;

/**
 * @author  gongshengjun
 * @date    2021/3/16 20:12
 */
public class JedisUtils {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        String ping = jedis.ping();
        System.out.println(ping);
    }
}
