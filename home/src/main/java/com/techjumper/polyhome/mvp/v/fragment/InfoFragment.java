package com.techjumper.polyhome.mvp.v.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.techjumper.commonres.entity.CalendarEntity;
import com.techjumper.commonres.entity.WeatherEntity;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhome.R;
import com.techjumper.polyhome.mvp.p.fragment.InfoFragmentPresenter;
import com.techjumper.polyhome.utils.StringUtil;
import com.techjumper.polyhome.widget.AlmanacView;
import com.techjumper.polyhome.widget.WeatherView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
@Presenter(InfoFragmentPresenter.class)
public class InfoFragment extends AppBaseFragment<InfoFragmentPresenter> {

    @Bind(R.id.liw_icon)
    ImageView liwIcon;
    @Bind(R.id.liw_temperature)
    TextView liwTemperature;
    @Bind(R.id.liw_info)
    TextView liwInfo;
    @Bind(R.id.liw_sub_temperature)
    TextView liwSubTemperature;
    @Bind(R.id.liww_first)
    WeatherView liwwFirst;
    @Bind(R.id.liww_second)
    WeatherView liwwSecond;
    @Bind(R.id.liww_third)
    WeatherView liwwThird;
    @Bind(R.id.liww_fourth)
    WeatherView liwwFourth;
    @Bind(R.id.liw_running)
    TextView liwRunning;
    @Bind(R.id.liw_pm)
    TextView liwPm;
    @Bind(R.id.liw_quality_description)
    TextView liwQualityDescription;
    @Bind(R.id.liw_quality_num)
    TextView liwQualityNum;
    @Bind(R.id.lid_date)
    TextView lidDate;
    @Bind(R.id.lid_lunar)
    TextView lidLunar;
    @Bind(R.id.lid_almanac_ok)
    AlmanacView lidAlmanacOk;
    @Bind(R.id.lid_almanac_nope)
    AlmanacView lidAlmanacNope;

    public static InfoFragment getInstance() {
        return new InfoFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    public void getWeatherInfo(WeatherEntity.WeatherDataEntity weatherDataEntity) {
        // TODO: 16/4/28 有些数据需要微调，比如空起质量，适合跑步等 
        liwTemperature.setText(weatherDataEntity.getTemperature());
        liwInfo.setText(weatherDataEntity.getWeather_info());
        liwSubTemperature.setText(StringUtil.addSeparator(weatherDataEntity.getTemperature_low(), weatherDataEntity.getTemperature_hight()));
        liwPm.setText(weatherDataEntity.getPm25());
        liwQualityDescription.setText(weatherDataEntity.getQuality());

        liwwFirst.setDate(weatherDataEntity.getDate_one());
        liwwFirst.setTemperature(weatherDataEntity.getTemperature_one());
        liwwSecond.setDate(weatherDataEntity.getWeather_two());
        liwwSecond.setTemperature(weatherDataEntity.getTemperature_two());
        liwwThird.setDate(weatherDataEntity.getDate_three());
        liwwThird.setTemperature(weatherDataEntity.getTemperature_three());
        liwwFourth.setDate(weatherDataEntity.getDate_four());
        liwwFourth.setTemperature(weatherDataEntity.getTemperature_four());
    }

    public void getCalendarInfo(CalendarEntity.CalendarDataEntity calendarDataEntity) {
        // TODO: 16/4/28 有些数据需要微调
        lidDate.setText(calendarDataEntity.getDate());
        lidLunar.setText(calendarDataEntity.getLunarYear() + calendarDataEntity.getLunar());

        List<String> suits = StringUtil.interceptString(calendarDataEntity.getSuit(), ".");
        List<String> avoids = StringUtil.interceptString(calendarDataEntity.getAvoid(), ".");
        lidAlmanacOk.setTexts(suits);
        lidAlmanacNope.setTexts(avoids);
    }
}
