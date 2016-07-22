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
public abstract class AdStat implements AdStatModel {

    public static final Factory<AdStat> FACTORY = new Factory<>((Creator<AdStat>) AutoValue_AdStat::new);

    public static final RowMapper<AdStat> MAPPER = FACTORY.select_allMapper();

}
