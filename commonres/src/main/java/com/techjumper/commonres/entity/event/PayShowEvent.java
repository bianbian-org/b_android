package com.techjumper.commonres.entity.event;

import com.techjumper.commonres.entity.PayEntity;

/**
 * Created by kevin on 16/11/30.
 */

public class PayShowEvent {
    private PayEntity.PayItemEntity itemEntity;

    public PayShowEvent(PayEntity.PayItemEntity itemEntity) {
        this.itemEntity = itemEntity;
    }

    public PayEntity.PayItemEntity getItemEntity() {
        return itemEntity;
    }

    public void setItemEntity(PayEntity.PayItemEntity itemEntity) {
        this.itemEntity = itemEntity;
    }
}
