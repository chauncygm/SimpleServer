package client.struct;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gongshengjun
 * @date 2021/4/26 11:47
 */
public class PlayerManager {

    private ConcurrentHashMap<Long, Player> playerMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, Player> getPlayerMap() {
        return playerMap;
    }

    public Player getPlayer(long uid) {
        return playerMap.get(uid);
    }

    public void cachePlayer(Player player) {
        if (player == null || player.getId() == 0) return;
        playerMap.put(player.getId(), player);
    }

    private PlayerManager() {}

    enum Singleton {
        /**
         * 枚举实现单例
         */
        INSTANCE;
        PlayerManager processor;

        Singleton() {
            processor = new PlayerManager();
        }

        public PlayerManager getProcessor() {
            return processor;
        }
    }

    public static PlayerManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
