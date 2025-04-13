package com.library.service

import com.library.respository.BookRepository
import spock.lang.Specification

class BookServiceTest extends Specification {
    BookRepository bookRepository = Mock(BookRepository)

    BookQueryService bookQueryService

    void setup() {
        bookQueryService = new BookQueryService(bookRepository)
    }

    def "search시 인자가 그대로 넘어간다."() {
        given:
        def givenQuery = "HTTP"
        def givenPage = 1
        def givenSize = 1

        when:
        bookQueryService.search(givenQuery, givenPage, givenSize)

        then:
        // mocking 하기 때문에 인자가 잘 들어가는지 확인만.
        1 * bookRepository.search(*_) >> {
            String query, int page, int size ->
                assert query == givenQuery
                assert page == givenPage
                assert size == givenSize
        }
    }
}
