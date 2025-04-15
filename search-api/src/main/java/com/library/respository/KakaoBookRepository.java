package com.library.respository;

import com.library.Document;
import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.feign.KakaoClient;
import com.library.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class KakaoBookRepository implements BookRepository{
    private final KakaoClient kakaoClient;

    @Override
    public PageResult<SearchResponse> search(String query, int page, int size) {
        var response = kakaoClient.search(query, page, size);

        var responses = response.documents().stream()
                .map(this::createResponse)
                .toList();

        return new PageResult<>(page, size, response.meta().totalCount(), responses);
    }

    private SearchResponse createResponse(Document document) {
        return SearchResponse.builder()
                .title(document.title())
                .author(document.authors().get(0))
                .publisher(document.publisher())
                .pubDate(DateUtils.parseOffsetDateTime(document.datetime()).toLocalDate())
                .isbn(document.isbn())
                .build();
    }
}
