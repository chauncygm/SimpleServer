/*
 * @AUTO GENERATED
 * AUTO GENERATED FILE, DON'T TO EDIT.
 */
package cfg;

import struct.IntegerReadArray;
import struct.FloatReadArray;
import struct.IntegerReadArrayEs;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author  gongshengjun
 * @date    2021-5-6 11:30:26
 */
public class CfgFrequency {

    private static final long INIT_TIME = System.currentTimeMillis();

    private static final Map<Integer, CfgFrequency> map = new LinkedHashMap<>();

    /**
     * 功能类型
     */
    private final int type;

    /**
     * 基础次数（每日免费次数）
     */
    private final float initNum;

    /**
     * 购买次数上限
     */
    private final long timeMax;

    /**
     * 购买这个次数消耗哪个货币道具
     */
    private final IntegerReadArray consumeItem;

    /**
     * 购买这个次数每次所需的道具数量
     */
    private final IntegerReadArray consume1;

    /**
     * 购买这个次数每次所需的道具数量
     */
    private final FloatReadArray consume2;

    /**
     * 购买这个次数每次所需的道具数量
     */
    private final IntegerReadArrayEs consume3;

    /**
     * 购买这个次数每次所需的道具数量
     */
    private final IntegerReadArrayEs consume4;

    /**
     * 购买这个次数每次所需的道具数量
     */
    private final Map<Integer, Integer> consume5;

    /**
     * 购买这个次数每次所需的道具数量
     */
    private final Map<Integer, IntegerReadArray> consume6;


    public CfgFrequency(int type, float initNum, long timeMax, String consumeItem, String consume1, String consume2, String consume3, String consume4, String consume5, String consume6) {
        this.type = type;
        this.initNum = initNum;
        this.timeMax = timeMax;
        this.consumeItem = new IntegerReadArray(consumeItem, "_");
        this.consume1 = new IntegerReadArray(consume1, "_");
        this.consume2 = new FloatReadArray(consume2, "_");
        this.consume3 = new IntegerReadArrayEs(consume3, "\\|", "_");
        this.consume4 = new IntegerReadArrayEs(consume4, "\\|", "_");
        this.consume5 = new LinkedHashMap<>();
        for (String s : consume5.split("\\|")) {
            if (s.trim().isEmpty()) continue;
            this.consume5.put(Integer.parseInt(s.split(":")[0]), Integer.parseInt(s.split(":")[1]));
        }
        this.consume6 = new LinkedHashMap<>();
        for (String s : consume6.split("\\|")) {
            if (s.trim().isEmpty()) continue;
            this.consume6.put(Integer.parseInt(s.split(":")[0]), new IntegerReadArray(s.split(":")[1], "_"));
        }
    }

    public int getType() {
        return type;
    }

    public float getInitNum() {
        return initNum;
    }

    public long getTimeMax() {
        return timeMax;
    }

    public IntegerReadArray getConsumeItem() {
        return consumeItem;
    }

    public IntegerReadArray getConsume1() {
        return consume1;
    }

    public FloatReadArray getConsume2() {
        return consume2;
    }

    public IntegerReadArrayEs getConsume3() {
        return consume3;
    }

    public IntegerReadArrayEs getConsume4() {
        return consume4;
    }

    public Map<Integer, Integer> getConsume5() {
        return consume5;
    }

    public Map<Integer, IntegerReadArray> getConsume6() {
        return consume6;
    }

    public static CfgFrequency get(int id) {
        return map.get(id);
    }

    public static CfgFrequency getNotNull(int id) {
        return Objects.requireNonNull(map.get(id));
    }

    public static Map<Integer, CfgFrequency> all() {
        return map;
    }

}