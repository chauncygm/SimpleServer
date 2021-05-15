package game.db;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author gongshengjun
 * @date 2021/5/7 14:31
 */
public enum DBFactory {

    DB_GAME("jdbc.properties", "configuration.xml");

    private SqlSessionFactory factory;

    DBFactory(String resource, String dBConfig) {
        try {
            InputStream inputStream = Resources.getResourceAsStream(resource);
            Properties properties = Resources.getResourceAsProperties(dBConfig);
            this.factory = new SqlSessionFactoryBuilder().build(inputStream, properties);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public SqlSessionFactory getFactory() {
        return factory;
    }
}
