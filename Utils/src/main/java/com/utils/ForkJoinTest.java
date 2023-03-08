package com.utils;

import cn.hutool.core.date.StopWatch;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author by user
 * @description TODO
 * @date 2022/8/18 11:13
 */
@State(value = Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 2, time = 2) //预热2次，没2秒1次
@Measurement(iterations = 2, time = 2) //测量2次，没2秒1次
@Threads(value = 2)
@Fork(value = 2)
public class ForkJoinTest {

    private static final ThreadLocalRandom random = ThreadLocalRandom.current();
    private static final StopWatch stopWatch = new StopWatch("Test");

    private static final Supplier<Integer> supplier = () -> random.nextInt(Integer.MAX_VALUE);

    public static void main(String[] args) {
        System.out.println(stopWatch.prettyPrint());
    }


    @Param(value = {"100000", "1000000", "10000000"})
    private int length;

    @Benchmark
    public void testStreamSort(Blackhole blackhole) {
//        stopWatch.start("Sync");
        List<Integer> collect = Stream.generate(supplier).limit(length).sorted().collect(Collectors.toList());
//        stopWatch.stop();
    }

    @Benchmark
    public void testParallelStreamSort() {
//        stopWatch.start("Async");
        List<Integer> collect = Stream.generate(supplier).limit(length).parallel().sorted().collect(Collectors.toList());
//        stopWatch.stop();
    }
}
