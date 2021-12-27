package com.arithmetic;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

/**
 * 最大子数组问题
 *
 * @author gongshengjun
 * @date 2021/7/26 17:50
 */
public class MaxSumArray {


    private static final SecureRandom RANDOM = new SecureRandom();

    private static class MaxSumResult {
        int start;
        int length;
        int sum;
        int toEndSum;

        public MaxSumResult(int start, int length, int sum, int toEndSum) {
            this.start = start;
            this.length = length;
            this.sum = sum;
            this.toEndSum = toEndSum;
        }

        @Override
        public String toString() {
            return "MaxSumResult{" +
                    "start=" + start +
                    ", length=" + length +
                    ", sum=" + sum +
                    '}';
        }
    }


    public static void main(String[] args) throws InterruptedException {
        test(50);
    }

    private static void test(int num) {
        int[] source = Stream.generate(() -> RANDOM.nextInt(100) - 50).limit(num).mapToInt(Integer::intValue).toArray();

        System.out.println(Arrays.toString(source));
        long t1 = System.currentTimeMillis();
        MaxSumResult result = getMaxSumArray(source, source.length - 1);
        System.out.println(result);

        long t2 = System.currentTimeMillis();
        MaxSumResult maxSumArray = getMaxSumArray(source);
        System.out.println(maxSumArray);
        long t3 = System.currentTimeMillis();
        Runtime runtime = Runtime.getRuntime();
        runtime.traceMethodCalls(true);

        System.out.println("消耗：" + (t2 - t1) + " ---- " + (t3 - t2));
    }

    /**
     * 暴力破解法
     */
    private static MaxSumResult getMaxSumArray(int[] array) {
        int start = 0, length = 0;
        int maxSum = 0;

        for (int i = 0; i < array.length; i++) {
            for (int j = 1; j <= array.length - i; j++) {
                int sum = 0;
                for (int k = i; k < i + j; k++) {
                    sum += array[k];
                }
                if (sum > maxSum) {
                    maxSum = sum;
                    start = i;
                    length = j;
                }
            }
        }
        return new MaxSumResult(start, length, maxSum, 0);
    }


    /**
     * 线性复杂的
     */
    private static MaxSumResult getMaxSumArray(int[] array, int end) {
        MaxSumResult result = null;

        for (int i = 0; i < array.length; i++) {
            if (i == 0) {
                result = array[0] > 0 ? new MaxSumResult(0, 1, array[0], array[0]) : new MaxSumResult(1, 0, 0, 0);
                continue;
            }
            result.toEndSum += array[i];
            if (array[i] <= 0) {
                continue;
            }

            if (result.toEndSum > result.sum) {
                result.length = i - result.start + 1;
                result.sum = result.toEndSum;
                continue;
            }

            int sum = 0, maxSum = 0, newStart = i;
            for (int j = i; j >= result.start; j--) {
                sum += array[j];
                if (sum > maxSum) {
                    maxSum = sum;
                    newStart = j;
                }
            }
            if (maxSum > result.sum) {
                result.start = newStart;
                result.length = i - newStart + 1;
                result.sum = result.toEndSum = maxSum;
            }
        }
//
//        if (end > 0) {
//            result = getMaxSumArray(array, end - 1);
//        }
//        if (end == 0) {
//            return array[0] > 0 ? new MaxSumResult(0, 1, array[0], array[0]) : new MaxSumResult(1, 0, 0, 0);
//        }
//        if (result == null) {
//            return null;
//        }
//
//        result.toEndSum += array[end];
//        if (array[end] <= 0) {
//            return result;
//        }
//
//        if (result.toEndSum > result.sum) {
//            result.length = end - result.start + 1;
//            result.sum = result.toEndSum;
//        } else {
//            int sum = 0, maxSum = 0, newStart = end;
//            for (int i = end; i >= result.start; i--) {
//                sum += array[i];
//                if (sum > maxSum) {
//                    maxSum = sum;
//                    newStart = i;
//                }
//            }
//            if (maxSum > result.sum) {
//                result.start = newStart;
//                result.length = end - newStart + 1;
//                result.sum = result.toEndSum = maxSum;
//            }
//        }

        return result;
    }

}
