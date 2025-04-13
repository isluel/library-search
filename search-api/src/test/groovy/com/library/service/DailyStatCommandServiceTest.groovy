package com.library.service

import com.library.entity.DailyStat
import com.library.respository.DailyStatRepository
import spock.lang.Specification

import java.time.LocalDateTime

class DailyStatCommandServiceTest extends Specification {
    DailyStatCommandService dailyStatCommandService

    DailyStatRepository dailyStatRepository = Mock(DailyStatRepository)

    void setup() {
        dailyStatCommandService = new DailyStatCommandService(dailyStatRepository)
    }


    def "저장시 넘어온 인자 그대로 호출된다."() {
        given:
        def givenDailStat = new DailyStat(query: "HTTP", eventDateTime: LocalDateTime.now())

        when:
        dailyStatCommandService.save(givenDailStat)

        then:
        1 * dailyStatRepository.save(*_) >> {
            DailyStat dailyStat ->
                assert dailyStat.query == givenDailStat.query
                assert dailyStat.eventDateTime == givenDailStat.eventDateTime
        }
    }

}
