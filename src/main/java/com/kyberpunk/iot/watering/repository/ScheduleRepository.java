package com.kyberpunk.iot.watering.repository;

import com.kyberpunk.iot.watering.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
