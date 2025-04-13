package com.library.service;

import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.controller.response.StatResponse;
import com.library.entity.DailyStat;
import com.library.respository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookApplicationService {
    final BookQueryService bookQueryService;
    final DailyStatCommandService dailyStatCommandService;
    final DailyStatQueryService dailyStatQueryService;

    public PageResult<SearchResponse> search(String query, int page, int size) {
        // 외부 api 호출 -> 통게 데이터 저장 -> API 호출값 응답.
        var response = bookQueryService.search(query, page, size);
        // 통계값이 저장이 될때 까지 사용자는 응답을 받아볼수 없는 문제.
        var dailyStat = new DailyStat(query, LocalDateTime.now());
        dailyStatCommandService.save(dailyStat);
        return response;
    }

    public StatResponse findQueryCount(String query, LocalDate date) {
        return dailyStatQueryService.findQueryCount(query, date);
    }

    public List<StatResponse> findTop5Query() {
        return dailyStatQueryService.findTop5Query();
    }
}
