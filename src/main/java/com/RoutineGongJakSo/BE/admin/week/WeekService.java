package com.RoutineGongJakSo.BE.admin.week;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.RoutineGongJakSo.BE.admin.week.WeekValidator.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeekService {
    private final WeekRepository weekRepository;

    public List<WeekDto.ResponseDto> getAllWeek() {
        log.info("getAllWeek");

        List<Week> weekList = weekRepository.findAll();

        log.info("getAllWeek weekList " + weekList);

        List<WeekDto.ResponseDto> responseDtoList = new ArrayList<>();

        for(Week week : weekList){
            WeekDto.ResponseDto responseDto = new WeekDto.ResponseDto(week);
            responseDtoList.add(responseDto);
        }

        log.info("getAllWeek responseDtoList " + responseDtoList);
        return responseDtoList;
    }

    public WeekDto.ResponseDto createWeek(WeekDto.RequestDto requestDto) {
        log.info("createWeek");
        String weekName = requestDto.getWeekName();

        Optional<Week> found = weekRepository.findByWeekName(weekName);

        checkWeek(found);
        checkName(weekName);

        Week week = new Week(weekName);
        weekRepository.save(week);


        log.info("createWeek week " + week);

        return new WeekDto.ResponseDto(week);
    }
}
