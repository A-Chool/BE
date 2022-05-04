package com.RoutineGongJakSo.BE.chat.repo;

import com.RoutineGongJakSo.BE.chat.model.ChatMessage;
import org.springframework.data.repository.CrudRepository;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {
}
