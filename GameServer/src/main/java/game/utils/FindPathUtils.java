package game.utils;

import game.map.Pos;
import game.map.RoadPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author  gongshengjun
 * @date    2020/12/10 20:55
 */
public class FindPathUtils {

    private static final Logger logger = LoggerFactory.getLogger(FindPathUtils.class);

    /**
     * 寻路
     *
     * @param start     起始坐标
     * @param end       结束坐标
     * @param limitStep 限制步数
     * @return          返回路点
     */
    public static List<Pos> findPath(Pos start, Pos end, int limitStep) {
        List<Pos> result = new ArrayList<>();
        if (start.getIndex() == end.getIndex()) {
            return result;
        }

        int step = 0;
        //经过的坐标索引
        Set<Integer> passed = new HashSet<>();
        //计算过的路点
        HashMap<Integer, RoadPos> countedMap = new HashMap<>();

        //待探寻的坐标列表
        List<RoadPos> waitPosList = new ArrayList<>();
        waitPosList.add(new RoadPos(start));

        //最后一个点
        RoadPos lastRoadPos = new RoadPos(end);

        while(step < limitStep && !waitPosList.isEmpty()) {
            RoadPos roadPos = waitPosList.remove(0);
            //到终点了
            if (roadPos.getCurPos().getIndex() == end.getIndex()) {
                lastRoadPos.setFather(roadPos.getFather());
                break;
            }
            //计算过了
            if (countedMap.containsKey(roadPos.getCurPos().getIndex())) {
                continue;
            }
            //阻挡点
            if (Pos.isBlock(roadPos.getCurPos())) {
                continue;
            }

            step ++;
            countedMap.put(roadPos.getCurPos().getIndex(), roadPos);

            //从周围的点中找最近的点
            for (Pos tempPos : Pos.getRoundPos(roadPos.getCurPos())) {
                if (passed.contains(tempPos.getIndex())) continue;

                RoadPos tempRoadPos = new RoadPos(tempPos, roadPos.getCurPos().getIndex());
                tempRoadPos.setWeight(tempRoadPos.getWeight(end));
                passed.add(tempPos.getIndex());

                addToWaitList(waitPosList, tempRoadPos);
            }
        }

        //没找到终点用最后一个点
        if (lastRoadPos.getFather() == -1) {
            lastRoadPos = waitPosList.remove(0);
        }

        //绘制路径
        result.add(lastRoadPos.getCurPos());
        while (lastRoadPos.getFather() != -1) {
            lastRoadPos = countedMap.get(lastRoadPos.getFather());
            result.add(lastRoadPos.getCurPos());
        }

        return result;
    }

    private static void addToWaitList(List<RoadPos> list, RoadPos roadPos) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getWeight() > roadPos.getWeight()) {
                list.add(i, roadPos);
                return;
            }
        }
        list.add(roadPos);
    }


    public static void main(String[] args) {
        Pos start = Pos.getPos(1, 1);
        Pos end = Pos.getPos(10, 10);
        List<Pos> result = findPath(start, end, 1000);
        for (Pos pos : result) {
            logger.info(pos.toString());
        }
    }
}
