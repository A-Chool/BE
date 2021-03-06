package com.RoutineGongJakSo.BE.client.chat.repo;

import com.RoutineGongJakSo.BE.client.chat.model.ChatFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatFileRepository extends JpaRepository<ChatFile, Long> {

    List<ChatFile> findByRoomId(String roomId);

    List<ChatFile> findAllByRoomId(String roomId);

    ChatFile findByPrevId(Long prevId);

    Optional<ChatFile> findByFileIdAndRoomId(Long id, String roomId);
}
