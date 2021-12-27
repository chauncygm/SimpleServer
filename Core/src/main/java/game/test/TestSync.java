package game.test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gongshengjun
 * @date 2021/5/21 16:59
 */
public class TestSync {

    private List<String> list = new ArrayList<>();

    private List<String> otherList = new ArrayList<>();

    public List<String> getList() {
        return list;
    }

    public List<String> getOtherList() {
        return otherList;
    }

    public void doSomething() {
        synchronized (this) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("do something over.");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TestSync sync = new TestSync();
        new Thread(() -> {
           sync.doSomething();
        }).start();

        Thread.sleep(1000);
        System.out.println(sync.getOtherList().size());
    }

}
