package com.techjumper.polyhome_b.adlib.entity.sql;

import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/7/22
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@AutoValue
public abstract class AdStatTime implements AdStatTimeModel {

    public static final Factory<AdStatTime> FACTORY = new Factory<>((Creator<AdStatTime>) AutoValue_AdStatTime::new);

    public static final RowMapper<AdStatTime> MAPPER = FACTORY.select_allMapper();

}
