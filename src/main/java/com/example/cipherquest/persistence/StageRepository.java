package com.example.cipherquest.persistence;

import com.example.cipherquest.model.StageEntity;
import org.springframework.data.repository.CrudRepository;

public interface StageRepository extends CrudRepository<StageEntity, Long> {
    StageEntity findByStageId(Long stageId);
    Boolean existsByAnswer(String input);
}
