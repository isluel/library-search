package com.library.feign;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.context.named.NamedContextFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.ActiveProfiles;
import spock.lang.Ignore;
import spock.lang.Specification

@Ignore
@SpringBootTest(classes = KakaoClientIntegrationTest.TestConfig.class)
@ActiveProfiles("test")
public class KakaoClientIntegrationTest extends Specification {

    @EnableFeignClients(clients = KakaoClient.class)
    @EnableAutoConfiguration
    static class TestConfig {}

    @Autowired
    KakaoClient kakaoClient;

    def "kakao 호출"() {
        given:
        when:
        def response = kakaoClient.search("HTTP", 1, 10)

        then:
        response.meta.totalCount() == 75
    }
}
