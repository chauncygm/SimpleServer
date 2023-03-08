package com.utils;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author by user
 * @description TODO
 * @date 2022/7/7 19:31
 */
public class IPUtil {

    public static String getLocalIP() {
        String ip = null;
        try {
            Enumeration<?> e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration<?> ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) ee.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        ip = inetAddress.getHostAddress();
                        break;
                    }
                }
            }
            if (ip == null) {
                ip = InetAddress.getLocalHost().getHostAddress();
            }
        } catch (Exception ignore) {
            return "127.0.0.1";
        }
        return ip;
    }

    public static void main(String[] args) {
        System.out.println(IPUtil.getLocalIP());
    }
}
