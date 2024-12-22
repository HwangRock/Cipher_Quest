package com.example.cipherquest.persistence;

import com.example.cipherquest.model.StageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StageRepository extends JpaRepository<StageEntity, Long> {
    StageEntity findByStageId(Long stageId);
    Boolean existsByAnswer(String input);
}
