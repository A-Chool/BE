package com.RoutineGongJakSo.BE.teamBoard;

import com.RoutineGongJakSo.BE.admin.dto.TeamDto;
import com.RoutineGongJakSo.BE.admin.repository.MemberRepository;
import com.RoutineGongJakSo.BE.admin.repository.WeekTeamRepository;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//팀 대시보드
public class TeamBoardService {


    private final TeamBoardRepository teamBoardRepository;
    /*private final WeekTeamRepository weekTeamRepository;
    private final MemberRepository memberRepository;*/

    //워크 스페이스스
    public String savePost(TeamBoardDto teamBoardDto) {
        return (String) teamBoardRepository.save(teamBoardDto);
    }
    }


//    //주차 팀정보
//    public List<TeamDto.weekTeamDto> getWeekTeamList
//    }
//}
       /* //해당 주차의 모든 팀 조회
        public Map<String, Object> getTeamList(UserDetailsImpl userDetails, String week) {

            List<WeekTeam> weekTeamList = weekTeamRepository.findByWeek(week);

            //팀별 팀원 리스트
            Map<String, Object> weekMemberList = new HashMap<>();


            for (WeekTeam p : weekTeamList) {
                List<Member> findMember = memberRepository.findByWeekTeam(P);
                List<TeamDto.getUserList> userLists = new ArrayList<>();
                for (Member getResponse : findMember) {
                    TeamDto.getUserList userList = TeamDto.getUserList.builder()
                            .teamId(getResponse.getWeekTeam().getWeekTeamId())
                            .userId(getResponse.getUser().getUserId())
                            .userName(getResponse.getUser().getUserName())
                            .userEmail(getResponse.getUser().getUserEmail())
                            .phoneNumber(getResponse.getUser().getPhoneNumber())
                            .kakaoId(getResponse.getUser().getKakaoId())
                            .createdAt(getResponse.getUser().getCreatedAt())
                            .memberId(getResponse.getMemberId())
                            .build();

                    userLists.add(userList);
                }
                weekMemberList.put(p.getTeamName() + ":" + p.getWeekTeamId(), userLists);
            }
            return weekMemberList;
        }*/


//팀 정보 보기
//그라운드룰 수정
