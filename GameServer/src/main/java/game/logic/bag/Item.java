package game.logic.bag;

/**
 * 物品
 * @author  gongshengjun
 * @date    2020/11/10 20:25
 */
public class Item {

    /**
     * id
     */
    private int id;

    /**
     * 配置id
     */
    private int cfgId;

    /**
     * 数量
     */
    private int num;

    /**
     * 绑定状态
     */
    private boolean bind;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCfgId() {
        return cfgId;
    }

    public void setCfgId(int cfgId) {
        this.cfgId = cfgId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isBind() {
        return bind;
    }

    public void setBind(boolean bind) {
        this.bind = bind;
    }

    @Override
    public String toString() {
        return "Item{" +
                "cfgId=" + cfgId +
                ", num=" + num +
                ", bind=" + bind +
                '}';
    }
}

