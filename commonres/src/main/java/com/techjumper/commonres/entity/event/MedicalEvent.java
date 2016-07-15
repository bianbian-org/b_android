package com.techjumper.commonres.entity.event;

import com.techjumper.commonres.entity.MedicalEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/7/15.
 */

public class MedicalEvent {

    private List<MedicalEntity.MedicalItemEntity> entities = new ArrayList<>();

    public MedicalEvent(List<MedicalEntity.MedicalItemEntity> entities) {
        this.entities = entities;
    }

    public List<MedicalEntity.MedicalItemEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<MedicalEntity.MedicalItemEntity> entities) {
        this.entities = entities;
    }
}
