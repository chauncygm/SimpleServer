package com.arithmetic;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 8种排序算法，以0-10000的数为示例
 *
 * @author chauncy
 */
public class SortArithmetic {


    private static final AtomicInteger atomicInteger = new AtomicInteger();
    private static final SecureRandom random = new SecureRandom();

    public static void main(String[] args) {
        List<Integer> list = Stream.generate(() -> random.nextInt(999)).limit(25).collect(Collectors.toList());
        Collections.shuffle(list);
        int[] arr = list.stream().mapToInt(Integer::intValue).toArray();
//        for (int i = 1; i <= 8; i++) {
//            testSort(arr, i);
//        }
//        for (int i = 0; i <= 10; i++) {
//            testSort(arr, 8);
//        }
        testSort(arr, 6);
    }

    public static void testSort(int[] arr, int type) {
        long start = System.currentTimeMillis();
        String TP = "";
        switch (type) {
            case 1:
                insertionSort(arr);
                TP = "插入";
                break;
            case 2:
                shellSort(arr);
                TP = "希尔";
                break;
            case 3:
                radixSort(arr);
                TP = "基数";
                break;
            case 4:
                bubbleSort(arr);
                TP = "冒泡";
                break;
            case 5:
                mergeSort(arr, 0, arr.length - 1);
                TP = "归并";
                break;
            case 6:
                heapSort(arr);
                TP = "堆";
                break;
            case 7:
                bucket(arr);
                TP = "桶";
                break;
            case 8:
                quickSort(arr, 0, arr.length - 1);
                TP = "快速";
                break;
            default:
                break;
        }
        long end = System.currentTimeMillis();
        System.out.println(TP + "排序use：" + (end - start) + " MS");
    }

    /**
     * 插入排序
     *
     * @param arr 源数组
     */
    public static void insertionSort(int[] arr) {
        int i, j, length = arr.length;
        //从1开始，一次遍历后一个数，将其插入到前面的数中
        for (i = 1; i < length; i++) {
            int tmp = arr[i];
            //依次排好前n个数
            for (j = i; j > 0 && arr[j - 1] > tmp; j--) {
                arr[j] = arr[j - 1];
            }
            arr[j] = tmp;
        }
    }

    /**
     * 希尔排序，每次循环将增量一分为二，使相差增量的数有序
     *
     * @param arr 源数组
     */
    public static void shellSort(int[] arr) {
        int length = arr.length;
        //gap 增量，即组的大小，每次循环减小一半，直到为1，如20个数，gap=[10, 5, 2, 1]
        for (int gap = length / 2; gap > 0; gap /= 2) {
            //增量大小开始到length为止依次遍历，[10,19],[5,19],[2,19],[1,19]
            for (int i = gap; i < length; i++) {
                //将自己与和自己相差增量倍数的数比较，放到最靠左的位置
                int j = i, temp = arr[i];
                while (j >= gap && arr[j - gap] > temp) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                //所有增量倍数的数中最靠左比该数大的数与之交换
                arr[j] = temp;
            }
        }
    }

    /**
     * 基数排序，每次根据数字在整10位上的基数进行排序，先排个位数，然后十位，直到最高位
     * 初始数组：[4, 21, 8, 18, 5, 15, 23, 11, 10, 12, 19, 9, 24, 3, 2, 0, 7, 6, 22, 13, 14, 1, 16, 17, 20]
     * output1：[10, 0, 20, 21, 11, 1, 12, 2, 22, 23, 3, 13, 4, 24, 14, 5, 15, 6, 16, 7, 17, 8, 18, 19, 9]
     * output2：[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24]
     *
     * @param arr 源数组
     */
    public static void radixSort(int[] arr) {
        //找到数组中的最大值
        int max = Arrays.stream(arr).reduce(0, Math::max);

        //依次取个位，十位，百位直到最大值的最高位，如最大值800，则exp=1,10,100
        //每次循环后将指定位相同的数放在一起（小于等于指定位的数有序，有较高位数的数无序，每次循环）
        for (int exp = 1; exp <= max; exp *= 10) {
            //存放每次循环排序的结果
            int[] output = new int[arr.length];
            //存放数组中基数为下标，即基数为0~9的个数，size为10，
            int[] count = new int[10];

            //统计当前位相同的数的个数，如十位为0的有3个，为1的有2个，count[]=[3,2,x...]
            for (int value : arr) {
                count[(value / exp) % 10]++;
            }

            //将个数转化为排列后指定位数为i的数最后一个的位置，count[]=[3,5,5+x,...],
            //表示排序后前3个是指定位为0的数，4~5位置是指定位为1的数，...
            for (int i = 1; i < count.length; i++) {
                count[i] += count[i - 1];
            }

            //遍历源数组，(必须从最后开始遍历，因为从第二次遍历开始后面的数基数更大)
            for (int i = arr.length - 1; i >= 0; i--) {
                //取指定位的基数
                int baseNum = (arr[i] / exp) % 10;
                //取当前数的基数的位置，并存入
                output[count[baseNum] - 1] = arr[i];
                //存一个，位置-1，表示下一个相同基数的数放在当前数的左边小一位位置
                count[baseNum]--;
            }

            //把排序结果保存到源数组，以便下一次排序，此时位数相同的数，大数在小数右边
            arr = output;
            System.out.println(Arrays.toString(arr));
        }
    }

    /**
     * 冒泡排序,每次将最大的数冒泡到最右边
     *
     * @param arr 源数组
     */
    public static void bubbleSort(int[] arr) {
        int length = arr.length;
        //循环N次，每次找到最大值
        for (int i = 0; i < length; i++) {
            //从0开始，到length - i -1为止
            for (int j = 0; j < length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                }
            }
        }
    }

    /**
     * 归并排序，分治法，每次将待排数组分成两份，递归进行排序
     *
     * @param arr 源数组
     * @param l   左边索引
     * @param r   右边索引
     */
    public static void mergeSort(int[] arr, int l, int r) {
        if (l < r) {
            //m 中间索引
            int m = l + (r - l) / 2;
            //分别排序左边数组和右边数组
            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);
            //合并左右数组
            merge(arr, l, m, r);
        }
    }

    /**
     * 合并数组，此处设定左边数组和右边数组有排好序了，或者左右数组size为1
     *
     * @param arr 源数组
     * @param l   左边索引
     * @param m   中间索引
     * @param r   右边索引
     */
    private static void merge(int[] arr, int l, int m, int r) {
        int i = 0, j = 0, k = l;
        //n1 左边数组的大小
        int n1 = m - l + 1;
        //n2 右边数组大小
        int n2 = r - m;
        int[] L = Arrays.copyOfRange(arr, l, m + 1);
        int[] R = Arrays.copyOfRange(arr, m + 1, r + 1);

        //将L,R排序放到源数组中
        while (i < n1 && j < n2 && k <= r) {
            if (L[i] <= R[j]) {
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
        }
        //若左边数组个数大于右边数组个数
        while (i < n1 && k <= r) {
            arr[k++] = L[i++];
        }
        //若右边数组个数大于左边数组个数
        while (j < n2 && k <= r) {
            arr[k++] = R[j++];
        }

    }

    /**
     * 堆排序
     * 最大堆:每个子树的父节点都大于子节点，左右节点无大小关系
     *
     * @param arr 待排序数组
     */
    public static void heapSort(int[] arr) {
        int length = arr.length;

        //将数组排序成最大堆结构，
        for (int i = length / 2 - 1; i >= 0; i--) {
            heapify(arr, length, i);
        }

        //从最大堆中一次取出第一个值到数组最后
        for (int i = length - 1; i > 0; i--) {
            swap(arr, 0, i);
            //重新排列剩余数组为最大堆结构
            heapify(arr, i, 0);
        }
    }

    /**
     * 交换数组元素
     */
    private static void swap(int[] arr, int i, int j) {
        if (i >= arr.length || j >= arr.length || i == j) {
            System.out.println("error, i: " + i + ", j: " + j);
            return;
        }
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * 将数组中指定根节点的树排序成最大堆结构
     * 索引：    0             若数组长度为n,则拥有子节点的最大索引为 (n / 2) - 1
     * 1    2           需要对索引为[(n /2) -1] ~ [0] 的子树做进行最大堆处理
     * 3 4  5 6
     * ..........
     *
     * @param arr 源数组
     * @param n   数组长度
     * @param i   根节点索引
     */
    private static void heapify(int[] arr, int n, int i) {
        int largest = i;        //根节点索引
        int l = 2 * i + 1;      //左叶子索引
        int r = 2 * i + 2;      //右叶子索引

        if (l < n && arr[l] > arr[largest]) largest = l;
        if (r < n && arr[r] > arr[largest]) largest = r;
        //找到最大值的节点索引，如果不相同，说明应该交换根节点和叶子节点
        if (largest != i) {
            swap(arr, i, largest);
            //如果被交换，继续排叶子根节点为最大堆
            heapify(arr, n, largest);
        }
    }

    /**
     * 桶排序，彻底的分治思想
     * 排序相对最快，但相对消耗空间更大
     * 已知待排序序列为0-10000的整数
     */
    public static void bucket(int[] arr) {
        //声明一个size为10000的数组
        int[] bucket = new int[10000];
        //遍历arr，将桶编号为元素值的数++,表示该桶拥有的元素
        //此处每个桶有一个元素，若桶的数量小于待排序数量，桶中有多个元素
        //还需要对每个桶的元素进行排序，可使用其他排序算法
        for (int i : arr) {
            bucket[i]++;
        }
        int k = 0;
        //遍历桶，即放到原数组的中位置;
        for (int j = 0; j < bucket.length; j++) {
            //这里没有桶最多一个元素，所以用if，若有多个需排序后for循环输出
            if (bucket[j] != 0) {
                arr[k++] = j;
            }
        }
    }

    /**
     * 快速排序,简称快排，分治思想
     */
    public static void quickSort(int[] arr, int start, int end) {
        if (start >= end) {
            return;
        }
        int left = start, right = end - 1;
        //以最后一个数为基准，小数放左边，大数放右边，再将其放中间
        int mid = arr[end];
        while (left < right) {
            while (arr[left] <= mid && left < right) {
                left++;
            }
            while (arr[right] >= mid && left < right) {
                right--;
            }
            if (left == right) {
                break;
            }
            swap(arr, left, right);
        }
        //不确定while结束前是left++还是right--，即不确定left索引的值是否大于基准值
        //所以还需要进行一次判断
        if (arr[left] >= arr[end]) {
            swap(arr, left, end);
        } else {
            left++;
        }
        //递归分别对左边和右边序列进行排序
        quickSort(arr, start, left - 1);
        quickSort(arr, left + 1, end);
    }


}