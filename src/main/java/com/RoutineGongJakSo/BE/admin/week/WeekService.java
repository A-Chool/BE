package com.RoutineGongJakSo.BE.admin.week;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        for (Week week : weekList) {
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

        checkWeekDuple(found);
        checkNameBlank(weekName);
        checkNameLength(weekName);

        Week week = new Week(weekName);
        weekRepository.save(week);


        log.info("createWeek week " + week);

        return new WeekDto.ResponseDto(week);
    }

    public void deleteWeek(Long weekId) {
        log.info("deleteWeek " + weekId);

        Optional<Week> found = weekRepository.findById(weekId);
        checkWeekPresent(found);
        Week target = found.get();

        checkWeekDisplay(target);
        log.info("deleteWeek target " + target);

        weekRepository.delete(target);
    }

    @Transactional
    public WeekDto.ResponseDto displayWeek(Long weekId) {
        log.info("displayWeek " + weekId);

        Optional<Week> found = weekRepository.findById(weekId);
        checkWeekPresent(found);
        Week target = found.get();

        Optional<Week> displayWeek = weekRepository.findByDisplay(true);
        if (displayWeek.isPresent()) {
            Week _displayWeek = displayWeek.get();
            _displayWeek.setDisplay(false);
            weekRepository.save(_displayWeek);
        }

        log.info("displayWeek target " + target);

        target.setDisplay(true);
        weekRepository.save(target);

        return new WeekDto.ResponseDto(target);
    }

    public WeekDto.ResponseDto changeWeekName(Long weekId, WeekDto.RequestDto requestDto) {
        log.info("changeWeekName");
        String weekName = requestDto.getWeekName();
        Optional<Week> foundById = weekRepository.findById(weekId);
        Optional<Week> foundByName = weekRepository.findByWeekName(weekName);

        checkWeekPresent(foundById);
        checkWeekDuple(foundByName);
        checkNameBlank(weekName);
        checkNameLength(weekName);

        Week week = foundById.get();
        week.setWeekName(requestDto.getWeekName());
        weekRepository.save(week);

        log.info("changeWeekName week " + week);

        return new WeekDto.ResponseDto(week);

    }
}
