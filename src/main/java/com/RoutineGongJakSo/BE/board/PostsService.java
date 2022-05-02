package com.RoutineGongJakSo.BE.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 Service
 - 주업무 코드 기술 Layer. Mapper호출, API호출, Biz로직 처리 등 기능 구현
 - session, request, response등 Web에 종속된 객체나 클리스 사용x
 * */
@Service
@RequiredArgsConstructor
public class PostsService {

   private final PostsRepository postsRepository;

   //전체조회
   public List<PostsResponseDto> findAll() {
      List<Posts> list = postsRepository.findAll();
      List<PostsResponseDto> postsResponseDtos = new ArrayList<>();
      for (Posts posts : list) {
         System.out.println(posts);
         PostsResponseDto postsResponseDto = new PostsResponseDto(posts);
         postsResponseDtos.add(postsResponseDto);
      }
      return postsResponseDtos;
   }

   //생성
   //@Transactional : 프록시객체 생성, 처리한 쿼리들 에러났을때 자동 rollback
   public Long save(PostsSaveRequestDto requestDto) {
      return postsRepository.save(requestDto.toEntity()).getId();
   }

   //변경
   //update시 repository에 query를 보내지 않는다. JPA의 엔티티를 영구저장하는 환경인 영속성 컨텍스트 때문이다.
   //해당 데이터의 값을 변경하면 트랜젝션이 끝나는 시점에 해당 테이블에 변경분을 반영한다. 이것을 더티체킹이라 한다.
   public Long update(Long id, PostsUpdateRequestDto requestDto) {
      Posts posts = postsRepository.findbyID(id).orElsethrow(() -> new IllegalArgumentException("Id not exist. id=" + id));
      posts.update(requestDto.getTitle(), requestDto.getModifiedAt(), requestDto.getDetail());
      return id;
   }

   //삭제
   @Transactional
   public void delete(Long id) {
      Posts posts = postsRepository.findById(id).orElsethrow(() -> new IllegalArgumentException("Id not exist. id=" + id));
      postsRepository.delete(posts);
   }
}



   //조회
   public PostsResponseDto findById(Long id) {
      Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id not exist. id=" + id));
      return new PostsResponseDto(posts);


   }
}
