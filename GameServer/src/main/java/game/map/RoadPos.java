package game.map;

/**
 * @author  gongshengjun
 * @date    2020/12/11 1:03
 */
public class RoadPos {

    private Pos curPos;         //当前位置
    private int father;         //父节点位置索引
    private int weight;         //权重值

    public RoadPos(int x, int y) {
        this(Pos.getPos(x, y));
    }

    public RoadPos(Pos pos) {
        this(pos, -1, 0);
    }

    public RoadPos(Pos pos, int father) {
        this(pos, father, 0);
    }

    public RoadPos(Pos pos, int father, int weight) {
        if (pos == null) {
            throw new NullPointerException("pos is null!");
        }
        this.curPos = pos;
        this.father = father;
        this.weight = weight;
    }

    public Pos getCurPos() {
        return curPos;
    }

    public void setCurPos(Pos curPos) {
        this.curPos = curPos;
    }

    public int getFather() {
        return father;
    }

    public void setFather(int father) {
        this.father = father;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeight(Pos end) {
        return Math.abs(curPos.getX() - end.getX()) + Math.abs(curPos.getY() - end.getY());
    }
}
