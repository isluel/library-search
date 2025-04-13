package com.library.feign;

import com.library.NaverBookResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest(classes = NaverClientTest.TestConfig.class)
//@ActiveProfiles("test")
class NaverClientTest1 {

//    @EnableAutoConfiguration
//    @EnableFeignClients(clients = NaverClient.class)
//    static class TestConfig{}
//
//    @Autowired
//    NaverClient naverClient;
//
//    @Test
//    void callNaver() {
//        NaverBookResponse http = naverClient.search("HTTP", 1, 10);
//        System.out.println(http);
//
//        assertFalse(http.isEmpty());
//    }
}