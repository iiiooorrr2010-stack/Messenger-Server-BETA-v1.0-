package com.example.demo.UnitModel.MessageModel;

import com.example.demo.MessageManagement.MessageRepository.MessageRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Entity
@Table(name = "text_message")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long mesId;
    @Size(min=1, max= 2000, message = "Сообщение вне лимита")
   private String textMessage;
   private Timestamp timestamp;
   @Column(nullable = false)
    @NotNull
   private String fromUser;
    @Column(nullable = false)
    @NotNull
    private String toUser;

}
