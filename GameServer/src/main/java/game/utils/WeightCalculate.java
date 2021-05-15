package game.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightCalculate<Obj> {
    
    private static final SecureRandom random = new SecureRandom();

    /**
     * 对象列表
     */
    private final List<Obj> objList = new ArrayList<>();
    /**
     * 权重列表
     */
    private final List<Integer> weightList = new ArrayList<>();
    /**
     * 总权重值
     */
    private int sumWeight;

    public void init() {
        objList.clear();
        weightList.clear();
        sumWeight = 0;
    }

    /**
     * 添加对象和对应的权重值
     *
     * @param obj       对象
     * @param weight    权重值
     */
    public boolean addObject(Obj obj, int weight) {
        if (weight < 0) {
            return false;
        }
        objList.add(obj);
        weightList.add(weight);
        sumWeight += weight;
        return true;
    }

    /**
     * 根据随机权重值获得物品，并移除
     */
    public Obj getObjectAndRemove() {
        int randomValue = random.nextInt(sumWeight) + 1;
        int index = getIndex(randomValue);
        if (index < 0) {
            return null;
        }
        sumWeight -= weightList.remove(index);
        return objList.remove(index);
    }

    /**
     * 根据随机权重值获得物品
     */
    public Obj getObject() {
        int randomValue = random.nextInt(sumWeight) + 1;
        int index = getIndex(randomValue);
        if (index < 0) {
            return null;
        }
        return objList.get(index);
    }

    private int getIndex(int randomValue) {
        if (randomValue > sumWeight) {
            return -1;
        }
        if (randomValue < 0) {
            return -1;
        }
        int size = objList.size();
        int curRate = 0;
        int tmpWeight;
        for (int i = 0; i < size; ++i) {
            tmpWeight = weightList.get(i);
            if (randomValue > curRate && randomValue <= curRate + weightList.get(i)) {
                return i;
            }
            curRate += tmpWeight;
        }
        return -1;
    }

}
