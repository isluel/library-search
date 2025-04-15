package com.library.service.event;

import com.library.entity.DailyStat;
import com.library.service.DailyStatCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SearchEventHandler {
    private final DailyStatCommandService dailyStatCommandService;

    @Async
    @EventListener
    public void handleEvent(SearchEvent searchEvent) {
        log.info("[SearchEventHandler] handleEvent: {}", searchEvent);
        var dailyStat = new DailyStat(searchEvent.query(), searchEvent.timestamp());
        dailyStatCommandService.save(dailyStat);
    }
}
