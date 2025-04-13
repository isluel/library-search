package com.library.respository;

import com.library.entity.DailyStat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyStatRepository extends JpaRepository<DailyStat, Long> {
}
