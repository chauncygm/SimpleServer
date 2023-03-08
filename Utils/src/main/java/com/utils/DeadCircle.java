package com.utils;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author by user
 * @description TODO
 * @date 2023/1/17 17:23
 */
public class DeadCircle {

    private int a;
    @sun.misc.Contended()
    private int b;

    public static void main(String[] args) {
        System.out.println(ClassLayout.parseInstance(new DeadCircle()).toPrintable());
    }
}
