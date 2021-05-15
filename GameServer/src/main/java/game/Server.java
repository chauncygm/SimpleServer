package game;

import game.server.GameServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        new GameServer().start();
    }
}
