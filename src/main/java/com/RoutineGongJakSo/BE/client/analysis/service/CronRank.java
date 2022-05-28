package com.RoutineGongJakSo.BE.client.analysis.service;

import com.RoutineGongJakSo.BE.client.analysis.model.Analysis;
import com.RoutineGongJakSo.BE.client.analysis.repository.AnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CronRank {
    private final AnalysisRepository analysisRepository;
    private final RedisTemplate<String, String> redisTemplate;

    //    @Scheduled(cron = "0 00 5 * * *") // 매일 오전 5시에 실행
    @Transactional
    public void updateRank() {
        String key = "rank";
//        ZSetOperations<String, String> ZSetOperation = redisTemplate.opsForZSet();

        Date today = new Date();
        String todayString = today.toString();
        System.out.println("todayString = " + todayString);
//        List<Analysis> analyseList = analysisRepository.findAllByDate();

//        ZSetOperation.add(key, "id1", 123);
    }

    //    @Scheduled(cron = "0 00 5 * * MON") // 매주 월요일 오전 5시에 실행
    @Transactional
    public void resetRank() {
        String key = "rank";
        ZSetOperations<String, String> ZSetOperation = redisTemplate.opsForZSet();
        ZSetOperation.remove(key);
    }
}
