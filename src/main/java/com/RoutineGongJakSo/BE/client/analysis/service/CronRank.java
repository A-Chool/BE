package com.RoutineGongJakSo.BE.client.analysis.service;

import com.RoutineGongJakSo.BE.client.analysis.dto.RankRedisDto;
import com.RoutineGongJakSo.BE.client.analysis.model.Analysis;
import com.RoutineGongJakSo.BE.client.analysis.repository.AnalysisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;

import static com.RoutineGongJakSo.BE.util.CalendarUtil.DateFormat;
import static com.RoutineGongJakSo.BE.util.CalendarUtil.todayCalender;

@Service
@Slf4j
@RequiredArgsConstructor
public class CronRank {
    private final AnalysisRepository analysisRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Scheduled(cron = "0 15 5 * * *") // 매일 오전 5시에 실행
    @Transactional
    public void updateRank() throws ParseException {
        log.info("updateRank");

        String key = "rank";
        ZSetOperations<String, Object> ZSetOperation = redisTemplate.opsForZSet();

        String date = (LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1)).toString(); // 현재 서울 날짜 에서 하루 빼야함
        Calendar today = todayCalender(date); //현재 시간 기준 날짜
        String dateFormat = DateFormat(today);

        log.info("날짜 : {}", dateFormat);
        List<Analysis> todayAnalysisList = analysisRepository.findAllByDate(dateFormat);

        for (Analysis _analysis : todayAnalysisList) {
            ZSetOperation.incrementScore(key, new RankRedisDto(_analysis), toTime(_analysis.getDaySum()));
        }
    }

    public void updateRankForce() throws ParseException {
        log.info("updateRankForce");

        String key = "rank";
        ZSetOperations<String, Object> ZSetOperation = redisTemplate.opsForZSet();

        String date = (LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1)).toString(); // 현재 서울 날짜 에서 하루 빼야함
        Calendar today = todayCalender(date); //현재 시간 기준 날짜
        String dateFormat = DateFormat(today);

        log.info("날짜 : {}", dateFormat);
        List<Analysis> todayAnalysisList = analysisRepository.findAllByDate(dateFormat);

        for (Analysis _analysis : todayAnalysisList) {
            ZSetOperation.incrementScore(key, new RankRedisDto(_analysis), toTime(_analysis.getDaySum()));
        }
    }

    @Scheduled(cron = "0 00 5 * * MON") // 매주 월요일 오전 5시에 실행
    @Transactional
    public void resetRank() {
        log.info("resetRank");
        String key = "rank";
        redisTemplate.delete(key);
    }

    public void resetRankForce() {
        log.info("resetRankForce");
        String key = "rank";
        redisTemplate.delete(key);
    }

    // string -> millisec 로 변환
    private double toTime(String daySum) {
        String[] splitDaySum = daySum.split(":");
        String hour = splitDaySum[0];
        String min = splitDaySum[1];
        String sec = splitDaySum[2];

        return Integer.parseInt(hour) * 60 * 60 + Integer.parseInt(min) * 60 + Integer.parseInt(sec);
    }
}
