package com.techjumper.polyhome.b.home.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.utils.WeatherUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kevin on 16/4/22.
 */
public class WeatherView extends RelativeLayout {
    @Bind(R.id.lw_date)
    TextView lwDate;
    @Bind(R.id.lw_temperature)
    TextView lwTemperature;
    @Bind(R.id.lw_img)
    ImageView lwImg;

    private String dateString;
    private String temperatureString;

    public WeatherView(Context context) {
        super(context);
    }

    public WeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_weather, this, true);
        ButterKnife.bind(view, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WeatherView);
        dateString = typedArray.getString(R.styleable.WeatherView_weather_date);
        temperatureString = typedArray.getString(R.styleable.WeatherView_weather_temperature);

        setDate(dateString);
        setTemperature(temperatureString);

        typedArray.recycle();
    }

    public void setDate(String date) {
        lwDate.setText(date == null ? "" : date);
    }

    public void setTemperature(String temperature) {
        lwTemperature.setText(temperature == null ? "" : temperature);
    }

    public void setImg(String imgType) {
       lwImg.setBackgroundResource(WeatherUtil.getImgRes(imgType));
    }
}
