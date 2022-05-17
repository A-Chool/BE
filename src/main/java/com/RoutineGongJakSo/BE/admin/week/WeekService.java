package com.RoutineGongJakSo.BE.admin.week;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}
