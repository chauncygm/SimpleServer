package com.utils;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

/**
 * @author by user
 * @description TODO
 * @date 2022/7/4 11:29
 */
public class BenchSample {

    @Benchmark
    public void measureName(Blackhole bh) {
        bh.consume(11);
    }
}
