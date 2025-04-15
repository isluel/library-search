package com.library.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class KakaoClientConfiguration {
    @Bean
    public RequestInterceptor kakaoRequestInterceptor(
            @Value("${external.kakao.headers.rest-api-key}") String restApiKey
    ) {
        return requestTemplate -> requestTemplate.header("Authorization", "kakaoAK " + restApiKey);
    }

    @Bean
    public KakaoErrorDecoder kakaoErrorDecoder(ObjectMapper objectMapper) {
        return new KakaoErrorDecoder(objectMapper);
    }
}
