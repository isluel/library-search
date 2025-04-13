package com.library.respository;

import com.library.Item;
import com.library.NaverBookResponse;
import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.feign.NaverClient;
import com.library.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class NaverBookRepository implements BookRepository {
    private final NaverClient naverClient;

    @Override
    public PageResult<SearchResponse> search(String query, int page, int size) {
        NaverBookResponse response = naverClient.search(query, page, size);
        var responses = response.getItems().stream()
                .map(this::createReponse)
                .toList();
        return new PageResult<>(page, size, response.getTotal(), responses);
    }

    private SearchResponse createReponse(Item item) {
        return SearchResponse.builder()
                .title(item.getTitle())
                .author(item.getAuthor())
                .publisher(item.getPublisher())
                .pubDate(DateUtils.parseYYYYMMDD(item.getPubDate()))
                .isbn(item.getIsbn())
                .build();

    }
}
