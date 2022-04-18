package com.ecinema.app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ECinemaApplicationTests {

    @Test
    public void contextLoads() {}

    @Test
    public void test() {
        assertEquals(20, 5 * 4);
    }

}
