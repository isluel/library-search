package com.library.service

import com.library.controller.response.PageResult
import com.library.respository.KakaoBookRepository
import com.library.respository.NaverBookRepository
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

// 메번 Context 를 비우는 annotation. 비우지 않으면 circuit이 전체로 돌음.
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@ActiveProfiles("test")
class BookQueryServiceItTest extends Specification {
    @Autowired
    BookQueryService bookQueryService;

    // Circuit Breaker의 상태 검증을 위한 Registry 추가
    @Autowired
    CircuitBreakerRegistry circuitBreakerRegistry;

    @SpringBean
    KakaoBookRepository kakaoBookRepository = Mock()
    @SpringBean
    NaverBookRepository naverBookRepository = Mock()

    def "정상 상황에서는 Circuit의 상태가 close 이고 naver 쪽으로 호출이 들아간다."() {
        given:
        def keyword = 'HTTP'
        def page = 1
        def size = 10

        when:
        bookQueryService.search(keyword, page, size)

        then:
        1 * naverBookRepository.search(keyword, page, size) >> new PageResult<>(1, 10, 0, [])

        and:
        def circuitBreaker = circuitBreakerRegistry.getAllCircuitBreakers().stream().findFirst().get()
        circuitBreaker.state == CircuitBreaker.State.CLOSED

        and:
        0 * kakaoBookRepository.search(*_)
    }

    def "circuit 이 open 되면 kakao 쪽으로 요청을 한다."() {
        given:
        def keyword = "HTTP"
        def page = 1
        def size = 10
        def kakaoResponse = new PageResult<>(1, 10, 0, [])

        // Cirucit이 바로 열릴수 있도록 config를 수정한다.
        def config = CircuitBreakerConfig.custom()
                .slidingWindowSize(1)
                .minimumNumberOfCalls(1)
                .failureRateThreshold(50)
                .build()
        circuitBreakerRegistry.circuitBreaker("naverSearch", config)

        and: "naver 쪽으로는 항상 예외가 발생하도록 한다."
        naverBookRepository.search(keyword, page, size) >> {throw new RuntimeException("ERROR!") }

        when:
        def result = bookQueryService.search(keyword, page, size)

        then: "kakako 쪽으로 Fallback 된다."
        1 * kakaoBookRepository.search(keyword, page, size) >> kakaoResponse

        and: "Circuit이 Open 된다."
        def circuitBreaker = circuitBreakerRegistry.getAllCircuitBreakers().stream().findFirst().get()
        circuitBreaker.state == CircuitBreaker.State.OPEN

        and:
        result == kakaoResponse
    }
}
