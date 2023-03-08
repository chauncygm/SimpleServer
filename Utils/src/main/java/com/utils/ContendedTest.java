package com.utils;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author by user
 * @description TODO
 * @date 2023/1/17 17:37
 */
public class ContendedTest {

    byte a;
    @sun.misc.Contended("a")
    long b;
    @sun.misc.Contended("b")
    long c;
    int d;

    private static Unsafe UNSAFE;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            UNSAFE = (Unsafe) f.get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws NoSuchFieldException {
        System.out.println("offset-a: " + UNSAFE.objectFieldOffset(ContendedTest.class.getDeclaredField("a")));
        System.out.println("offset-b: " + UNSAFE.objectFieldOffset(ContendedTest.class.getDeclaredField("b")));
        System.out.println("offset-c: " + UNSAFE.objectFieldOffset(ContendedTest.class.getDeclaredField("c")));
        System.out.println("offset-d: " + UNSAFE.objectFieldOffset(ContendedTest.class.getDeclaredField("d")));

        ContendedTest contendedTest = new ContendedTest();

        long objectSize = ObjectSizeCalculator.getObjectSize(true);
        long objectSize1 = ObjectSizeCalculator.getObjectSize('a');
        long objectSize2 = ObjectSizeCalculator.getObjectSize(10);
        long objectSize3 = ObjectSizeCalculator.getObjectSize(100L);
        long objectSize4 = ObjectSizeCalculator.getObjectSize(new Object());
        long objectSize5 = ObjectSizeCalculator.getObjectSize(new int[]{1, 2});
        long objectSize6 = ObjectSizeCalculator.getObjectSize(new int[]{1});
        System.out.println("objectSize: " + objectSize);
        System.out.println("objectSize1: " + objectSize1);
        System.out.println("objectSize2: " + objectSize2);
        System.out.println("objectSize3: " + objectSize3);
        System.out.println("objectSize4: " + objectSize4);
        System.out.println("objectSize5: " + objectSize5);
        System.out.println("objectSize6: " + objectSize6);
    }

}
