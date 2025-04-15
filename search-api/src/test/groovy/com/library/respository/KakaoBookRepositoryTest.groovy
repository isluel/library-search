package com.library.respository

import com.library.Document
import com.library.KakaoBookResponse
import com.library.Meta
import com.library.feign.KakaoClient
import spock.lang.Specification

import java.time.LocalDate

class KakaoBookRepositoryTest extends Specification {
    BookRepository bookRepository

    KakaoClient kakaoClient = Mock()

    void setup() {
        bookRepository = new KakaoBookRepository(kakaoClient)
    }

    def "search 호출 시 적절한 데이터 형식으로 변환"() {
        given:
        def documents = [
                new Document("제목1", ["저자"], "출판사", "isbn", "2016-02-01T00:00:00.000+09:00"),
                new Document("제목2", ["저자2"], "출판사2", "isbn2", "2016-02-01T00:00:00.000+09:00"),
        ]
        def meta = new Meta(false, 1, 10)
        def response = new KakaoBookResponse(documents, meta)
        def givenQuery = "HTTP"
        def givenPage = 1
        def givenSize = 2

        and:
        1 * kakaoClient.search(givenQuery, givenPage, givenSize) >> response

        when:
        def result = bookRepository.search(givenQuery, givenPage, givenSize)

        then:
        verifyAll(result) {
            size() == givenSize
            page() == givenPage
            totalElements() == 10
            contents().size() == 2
            contents().get(0).pubDate() == LocalDate.of(2024, 1, 1)
        }
    }
}
