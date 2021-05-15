import manager.ConfigScriptManager;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

/**
 * @author  gongshengjun
 * @date    2021/4/16 18:02
 */
public class ConfigData {

    public static void main(String[] args) throws Exception {
        ConfigScriptManager.getInstance().init();
        TimeUnit.SECONDS.sleep(5);
        ConfigScriptManager.getInstance().reloadConfig("CfgFrequencyLoad");

        aaa();
    }

    public static void aaa() throws Exception{
        // 创建代理服务器，映射到本地ShadowSockets工具的代理端口
        // 本地开的代理端口就是1080
        System.setProperty("https.protocols", "TLSv1.1");
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8786);
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, address); // http代理协议类型
        URL url = new URL("https://www.facebook.com/");
        URLConnection conn = url.openConnection(proxy);
        conn.connect();
    }
}
