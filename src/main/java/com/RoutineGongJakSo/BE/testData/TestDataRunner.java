package com.RoutineGongJakSo.BE.testData;


//import com.RoutineGongJakSo.BE.admin.team.WeekTeamRepository;
//import com.RoutineGongJakSo.BE.admin.team.TeamService;
//import com.RoutineGongJakSo.BE.admin.team.WeekTeam;
//import com.RoutineGongJakSo.BE.user.UserRepository;
//import com.RoutineGongJakSo.BE.user.JoinDto;
//import com.RoutineGongJakSo.BE.user.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.UUID;
//
//@Component
//public class TestDataRunner implements ApplicationRunner {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private WeekTeamRepository weekTeamRepository;
//    @Autowired
//    private TeamService teamService;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        // 테스트 User 생성
//        JoinDto joinDto0 = new JoinDto();
//        joinDto0.setEmail("0@0.com");
//        joinDto0.setUserName("zz");
//        joinDto0.setUserPw("1q2w3e4r");
//        joinDto0.setUserPwCheck("1q2w3e4r");
//        joinDto0.setPhoneNumber("010-0101-0101");
//
//        userService.join(joinDto0);
//        JoinDto joinDto1 = new JoinDto();
//        joinDto1.setEmail("1@1.com");
//        joinDto1.setUserName("zz");
//        joinDto1.setUserPw("1q2w3e4r");
//        joinDto1.setUserPwCheck("1q2w3e4r");
//        joinDto1.setPhoneNumber("010-0101-0101");
//
//        userService.join(joinDto1);
//        JoinDto joinDto2 = new JoinDto();
//        joinDto2.setEmail("2@2.com");
//        joinDto2.setUserName("zzz");
//        joinDto2.setUserPw("1q2w3e4r");
//        joinDto2.setUserPwCheck("1q2w3e4r");
//        joinDto2.setPhoneNumber("010-0101-0101");
//
//        userService.join(joinDto2);
//        JoinDto joinDto3 = new JoinDto();
//        joinDto3.setEmail("3@3.com");
//        joinDto3.setUserName("zzzz");
//        joinDto3.setUserPw("1q2w3e4r");
//        joinDto3.setUserPwCheck("1q2w3e4r");
//        joinDto3.setPhoneNumber("010-0101-0101");
//
//        userService.join(joinDto3);
//        JoinDto joinDto4 = new JoinDto();
//        joinDto4.setEmail("4@4.com");
//        joinDto4.setUserName("zzzzz");
//        joinDto4.setUserPw("1q2w3e4r");
//        joinDto4.setUserPwCheck("1q2w3e4r");
//        joinDto4.setPhoneNumber("010-0101-0101");
//
//        userService.join(joinDto4);
//
//
//        WeekTeam weekTeam1 = WeekTeam.builder()
//                .teamName("장미반")
//                .week("1주차")
//                .groundRole("")
//                .workSpace("")
//                .roomId(UUID.randomUUID().toString()) //1주차 1조
//                .build();
//
//        weekTeamRepository.save(weekTeam1);
//        WeekTeam weekTeam2 = WeekTeam.builder()
//                .teamName("떡잎반")
//                .week("1주차")
//                .groundRole("")
//                .workSpace("")
//                .roomId(UUID.randomUUID().toString()) //1주차 1조
//                .build();
//
//        weekTeamRepository.save(weekTeam2);
//        WeekTeam weekTeam3 = WeekTeam.builder()
//                .teamName("장미반")
//                .week("2주차")
//                .groundRole("")
//                .workSpace("")
//                .roomId(UUID.randomUUID().toString()) //1주차 1조
//                .build();
//
//        weekTeamRepository.save(weekTeam3);
//        WeekTeam weekTeam4 = WeekTeam.builder()
//                .teamName("떡잎반")
//                .week("2주차")
//                .groundRole("")
//                .workSpace("")
//                .roomId(UUID.randomUUID().toString()) //1주차 1조
//                .build();
//
//        weekTeamRepository.save(weekTeam4);
//
//
//    }
//}
