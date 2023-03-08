package com.utils;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
/*此类所有基准测试的方法继承此类上的注解*/
/*Benchmark所有测试线程共享一个实例，Group同一个线程在一个group里共享实例，Thead每个测试线程分配一个实例*/
@State(value = Scope.Benchmark)
/*测试的指标，可选：吞吐量、平均耗时、采样耗时、单次运行耗时(主要用于冷性能，开销过小的测试会使计时器开销占比增大)*/
@BenchmarkMode(Mode.AverageTime)
/*预热次数，每次耗时(秒)， 默认5次，每次10秒*/
@Warmup(iterations = 3, time = 2, timeUnit = TimeUnit.SECONDS, batchSize = 2)
/*测量次数，每次耗时(秒)， 默认5次，每次10秒*/
@Measurement(iterations = 3, time = 2)
/*每个进程的测试线程数,每次测试之间，线程之间是同步的*/
@Threads(4)
/*fork出子进程的数量*/
@Fork(1)
/*测试统计结果时间单位*/
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class StringConnectTest {

    @Param(value = {"10", "50", "100"})
    private int length;

    @Benchmark
    public void testStringAdd(Blackhole blackhole) {
        String a = "";
        for (int i = 0; i < length; i++) {
            a += i;
        }
        blackhole.consume(a);
    }

    @Benchmark
    public void testStringBuilderAdd(Blackhole blackhole) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(i);
        }
        blackhole.consume(sb.toString());
    }

    @Benchmark
    public void measureName(Blackhole bh) {
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(StringConnectTest.class.getSimpleName())
                .result("result.json")
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }
}
