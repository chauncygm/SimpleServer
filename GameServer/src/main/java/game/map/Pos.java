package game.map;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author  gongshengjun
 * @date    2020/12/10 20:56
 */
public final class Pos {

    public static final int MAX_ROW_LENGTH = 1000;
    public static final int MAX_CELL_LENGTH = 1000;
    public static final Map<Integer, Pos> posPool = new ConcurrentHashMap<>();

    private final int x;            //坐标x
    private final int y;            //坐标y

    public Pos(int x, int y) {
        if (x < 0 || y < 0 || x > MAX_ROW_LENGTH || y > MAX_CELL_LENGTH) {
            throw new IllegalArgumentException("(" + x + "," + y + ")");
        }
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getIndex() {
        return getPosIndex(x, y);
    }

    public static Pos getPos(int x, int y) {
        Pos pos = posPool.get(getPosIndex(x, y));
        if (pos == null) {
            pos = new Pos(x, y);
            posPool.put(getPosIndex(x, y), pos);
        }
        return pos;
    }

    public static int getPosIndex(int x, int y) {
        return x * MAX_ROW_LENGTH + y;
    }

    public static List<Pos> getRoundPos(Pos pos) {
        List<Pos> roundPosList = new ArrayList<>();
        int left = Math.max(0, pos.getX() - 1);
        int right = Math.min(MAX_ROW_LENGTH, pos.getX() + 1);
        int bottom = Math.max(0, pos.getY() - 1);
        int top = Math.min(MAX_CELL_LENGTH, pos.getY() + 1);
        for (int i = left; i <= right; i++) {
            for (int j = bottom; j <= top; j++) {
                roundPosList.add(getPos(i, j));
            }
        }
        return roundPosList;
    }

    public static List<Pos> getRoundTenPos(Pos pos) {
        List<Pos> roundPosList = new ArrayList<>();
        roundPosList.add(getPos(Math.max(0, pos.getX() - 1), pos.getY()));
        roundPosList.add(getPos(Math.min(MAX_ROW_LENGTH, pos.getX() + 1), pos.getY()));
        roundPosList.add(getPos(pos.getX(), Math.max(0, pos.getY() - 1)));
        roundPosList.add(getPos(pos.getX(), Math.min(MAX_CELL_LENGTH, pos.getY() + 1)));
        return roundPosList;
    }

    public static boolean isBlock(Pos pos) {
        return pos.getX() >= 5 && pos.getX() <=7 && pos.getY() >= 5 && pos.getY() <= 7;
    }

    @Override
    public String toString() {
        return "Pos(" + x + ", " + y + ')';
    }
}
