package client.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gongshengjun
 * @date 2021/4/25 11:21
 */
public class ClientManager {

    private static final Logger logger = LoggerFactory.getLogger(ClientManager.class);

    public static final String SERVER_IP = "127.0.0.1";
    public static final int SERVER_PORT = 2000;

    private ClientManager() {}

    enum Singleton {
        /**
         * 枚举实现单例
         */
        INSTANCE;
        ClientManager processor;

        Singleton() {
            processor = new ClientManager();
        }

        public ClientManager getProcessor() {
            return processor;
        }
    }

    public static ClientManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

}
