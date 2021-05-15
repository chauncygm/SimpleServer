package game.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author  gongshengjun
 * @date    2020/11/14 17:10
 */
public class ServerThread extends Thread{

    private static final Logger logger = LoggerFactory.getLogger(ServerThread.class);

    /**
     * 命令队列
     */
    private final LinkedBlockingQueue<ICommand> commandQueue = new LinkedBlockingQueue<>();

    private volatile boolean complete = false;

    private volatile boolean stop = false;

    public ServerThread(ThreadGroup group, String name) {
        super(group, name);
    }

    @Override
    public void run() {
        stop = false;
        while (!stop) {
            ICommand command = commandQueue.poll();
            if (command == null) {
                try {
                    synchronized (this) {
                        complete = true;
                        wait();
                    }
                } catch (InterruptedException e) {
                    logger.error("Thread: " + getName() + " run failed.Notify Exception:", e);
                }
                continue;
            }

            try {
                complete = false;
                command.action();
            } catch (Exception e) {
                logger.error(e.toString(), e);
            }
        }
    }

    /**
     * 添加命令到队列中
     */
    public void addCommand(ICommand command) {
        if (!stop) {
            try {
                commandQueue.put(command);
                synchronized (this) {
                    notify();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止线程
     */
    public void stopThread() {
        commandQueue.clear();
        stop = true;
        synchronized (this) {
            if (complete) {
                notify();
            }
        }
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean isStop() {
        return stop;
    }
}
