package com.RoutineGongJakSo.BE.client.checkIn;

import com.RoutineGongJakSo.BE.client.checkIn.model.Analysis;
import com.RoutineGongJakSo.BE.client.checkIn.model.CheckIn;
import com.RoutineGongJakSo.BE.client.checkIn.repository.AnalysisRepository;
import com.RoutineGongJakSo.BE.client.checkIn.repository.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CronTable {

    private final CheckInRepository checkInRepository;
    private final AnalysisRepository analysisRepository;
    private final CheckInValidator checkInValidator;

    @Scheduled(cron = "0 00 5 * * *") // 매일 오전 5시에 실행
    @Transactional
    public void reset() throws ParseException {

        String date = LocalDate.now(ZoneId.of("Asia/Seoul")).toString(); // 현재 서울 날짜

        Calendar today = checkInValidator.todayCalender(date); //현재 시간 기준 날짜
        checkInValidator.todayCalendarTime(today); // String yyyy-MM-dd HH:mm:ss 현재시간
        today.add(Calendar.DATE, -1);

        String setToday = checkInValidator.DateFormat(today); //Date -> String 변환

        List<CheckIn> checkInList = checkInRepository.findByDate(setToday); //전 일에 해당하는 모든 리스트

        for (CheckIn check : checkInList){
            String[] lastCheck = check.getCheckIn().split(":");
            int lastTime = Integer.parseInt(lastCheck[0]);

            if (lastTime >= 4){
                checkInRepository.delete(check);
            }

            if (check.getCheckOut() == null){
                int setH = Integer.parseInt(lastCheck[0]) + 1;
                String reSetCheckOut = setH + ":" + lastCheck[1] + ":" + lastCheck[2];
                check.setCheckOut(reSetCheckOut);
                checkInRepository.save(check);

                Optional<Analysis> analysis = analysisRepository.findByUserAndDate(check.getUser(), check.getDate());

                if (!analysis.isPresent()){
                    //체크아웃 기록이 아예 없어서 analysis 테이블이 null이라면,
                    Analysis setAnalysis = Analysis.builder()
                            .daySum("01:00:00")
                            .date(setToday)
                            .user(check.getUser())
                            .build();
                    analysisRepository.save(setAnalysis);
                    check.setAnalysis(setAnalysis);
                    checkInRepository.save(check);
                }else { //이미 체크아웃 한 기록이 있지만, 마지막 체크인 후 체크아웃을 하지 않은 경우 시간 초기화용
                    String[] setTime = analysis.get().getDaySum().split(":");
                    int reH = Integer.parseInt(setTime[0]) + 1;
                    String setDaySum =  reH + ":" + setTime[1] + ":" + setTime[2];
                    analysis.get().setDaySum(setDaySum);
                    analysisRepository.save(analysis.get());
                }
            }
            checkInRepository.deleteAll(checkInList);
        }

    }
}
