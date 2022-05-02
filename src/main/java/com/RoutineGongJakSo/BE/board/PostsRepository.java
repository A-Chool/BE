package com.RoutineGongJakSo.BE.board;

import org.springframework.data.jpa.repository.JpaRepository;

//MyBatis에서 Dao라고 불리는 DB Layer 접근자
//JPA에서는 Repository라고 부르며, interface로 생성한다.
//<Entity 클래스, PK타입>
//기본적인 CRUD 메소드가 자동으로 생성된다.
//@Repository를 추가할 필요도 없다.
//주의: Entity 클래스와 Entity Repository는 반드시 함께 위치해야 한다.
public interface PostsRepository extends JpaRepository<Posts, Long> {

}
