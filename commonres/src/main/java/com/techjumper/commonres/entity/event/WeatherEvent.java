package com.techjumper.commonres.entity.event;

import com.techjumper.commonres.entity.WeatherEntity;

/**
 * Created by kevin on 16/6/16.
 */
public class WeatherEvent {

    private WeatherEntity.WeatherDataEntity weatherEntity;

    public WeatherEvent(WeatherEntity.WeatherDataEntity weatherEntity) {
        this.weatherEntity = weatherEntity;
    }

    public WeatherEntity.WeatherDataEntity getWeatherEntity() {
        return weatherEntity;
    }

    public void setWeatherEntity(WeatherEntity.WeatherDataEntity weatherEntity) {
        this.weatherEntity = weatherEntity;
    }
}
