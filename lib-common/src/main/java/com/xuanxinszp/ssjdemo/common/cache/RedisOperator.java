package com.xuanxinszp.ssjdemo.common.cache;

import com.xuanxinszp.ssjdemo.common.util.JsonUtil;
import com.xuanxinszp.ssjdemo.common.util.StringUtil;
import com.xuanxinszp.ssjdemo.common.util.action.Action;
import com.xuanxinszp.ssjdemo.common.util.action.Action1;
import com.xuanxinszp.ssjdemo.common.util.func.Func;
import com.xuanxinszp.ssjdemo.common.util.func.Func1;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import javax.annotation.Resource;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;


/**
 * redis操作
 */
@Component
public class RedisOperator {

    private static final Logger logger = LoggerFactory.getLogger(RedisOperator.class);

    @Resource
    protected JedisConnectionFactory connectionFactory;

    /**
     * 执行包装的RedisConnection执行
     *
     * @param action RedisConnection
     */
    private void connectionRun(Action<RedisConnection> action) {
        RedisConnection connection = connectionFactory.getConnection();
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
//        redisTemplate.opsForHash().
        action.call(connection);
        connection.close();
    }

    /**
     * 使用源生的Jedis执行
     *
     * @param action Jedis
     */
    private void jedisRun(Action<Jedis> action) {
        RedisConnection connection = connectionFactory.getConnection();
        Jedis jedis = (Jedis) connection.getNativeConnection();
        action.call(jedis);
        connection.close();
    }

    /**
     * 设置一个key的过期时间（单位：秒）
     *
     * @param key     key值
     * @param seconds 多少秒后过期
     * @return 1：设置了过期时间  0：没有设置过期时间/不能设置过期时间
     */
    public Long expire(String key, int seconds) {
        if (key == null || key.equals("")) {
            return 0L;
        }
        return runJedisAction(w -> w.expire(key, seconds));
    }

    /**
     * 设置一个key的过期时间（单位：秒）
     *
     * @param key     key值
     * @param seconds 多少秒后过期
     * @return 1：设置了过期时间  0：没有设置过期时间/不能设置过期时间
     */
    public Long expire(byte[] key, int seconds) {
        if (key == null) {
            return 0L;
        }
        return runJedisAction(w -> w.expire(key, seconds));
    }

    /**
     * 设置一个key的过期时间（单位：秒）
     *
     * @return 1：设置了过期时间  0：没有设置过期时间/不能设置过期时间
     */
    public void subscribe(Action1<String, String> callBack, String... channels) {
        JedisPubSub sub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                super.onMessage(channel, message);
                callBack.call(channel, message);
            }
        };
        runJedisAction(w -> {
            w.subscribe(sub, channels);
            return "";
        });
    }

    public Long publish(String channel, String msg) {

        return runJedisAction(w -> {
            return w.publish(channel, msg);
        });
    }

    /**
     * 删除key的过期时间,让它成为永久数据
     *
     * @param key key值
     * @return 1:删除了过期时间,0:key不存在或过期时间不存在
     */
    public Long persist(byte[] key) {
        if (key == null) {
            return 0L;
        }
        return runJedisAction(w -> w.persist(key));
    }

    /**
     * 删除key的过期时间,让它成为永久数据
     *
     * @param key key值
     * @return 1:删除了过期时间,0:key不存在或过期时间不存在
     */
    public Long persist(String key) {
        if (key == null) {
            return 0L;
        }
        return runJedisAction(w -> w.persist(key));
    }

    /**
     * 设置一个key在某个时间点过期
     *
     * @param key           key值
     * @param unixTimestamp unix时间戳，从1970-01-01 00:00:00开始到现在的秒数
     * @return 1：设置了过期时间  0：没有设置过期时间/不能设置过期时间
     */
    public Long expireAt(String key, int unixTimestamp) {
        return ifNotNullOrEmpty(key, 0L, () -> runJedisAction(w -> w.expireAt(key, unixTimestamp)));
    }

    private static <T> T ifNotNullOrEmpty(String str, T defaultValue, Func<T> action) {
        if (StringUtil.isNil(str)) return defaultValue;
        return action.call();
    }

    /**
     * 截断一个List
     *
     * @param key   列表key
     * @param start 开始位置 从0开始
     * @param end   结束位置
     * @return 状态码
     */
    public String trimList(String key, long start, long end) {
        if (key == null || key.equals("")) {
            return "-";
        }
        return runJedisAction(w -> w.ltrim(key, start, end));
    }

    /**
     * 检查Set长度
     *
     * @param key
     * @return
     */
    public long scard(String key) {
        if (key == null) {
            return 0;
        }
        return runJedisAction(w -> w.scard(key));
    }

    /**
     * 尝试添加锁
     *
     * @param key
     * @return
     */
    public boolean tryLockOneTime(String key, String value, long timeoutSecond) {
        if (key == null) {
            return false;
        }
        return "OK".equals(runJedisAction(w -> w.set(key, value, "nx", "ex", timeoutSecond)));
    }

    /**
     * 尝试获取锁
     *
     * @param key        锁名称
     * @param waitSecond 如果获取失败,尝试的总时长(秒)
     * @param lockSecond 如果获取成功,设置加锁的时间(秒)
     * @return 返回是否成功
     */
    public boolean tryGetRedisLock(String key, String value, int waitSecond, int lockSecond) {
        long start = new Date().getTime();
        long timeOut = waitSecond * 1000 + start;
        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (tryLockOneTime(key, value, lockSecond)) {
                return true;
            }
            if (new Date().getTime() >= timeOut) {
                return false;
            }
        }
    }

    /**
     * 释放锁
     *
     * @param key
     * @return
     */
    public boolean releaseLock(String key, String value) {
        if (key == null) {
            return false;
        }
        String currentValue = runJedisAction(w -> w.get(key));
        if (value.equals(currentValue)) {
            runJedisAction(w -> w.del(key));
            return true;
        }
        return false;
    }

    /**
     * 添加到Set中（同时设置过期时间）
     *
     * @param key     key值
     * @param seconds 过期时间 单位s
     * @param value
     * @return
     */
    public boolean sadd(String key, int seconds, String... value) {
        Long result = sadd(key, value);
        if (result != null && result > 0) {
            long i = expire(key, seconds);
            return i == 1;
        }
        return false;
    }

    /**
     * 添加到Set中
     *
     * @param key
     * @param value
     * @return
     */
    public Long sadd(String key, String... value) {
        if (key == null || value == null) {
            return 0L;
        }
        return runJedisAction(w -> w.sadd(key, value));
    }

    /**
     * @param key
     * @param value
     * @return 判断值是否包含在set中
     */
    public boolean sismember(String key, String value) {
        if (key == null || value == null) {
            return false;
        }
        return runJedisAction(w -> w.sismember(key, value));
    }

    /**
     * 获取Set
     *
     * @param key
     * @return
     */
    public Set<String> smembers(String key) {
        return runJedisAction(w -> w.smembers(key));
    }

    /**
     * 获取多个键
     *
     * @param key 键
     * @return null
     */
    public List<String> mget(String... key) {
        return runJedisAction(w -> w.mget(key));
    }

    /**
     * 获取hash中的多个键
     *
     * @param key 键
     * @return 键不存在, 返回null
     */
    public List<String> hmget(String key, String... fields) {
        return runJedisAction(w -> w.hmget(key, fields));
    }

    /**
     * 获取hash中的所有值
     *
     * @param key 键
     * @return 键不存在, 返回null
     */
    public Map<String, String> hgetAll(String key) {
        return runJedisAction(w -> w.hgetAll(key));
    }

    /**
     * 保存hash
     *
     * @param key 键
     * @return 键不存在, 返回null
     */
    public String hmset(String key, Map<String, String> fields) {
        return runJedisAction(w -> w.hmset(key, fields));
    }

    /**
     * 获取在存储在键串值偏移的比特值
     *
     * @param key
     * @return 1返回true, 0或不存在返回false
     */
    public Boolean getbit(String key, int offset) {
        return runJedisAction(w -> w.getbit(key, offset));
    }

    /**
     * 从set中删除value
     *
     * @param key
     * @return
     */
    public long srem(String key, String... value) {
        return runJedisAction(w -> w.srem(key, value));
    }

    /**
     * 从set中返回一个或多个值(不删除Set的内容)
     *
     * @param key
     * @return
     */
    public List<String> srandmember(String key, int count) {
        return runJedisAction(w -> w.srandmember(key, count));
    }

    /**
     * 从set中返回一个或多个值(不删除Set的内容)
     *
     * @param key
     * @return
     */
    public String srandmember(String key) {
        return runJedisAction(w -> w.srandmember(key));
    }

    /**
     * 随机从Set中返回count个元素
     *
     * @param key
     * @return
     */
    public Set<String> spop(String key, long count) {
        return runJedisAction(w -> w.spop(key, count));
    }

    /**
     * 管道
     *
     * @param action
     * @return
     */
    public void pipelineRun(Action<Pipeline> action) {
        runJedisAction(w -> {
            Pipeline pipelined = w.pipelined();
            action.call(pipelined);
            pipelined.sync();
            return null;
//            return pipelined();
        });
    }

    /**
     * 从Set中返回一个值
     *
     * @param key
     * @return
     */
    public String spop(String key) {
        Set<String> strings = spop(key, 1);
        if (strings != null && strings.size() > 0) {
            Iterator<String> iterator = strings.iterator();
            if (iterator.hasNext()) {
                return iterator.next();
            }
        }
        return null;
    }

    /**
     * 截取List
     * 返回列表中指定区间内的元素，区间以偏移量 START 和 END 指定。
     * 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     *
     * @param key   key
     * @param start 起始位置
     * @param end   结束位置
     * @return List<String>
     */
    public List<String> lrange(String key, long start, long end) {
        if (key == null || key.equals("")) {
            return null;
        }
        return runJedisAction(w -> w.lrange(key, start, end));

    }

    public <T> List<T> lrange(Class<T> itemClass, String key, long start, long end) {
        if (key == null || key.equals("")) {
            return null;
        }
        List<String> list = runJedisAction(w -> w.lrange(key, start, end));
        return list.stream().map(s -> JsonUtil.jsonToBean(s, itemClass)).collect(toList());

    }

    /**
     * 设置HashSet对象
     *
     * @param key   键
     * @param field 字段名
     * @param value Json String or String value
     * @return
     */
    public long hSet(String key, String field, String value) {
        if (value == null) return 0;
        return runJedisAction(w -> w.hset(key, field, value));
    }

    /**
     * 删除HashSet的键
     *
     * @param key    键
     * @param fields 字段名
     * @return
     */
    public Long hdel(String key, String... fields) {
        return runJedisAction(w -> w.hdel(key, fields));
    }

    /**
     * 设置HashSet对象
     *
     * @param key   键值
     * @param field 字段名
     * @param value Json String or String value
     * @return
     */
    public long hSetNX(String key, String field, String value) {
        if (value == null) return 0;
        return runJedisAction(w -> w.hsetnx(key, field, value));
    }

    /**
     * 获得HashSet对象
     *
     * @param key   键值
     * @param field 字段名
     * @return Json String or String value
     */
    public String hGet(String key, String field) {
        return runJedisAction(w -> w.hget(key, field));
    }

    /**
     * 删除HashSet对象
     *
     * @param key    键值
     * @param fields 字段名
     * @return 删除的记录数
     */
    public long delHSet(String key, String... fields) {
        return runJedisAction(w -> w.hdel(key, fields));
    }

    /**
     * 判断字段是否存在
     *
     * @param key   键值
     * @param field 字段名
     * @return
     */
    public boolean hExistsSet(String key, String field) {
        return runJedisAction(w -> w.hexists(key, field));
    }

    /**
     * 全局扫描hset
     *
     * @param match field匹配模式
     * @return
     */
    public List<Map.Entry<String, String>> scanHSet(String key, String match) {
        return runJedisAction(jedis -> {
            List<Map.Entry<String, String>> list = new ArrayList<>();
            int cursor = 0;
            ScanParams scanParams = new ScanParams();
            scanParams.match(match);
            ScanResult<Map.Entry<String, String>> scanResult;
            do {
                scanResult = jedis.hscan(key, String.valueOf(cursor), scanParams);
                list.addAll(scanResult.getResult());
                cursor = Integer.parseInt(scanResult.getStringCursor());
            } while (cursor > 0);
            return list;
        });
    }

    /**
     * 返回 domain 指定的哈希集中所有字段的value值
     *
     * @param key
     * @return
     */

    public List<String> hvals(String key) {
        return runJedisAction(w -> w.hvals(key));
    }

    /**
     * 返回 domain 指定的哈希集中所有字段的key值
     *
     * @param key
     * @return
     */

    public Set<String> hkeys(String key) {
        return runJedisAction(w -> w.hkeys(key));
    }

    /**
     * 返回 domain 指定的哈希key值总数
     *
     * @param key
     * @return
     */
    public long hLen(String key) {

        return runJedisAction(w -> w.hlen(key));
    }


    /**
     * 获得元素排序打分
     *
     * @param key
     * @return
     */
    public Double zCore(String key, String member) {
        return runJedisAction(w -> w.zscore(key, member));
    }

    /**
     * 添加key value 并且设置存活时间
     *
     * @param key
     * @param value
     * @param second
     */
    public String set(String key, String value, int second) {
        return runJedisAction(w -> w.setex(key, second, value));
    }


    public String set(String key, String value) {
        return runJedisAction(w -> w.set(key, value));
    }

    /**
     * 替换指定位置的字符串
     *
     * @param key    key
     * @param offset 起始位置
     * @param value  字符串
     * @return
     */
    public Long setRange(String key, Long offset, String value) {
        return runJedisAction(w -> w.setrange(key, offset, value));
    }

    /**
     * 设置多个键和多个值(K,V不是一对一会异常)
     *
     * @param keyValues key,value,key,value
     * @return OK
     */
    public String multiSet(String... keyValues) {
        return runJedisAction(w -> w.mset(keyValues));
    }

    /**
     * 设置多个键和多个值(K,V不是一对一会异常)
     * 只有在键不存在时才设置
     *
     * @param keyValues key,value,key,value
     * @return 设置的数量
     */
    public Long multiSetNotExist(String... keyValues) {
        return runJedisAction(w -> w.msetnx(keyValues));
    }

    /**
     * 返回字符串的长度
     *
     * @param key
     * @return
     */
    public Long strLen(String key) {
        return runJedisAction(w -> w.strlen(key));
    }

    public Long setIfNotExist(String key, String value) {
        return runJedisAction(c -> c.setnx(key, value));
    }

    /**
     * 返回过期时间(秒)
     *
     * @param key key
     * @return ttl 永久数据返回 -1, 找不到值返回 -2 ,有设定过期时间返回剩下的时限
     */
    public Long ttl(String key) {
        return runJedisAction(w -> w.ttl(key));
    }

    /**
     * 返回过期时间(秒)
     *
     * @param key key
     * @return ttl 永久数据返回 -1 ,找不到值返回 -2 ,有设定过期时间返回剩下的时限
     */
    public Long ttl(byte[] key) {
        return runJedisAction(w -> w.ttl(key));
    }

    /**
     * 在已经存在的键集合中,随机返回一个
     *
     * @return
     */
    public String getRandomKey() {
        return runJedisAction(Jedis::randomKey);
    }

    public String get(String key, String defaultValue) {
        String result = runJedisAction(w -> w.get(key));
        if(StringUtil.isNotNil(result)) {
            return result;
        }
        return defaultValue;
    }

    /**
     * 自增 1
     *
     * @param key 如果键不存在,则指定为1
     * @return 增加后的结果
     */
    public Long incr(String key) {
        return runJedisAction(w -> w.incr(key));
    }

    /**
     * 增加指定的值
     *
     * @param key 如果键不存在,则指定为addValue
     * @return 增加后的结果
     */
    public Long incrBy(String key, long addValue) {
        return runJedisAction(w -> w.incrBy(key, addValue));
    }

    /**
     * 增加指定的值
     *
     * @param key 如果键不存在,则指定为addValue
     * @return 增加后的结果
     */
    public Long hIncrBy(String key, String field, long addValue) {
        if (addValue == 0) return 0L;
        return runJedisAction(w -> w.hincrBy(key, field, addValue));
    }

    /**
     * 增加指定的值
     *
     * @param key 如果键不存在,则指定为addValue
     * @return 增加后的结果
     */
    public Long hIncr(String key, String field) {
        return runJedisAction(w -> w.hincrBy(key, field, 1));
    }


    /**
     * 增加指定的值
     *
     * @param key 如果键不存在,则指定为addValue
     * @return 增加后的结果
     */
    public Double incrByFloat(String key, double addValue) {
        return runJedisAction(w -> w.incrByFloat(key, addValue));
    }

    /**
     * 增加指定的值
     *
     * @param key 如果键不存在,则指定为addValue
     * @return 增加后的结果
     */
    public Double hIncrByFloat(String key, String field, double addValue) {
        return runJedisAction(w -> w.hincrByFloat(key, field, addValue));
    }


    public <T> T runJedisAction(Func1<Jedis, T> getkey) {
        try {
            List<T> expire = new ArrayList<>();
            jedisRun(w -> expire.add(getkey.call(w)));
            T t = expire.get(0);
            expire.clear();
            return t;
        } catch (Exception e) {
            logger.error("缓存异常", e);
            return null;
        }
    }

    /**
     * 自减1
     *
     * @param key
     * @return
     */
    public Long decr(String key) {
        return runJedisAction(w -> w.decr(key));
    }

    /**
     * 自减
     *
     * @param key
     * @return
     */
    public Long decrBy(String key, long value) {
        return runJedisAction(w -> w.decrBy(key, value));
    }

    public String rename(String key, String newKey) {
        return runJedisAction(w -> w.rename(key, newKey));
    }

    public Long renameNX(String key, String newKey) {
        return runJedisAction(w -> w.renamenx(key, newKey));
    }

    /**
     * 追加字符串到指定的key后面,如果key不存在,则创建一个.
     *
     * @param key         key
     * @param appendValue
     * @return 追加后的value(byte不是字符串的长度)的长度
     */
    public Long appendStr(String key, String appendValue) {
        return runJedisAction(w -> w.append(key, appendValue));
    }

    /**
     * 返回键的类型
     *
     * @param key
     * @return
     */
    public String type(String key) {
        return runJedisAction(w -> w.type(key));
    }

    /**
     * 返回字符串的部分,从0开始
     * startOffset(....]endOffset
     *
     * @param key
     * @return 不存在kek返回""
     */
    public String getrange(String key, long startOffset, long endOffset) {
        return runJedisAction(w -> w.getrange(key, startOffset, endOffset));
    }


    /**
     * 通过key删除（字节）
     *
     * @param key
     */
    public Long del(byte[] key) {
        return runJedisAction(w -> w.del(key));
    }

    /**
     * 通过key删除
     *
     * @param key
     */
    public Long del(String key) {
        return runJedisAction(w -> w.del(key));
    }

    /**
     * 添加key value 并且设置存活时间(byte)
     *
     * @param key
     * @param value
     * @param timeoutSecond
     */
    public String set(byte[] key, byte[] value, int timeoutSecond) {
        return runJedisAction(w -> w.set(key, value, "nx".getBytes(), "ex".getBytes(), timeoutSecond));
    }


    /**
     * 添加key value (字节)(序列化)
     *
     * @param key
     * @param value
     */
    public String set(byte[] key, byte[] value) {
        return runJedisAction(w -> w.set(key, value));
    }

    /**
     * 获取redis value (String)
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return runJedisAction(w -> w.get(key));
    }

    /**
     * 获取redis value (byte [] )(反序列化)
     *
     * @param key
     * @return
     */
    public byte[] get(byte[] key) {
        return runJedisAction(w -> w.get(key));
    }

    /**
     * 检查key是否已经存在
     *
     * @param key
     * @return
     */
    public Boolean exists(String key) {
        return runJedisAction(w -> w.exists(key));
    }

    /**
     * 检查key是否已经存在
     *
     * @param key
     * @return
     */
    public Boolean exists(byte[] key) {
        return runJedisAction(w -> w.exists(key));
    }

    /**
     * 检查key是否已经存在
     *
     * @param key
     * @return
     */
    public Long exists(String... key) {
        return runJedisAction(w -> w.exists(key));
    }

    /**
     * 检查key是否已经存在
     *
     * @param key
     * @return
     */
    public Long exists(byte[]... key) {
        return runJedisAction(w -> w.exists(key));
    }

    /**
     * 返回存储在指定键的值的序列化版本
     *
     * @param key
     * @return
     */
    public byte[] dump(String key) {
        return runJedisAction(w -> w.dump(key));
    }

    /**
     * 清空redis 所有数据
     *
     * @return
     */
    public String flushDB() {
        return runJedisAction(BinaryJedis::flushDB);
    }

    /**
     * 查看redis里有多少条数据
     */
    public Long dbSize() {
        return runJedisAction(BinaryJedis::dbSize);
    }


    /**
     * 检查是否连接成功
     *
     * @return 返回 pong
     */
    public String ping() {
        return runJedisAction(BinaryJedis::ping);
    }


    /**
     * 获取原始值,设置新值
     *
     * @param key
     * @param value
     * @return
     */
    public String getAndSet(String key, String value) {
        return runJedisAction(c -> StringUtil.getStr(c.getSet(key.getBytes(), value.getBytes())));
    }

    /**
     * @return 返回所有key
     */
    public List<String> getKeys() {

        return getKeys("*");
    }

    /**
     * 搜索key
     *
     * @param pattern 正则
     * @return
     */
    public List<String> getKeys(String pattern) {
        return runJedisAction(w ->
                new ArrayList<>(w.keys(pattern)));
    }

    public String getStr(String key) {
        return runJedisAction(w -> StringUtil.getStr(w.get(key.getBytes())));
    }


    /**
     * 取出并获取列表中的第一个元素，或阻塞，直到有可用
     *
     * @param timeout
     * @param key
     * @return
     */
    //region list
    public List<String> blpop(int timeout, String key) {
        return runJedisAction(w -> w.blpop(timeout, key));
    }

    /**
     * 取出并获取列表中的最后一个元素，或阻塞，直到有可用
     *
     * @param timeout
     * @param key
     * @return
     */
    public List<String> brpop(int timeout, String key) {
        return runJedisAction(w -> w.brpop(timeout, key));
    }

    /**
     * 从列表中弹出一个值，它推到另一个列表并返回它;或阻塞，直到有可用
     *
     * @param timeout     超时时间
     * @param source      原始队列
     * @param destination 目标队列
     * @return
     */
    public String lBrpoplpush(String source, String destination, int timeout) {
        return runJedisAction(w -> w.brpoplpush(source, destination, timeout));
    }

    /**
     * 删除最后一个元素的列表，将其附加到另一个列表并返回它
     *
     * @param source      原始队列
     * @param destination 目标队列
     * @return
     */
    public String lRpoplpush(String source, String destination) {
        return runJedisAction(w -> w.rpoplpush(source, destination));
    }

    /**
     * 从列表中弹出一个值，它推到另一个列表并返回它;或阻塞，直到有可用
     *
     * @param key key
     * @return
     */
    public Long llen(String key) {
        return runJedisAction(w -> w.llen(key));
    }

    /**
     * 从一个列表其索引获取对应的元素
     *
     * @param key key
     * @return
     */
    public String lIndex(String key, long index) {
        return runJedisAction(w -> w.lindex(key, index));
    }

    /**
     * 从一个列表其索引获取对应的元素
     * 在target之前插入newValue
     *
     * @param key
     * @param target
     * @param newValue
     * @return
     */
    public Long lIndex(String key, String target, String newValue) {
        return runJedisAction(w -> w.linsert(key, BinaryClient.LIST_POSITION.BEFORE, target, newValue));
    }

    /**
     * 检查List长度
     *
     * @param key
     * @return
     */
    public long lLen(String key) {
        if (key == null) {
            return 0;
        }
        return runJedisAction(w -> w.llen(key));

    }


    /**
     * 添加到List中（同时设置过期时间）
     *
     * @param key     key值
     * @param seconds 过期时间 单位s
     * @param value
     * @return
     */
    public Long addList(String key, int seconds, String... value) {
        Long result = addList(key, value);
        if (result != null && result > 0) {
            return expire(key, seconds);
        }
        return null;
    }

    /**
     * 添加到List前面
     *
     * @param key
     * @param value
     * @return
     */
    public Long lPush(String key, String... value) {
        if (key == null || value == null) {
            return 0L;
        }
        return runJedisAction(w -> w.lpush(key, value));
    }

    /**
     * 添加到List前面
     *
     * @param key
     * @param value
     * @return
     */
    public Long lPush(String key, Object value) {
        if (key == null || value == null) {
            return 0L;
        }
        return runJedisAction(w -> w.lpush(key, JsonUtil.beanToJson(value)));
    }

    /**
     * 添加到List后面
     *
     * @param key
     * @param value
     * @return
     */
    public Long rPush(String key, Object value) {
        if (key == null || value == null) {
            return 0L;
        }
        return runJedisAction(w -> w.rpush(key, JsonUtil.beanToJson(value)));
    }

    /**
     * list从左边添加多个元素
     *
     * @param key
     * @param values
     * @return
     */
    public Long lPush(final String key, final List<Object> values) {
        if (key == null || CollectionUtils.isEmpty(values)) {
            return 0L;
        }
        String[] valueArray = new String[values.size()];
        int i = 0;
        for (Object value : values) {
            valueArray[i] = JsonUtil.beanToJson(value);
            i++;
        }
        return runJedisAction(w -> w.lpush(key, valueArray));
    }


    /**
     * 添加到List后面
     *
     * @param key
     * @param value
     * @return
     */
    public Long rpush(String key, String... value) {
        if (key == null || value == null) {
            return 0L;
        }
        return runJedisAction(w -> w.rpush(key, value));
    }

    /**
     * 添加到List,如果key存在
     *
     * @param key
     * @param value
     * @return
     */
    public Long addListIfExist(String key, String... value) {
        if (key == null || value == null) {
            return 0L;
        }
        return runJedisAction(w -> w.lpushx(key, value));
    }

    /**
     * 如果列表存在,在后面添加值
     *
     * @param key
     * @param value
     * @return
     */
    public Long lRPusHX(String key, String... value) {
        if (key == null || value == null) {
            return 0L;
        }
        return runJedisAction(w -> w.rpushx(key, value));
    }

    /**
     * 如果列表存在,在前面添加值
     *
     * @param key
     * @param value
     * @return
     */
    public Long lLPusHX(String key, String... value) {
        if (key == null || value == null) {
            return 0L;
        }
        return runJedisAction(w -> w.lpushx(key, value));
    }

    /**
     * 添加到List(只新增)
     *
     * @param key
     * @param list
     * @return
     */
    public Long addList(String key, String... list) {
        if (key == null || list == null || list.length == 0) {
            return 0L;
        }
        return runJedisAction(w -> w.lpush(key, list));
    }

    /**
     * 获取List
     *
     * @param key
     * @return
     */
    public List<String> getList(String key) {
        return runJedisAction(w -> w.lrange(key, 0, -1));
    }

    /**
     * 获取List
     *
     * @param key
     * @return
     */
    public List<String> lRange(String key, long start, long end) {
        return runJedisAction(w -> w.lrange(key, start, end));
    }

    /**
     * 从list中删除value 默认count 1
     *
     * @param key
     * @param values 值list
     * @return
     */
    public Long removeListValue(String key, List<String> values) {
        return removeListValue(key, 1, values);
    }

    /**
     * 从list中删除value
     * count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT 。
     * count < 0 : 从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT 的绝对值。
     * count = 0 : 移除表中所有与 VALUE 相等的值。
     *
     * @param key
     * @param count
     * @param values 值list
     * @return
     */
    public Long removeListValue(String key, long count, List<String> values) {
        if (values != null && values.size() > 0) {
            return runJedisAction(w -> {
                Long count1 = 0L;
                for (String value : values) {
                    count1 += w.lrem(key, count, value);
                }
                return count1;
            });
        }
        return null;
    }

    /**
     * 从list中删除value
     *
     * @param key
     * @param count 要删除个数
     * @param value
     * @return
     */
    public long removeListValue(String key, long count, String value) {
        return runJedisAction(w -> w.lrem(key, count, value));
    }

    /**
     * 设置列表指定索引的值
     *
     * @param key
     * @param index
     * @param value
     * @return
     */
    public String lSet(String key, long index, String value) {
        return runJedisAction(w -> w.lset(key, index, value));
    }

    /**
     * 保留指定范围的列表内容
     *
     * @param key
     * @param startOffset
     * @param endOffSet   -1表示到结尾
     * @return
     */
    public String lTrim(String key, long startOffset, long endOffSet) {
        return runJedisAction(w -> w.ltrim(key, startOffset, endOffSet));
    }

    /**
     * 取出列表中第一个元素
     *
     * @param key
     * @return
     */
    public String lPop(String key) {
        return runJedisAction(w -> w.lpop(key));
    }

    /**
     * 取出列表中最后一个元素
     *
     * @param key
     * @return
     */
    public String rpop(String key) {
        return runJedisAction(w -> w.rpop(key));
    }
    //endregion


    //region 有序集合

    /**
     * 添加值到有序集合
     *
     * @param key
     * @param score
     * @param value
     * @return
     */
    public long zAdd(String key, double score, String value) {
        return runJedisAction(w -> w.zadd(key, score, value));
    }

    /**
     * 添加值到有序集合
     *
     * @param key
     * @param items
     * @return
     */
    public long zAdd(String key, Map<String, Double> items) {
        return runJedisAction(w -> w.zadd(key, items));
    }

    /**
     * 获得排序集合
     *
     * @param key
     * @param startScore
     * @param endScore
     * @param orderByDesc
     * @return
     */
    public Set<String> getSoredSet(String key, long startScore, long endScore, boolean orderByDesc) {
        if (orderByDesc) {
            return runJedisAction(w -> w.zrevrangeByScore(key, endScore, startScore));
        } else {
            return runJedisAction(w -> w.zrangeByScore(key, startScore, endScore));
        }
    }

    /**
     * 计算排序长度
     *
     * @param key
     * @return
     */
    public Long zCard(String key) {
        return runJedisAction(w -> w.zcard(key));
    }

    /**
     * 增加指定元素的score
     *
     * @param key
     * @return
     */
    public Double zIncrScoreBy(String key, double score, String member) {
        return runJedisAction(w -> w.zincrby(key, score, member));
    }

    /**
     * 返回指定区间元素的数量
     *
     * @param key
     * @param startScore
     * @param endScore
     * @return
     */
    public Long zCount(String key, double startScore, double endScore) {
        return runJedisAction(w -> w.zcount(key, startScore, endScore));
    }

    /**
     * 删除排序集合
     *
     * @param key
     * @param value
     * @return
     */
    public Long zrem(String key, String... value) {
        return runJedisAction(w -> w.zrem(key, value));
    }

    /**
     * 按排名删除指定位置的元素
     *
     * @param key   key
     * @param start 开始索引
     * @param end   结束索引
     * @return
     */
    public Long zRemrangeByRank(String key, long start, long end) {
        return runJedisAction(w -> w.zremrangeByRank(key, start, end));
    }

    /**
     * 按分数删除指定位置的元素
     *
     * @param key   key
     * @param start 开始索引
     * @param end   结束索引
     * @return
     */
    public Long zremrangeByScore(String key, double start, double end) {
        return runJedisAction(w -> w.zremrangeByScore(key, start, end));
    }

    /**
     * 按元素删除指定位置的元素
     *
     * @param key   key
     * @param start 开始索引
     * @param end   结束索引
     * @return
     */
    public Long zremrangeByScore(String key, String start, String end) {
        return runJedisAction(w -> w.zremrangeByScore(key, start, end));
    }

    /**
     * 返回元素的排名
     *
     * @param key    key
     * @param member 开始索引
     * @return
     */
    public Long zrank(String key, String member) {
        return runJedisAction(w -> w.zrank(key, member));
    }

    /**
     * 返回元素的排名(倒序)
     *
     * @param key    key
     * @param member 开始索引
     * @return
     */
    public Long zrevrank(String key, String member) {
        return runJedisAction(w -> w.zrevrank(key, member));

    }

    /**
     * 获得排序集合
     *
     * @param key
     * @param startRange
     * @param endRange
     * @param orderByDesc
     * @return
     */
    public Set<String> getSoredSetByRange(String key, int startRange, int endRange, boolean orderByDesc) {
        if (orderByDesc) {
            return runJedisAction(w -> w.zrevrange(key, startRange, endRange));
        } else {
            return runJedisAction(w -> w.zrange(key, startRange, endRange));
        }
    }

    /**
     * 获取sorted set集合
     * <p>
     * 使用泛型指定元素类型。
     * </p>
     *
     * @param itemClass
     * @param key
     * @param startRange
     * @param endRange
     * @param orderByDesc
     * @param <T>
     * @return
     */
    public <T> Set<T> getSoredSetByRange(Class<T> itemClass, String key, int startRange, int endRange, boolean orderByDesc) {
        Set<String> sortedSet = null;
        if (orderByDesc) {
            sortedSet = runJedisAction(w -> w.zrevrange(key, startRange, endRange));
        } else {
            sortedSet = runJedisAction(w -> w.zrange(key, startRange, endRange));
        }
        Set<T> set = sortedSet.stream().map(s -> JsonUtil.jsonToBean(s, itemClass)).collect(toSet());
        return set;
    }

    /**
     * 获取sorted set集合
     * <p>
     * 1，使用泛型指定元素类型；
     * 2，返回结果有序集合。
     * </p>
     *
     * @param itemClass
     * @param key
     * @param startRange
     * @param endRange
     * @param orderByDesc
     * @param <T>
     * @return
     */
    public <T> List<T> getSortedSet2ListByRange(Class<T> itemClass, String key, int startRange, int endRange, boolean orderByDesc) {
        Set<String> sortedSet = null;
        if (orderByDesc) {
            sortedSet = runJedisAction(w -> w.zrevrange(key, startRange, endRange));
        } else {
            sortedSet = runJedisAction(w -> w.zrange(key, startRange, endRange));
        }

        return sortedSet.stream().map(s -> JsonUtil.jsonToBean(s, itemClass)).collect(toList());
    }
}
