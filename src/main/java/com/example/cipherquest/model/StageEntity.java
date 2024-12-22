package com.example.cipherquest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames="id")})

public class StageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="stage_id")
    private Long stageId;

    private String description;
    private String encryptedText;
    private String answer;
}
