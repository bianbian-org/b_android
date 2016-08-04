package com.techjumper.polyhome.b.home.sql;

import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;

/**
 * Created by kevin on 16/8/3.
 */
@AutoValue
public abstract class AdClick implements AdClickModel {

    public static final Factory<AdClick> FACTORY = new Factory<>(AutoValue_AdClick::new);

    public static final RowMapper<AdClick> MAPPER = FACTORY.select_by_adidMapper();
}
