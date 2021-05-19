package game.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;
import java.util.concurrent.TimeUnit;

/**
 * 描述： ID生成器，雪花算法，支持每秒生成65536个ID
 *
 * 0 | 0000000 00000000 00000000 00000001 | 00000000 000000001 | 00000000 00000001
 * 高位为0，确保ID为正数。
 * 2~32位为自起始时间到当前时间的秒数，共31位支持34年
 * 33~48位表示设备节点标志,支持65535台节点
 * 49~64位为自增序列，标识每秒最多可调用并生成65536个ID
 *
 * @author gongshengjun
 * @date 2021/5/17 17:17
 */
public class UUIDUtil {

    private static final Logger logger = LoggerFactory.getLogger(UUIDUtil.class);

    /**
     * 初始时间
     */
    private static final String INIT_TIME_STR = "2020-07-07 07:07:07";

    /**
     * 初始时间戳
     */
    private static final long INIT_TIME_SEC = TimeUtil.getTime(INIT_TIME_STR) / 1000L;

    /**
     * 时间戳位偏移量
     */
    private static final int TIME_BIT_OFFSET = 32;

    /**
     * 节点标识位偏移量
     */
    private static final int IDENTIFY_BIT_OFFSET = 16;

    /**
     * 自增序列最大位数
     */
    private static final int SEQUENCE_BIT_NUM = 16;

    /**
     * 自增序列最大值
     */
    private static final int MAX_SEQUENCE = (1 << SEQUENCE_BIT_NUM) - 1;

    /**
     * 自起始时间到ID生成时间的秒数
     */
    private static long SEC = 0;

    /**
     * 每秒生成ID的自增序列号
     */
    private static int SEQUENCE = 0;

    /**
     * 节点标识
     */
    private static long NODE_TAG = 0;

    /**
     * 初始化UUID生成器
     *
     * @param tag 当前节点标识
     */
    public static void init(int tag) throws Exception {
        if (tag < 0) {
            throw new InvalidParameterException("初始服务器标志错误：" + tag);
        }
        long nowSec = curSec();
        if (nowSec <= INIT_TIME_SEC) {
            throw new Exception("系统时间小于初始时间：" + INIT_TIME_STR);
        }
        SEC = nowSec - INIT_TIME_SEC;
        NODE_TAG = tag;
    }

    /**
     * 获取UUID
     */
    public synchronized static long getUUID() {
        if (NODE_TAG <= 0) {
            logger.error("生成ID失败，UUID生成器未初始化!");
            System.exit(-1);
        }
        return generate();
    }

    /**
     * 解析ID生成的时间戳
     */
    public static long parseTime(long id) {
        return (INIT_TIME_SEC + (id >> TIME_BIT_OFFSET)) * 1000;
    }

    /**
     * 解析ID生成的节点标识
     */
    public static int parseNodeTag(long id) {
        return (int) (id << TIME_BIT_OFFSET >> (TIME_BIT_OFFSET + SEQUENCE_BIT_NUM));
    }

    private static long generate() {

        long nowSec = curSec();
        if (INIT_TIME_SEC + SEC < nowSec) {
            SEC = nowSec - INIT_TIME_SEC;
            SEQUENCE = 0;
            return getUUID();
        }

        if (SEQUENCE <= MAX_SEQUENCE) {
            return ~(1L << 63) & (SEC << TIME_BIT_OFFSET | NODE_TAG << IDENTIFY_BIT_OFFSET | SEQUENCE ++);
        }

        sleep();
        return getUUID();
    }

    private static void sleep() {
        try {
            logger.error("UUID生成器调用过于频繁，开始sleep(200).");
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            logger.error("UUID生成器sleep异常:" + e, e);
        }
    }

    private static long curSec() {
        return System.currentTimeMillis() / 1000L;
    }
}
