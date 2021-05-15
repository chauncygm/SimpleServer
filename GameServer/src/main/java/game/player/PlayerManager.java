package game.player;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gongshengjun
 * @date 2021/4/26 17:16
 */
public class PlayerManager {

    private ConcurrentHashMap<Long, Player> playerMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<ChannelHandlerContext, Long> ctxMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, Player> getPlayerMap() {
        return playerMap;
    }

    public ConcurrentHashMap<ChannelHandlerContext, Long> getCtxMap() {
        return ctxMap;
    }

    public void cachePlayer(Player player) {
        if (player == null) return;
        playerMap.put(player.getUid(), player);
    }

    private PlayerManager() {}

    enum Singleton {
        /**
         * 枚举实现单例
         */
        INSTANCE;
        private PlayerManager processor;

        Singleton() {
            this.processor = new PlayerManager();
        }

        public PlayerManager getProcessor() {
            return processor;
        }
    }

    public static PlayerManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
