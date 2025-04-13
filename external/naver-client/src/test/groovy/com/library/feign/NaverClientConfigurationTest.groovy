package com.library.feign

import feign.RequestTemplate
import spock.lang.Specification

class NaverClientConfigurationTest extends Specification {

    NaverClientConfiguration configuration

    void setup() {
        configuration = new NaverClientConfiguration()
    }

    def "requestInterceptordml header에 key값들이 잘 적용된다."() {
        given:
        def template = new RequestTemplate()
        def clientId = "id"
        def clientSecret = "secret"

        and: "interceptor를 실행하기 전에 header 가 존재하지 않는다."
        template.headers()["X-Naver-Client-Id"] == null
        template.headers()["X-Naver-Client-Secret"] == null

        when: "interceptor 실행"
        def interceptor = configuration.requestInterceptor(clientId, clientSecret)
        interceptor.apply(template)

        then: "interceptor 실행 이후 header가 추가된다."
        template.headers()["X-Naver-Client-Id"].contains(clientId)
        template.headers()["X-Naver-Client-Secret"].contains(clientSecret)
    }
}
