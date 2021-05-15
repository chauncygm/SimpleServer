package game.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * @author  gongshengjun
 * @date    2021/4/21 15:21
 */
public abstract class ConsoleThread extends Thread{

    private static final Logger logger = LoggerFactory.getLogger(ConsoleThread.class);

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String order = scanner.nextLine();
            try {
                dealOrder(order.trim());
            } catch (Exception e) {
                logger.error(e.toString(), e);
            }
        }
    }

    public abstract boolean dealOrder(String order);

}
