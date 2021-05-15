/*
 * @AUTO GENERATED
 * AUTO GENERATED FILE, DON'T TO EDIT.
 */
package cfg;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author  gongshengjun
 * @date    2021-5-6 11:30:26
 */
public class CfgGlobal {

    private static final long INIT_TIME = System.currentTimeMillis();

    private static final Map<Integer, CfgGlobal> map = new LinkedHashMap<>();

    /**
     * 每个军队的最大英雄数量
     */
    private final int armyHeroNumber;

    /**
     * 兵种数量修正系数
     */
    private final float soldierCountModify;

    /**
     * 命中系数
     */
    private final int hitCoefficient;

    /**
     * 基础命中率
     */
    private final int baseHitRate;

    /**
     * 暴击溢值
     */
    private final int criticalOverflow;

    /**
     * 暴击增幅系数
     */
    private final int critIncreaseCoefficient;

    /**
     * 防御系数
     */
    private final int defenseFactor;

    /**
     * 攻击上限控制
     */
    private final float attackSpeedMax;

    /**
     * 攻击下限控制
     */
    private final float attackSpeedMin;


    public CfgGlobal(int armyHeroNumber, float soldierCountModify, int hitCoefficient, int baseHitRate, int criticalOverflow, int critIncreaseCoefficient, int defenseFactor, float attackSpeedMax, float attackSpeedMin) {
        this.armyHeroNumber = armyHeroNumber;
        this.soldierCountModify = soldierCountModify;
        this.hitCoefficient = hitCoefficient;
        this.baseHitRate = baseHitRate;
        this.criticalOverflow = criticalOverflow;
        this.critIncreaseCoefficient = critIncreaseCoefficient;
        this.defenseFactor = defenseFactor;
        this.attackSpeedMax = attackSpeedMax;
        this.attackSpeedMin = attackSpeedMin;
    }

    public int getArmyHeroNumber() {
        return armyHeroNumber;
    }

    public float getSoldierCountModify() {
        return soldierCountModify;
    }

    public int getHitCoefficient() {
        return hitCoefficient;
    }

    public int getBaseHitRate() {
        return baseHitRate;
    }

    public int getCriticalOverflow() {
        return criticalOverflow;
    }

    public int getCritIncreaseCoefficient() {
        return critIncreaseCoefficient;
    }

    public int getDefenseFactor() {
        return defenseFactor;
    }

    public float getAttackSpeedMax() {
        return attackSpeedMax;
    }

    public float getAttackSpeedMin() {
        return attackSpeedMin;
    }

    public static CfgGlobal get() {
        return Objects.requireNonNull(map.get(1));
    }

    public static Map<Integer, CfgGlobal> all() {
        return map;
    }

}