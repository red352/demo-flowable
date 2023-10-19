package org.example;

import org.flowable.common.engine.impl.persistence.StrongUuidGenerator;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

class AppTest {
    @Test
    void test1() {
        StrongUuidGenerator strongUuidGenerator = new StrongUuidGenerator();
        IntStream.range(0, 10).forEach(i-> System.out.println(strongUuidGenerator.getNextId()));
    }
}