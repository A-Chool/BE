package com.RoutineGongJakSo.BE.client.chat.model;

import com.RoutineGongJakSo.BE.util.Timestamped;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatFile extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @Column
    private String roomId;

    @Column
    private Long prevId;

    @Column
    private String fileUrl;

}
