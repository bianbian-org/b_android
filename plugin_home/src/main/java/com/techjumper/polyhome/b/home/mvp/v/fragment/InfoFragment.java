package com.techjumper.polyhome.b.home.mvp.v.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.techjumper.commonres.entity.CalendarEntity;
import com.techjumper.commonres.entity.WeatherEntity;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.mvp.p.fragment.InfoFragmentPresenter;
import com.techjumper.polyhome.b.home.utils.DateUtil;
import com.techjumper.polyhome.b.home.utils.StringUtil;
import com.techjumper.polyhome.b.home.utils.WeatherUtil;
import com.techjumper.polyhome.b.home.widget.AlmanacView;
import com.techjumper.polyhome.b.home.widget.ArcDataView;
import com.techjumper.polyhome.b.home.widget.MyTextureView;
import com.techjumper.polyhome.b.home.widget.SquareView;
import com.techjumper.polyhome.b.home.widget.WeatherView;

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
    @Bind(R.id.lid_date)
    TextView lidDate;
    @Bind(R.id.lid_lunar)
    TextView lidLunar;
    @Bind(R.id.lid_almanac_ok)
    AlmanacView lidAlmanacOk;
    @Bind(R.id.lid_almanac_nope)
    AlmanacView lidAlmanacNope;
    @Bind(R.id.lid_week)
    TextView lidWeek;
    @Bind(R.id.video_ad_tem)
    MyTextureView textureViewTem;
    @Bind(R.id.image_ad_tem)
    ImageView imageAdTem;
    @Bind(R.id.adv_heartrate)
    ArcDataView advHeartrate;
    @Bind(R.id.adv_bloodsugar)
    ArcDataView advBloodsugar;
    @Bind(R.id.adv_bloodpressure)
    ArcDataView advBloodpressure;
    @Bind(R.id.adv_detect)
    ArcDataView advDetect;
    @Bind(R.id.detect_layout)
    RelativeLayout detectLayout;
    @Bind(R.id.speak)
    SquareView speak;
    @Bind(R.id.setting)
    SquareView setting;
    @Bind(R.id.info_arrow_layout)
    FrameLayout infoArrowLayout;
    @Bind(R.id.ad_tem)
    FrameLayout adTem;

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

    public MyTextureView getTextureViewTem() {
        return textureViewTem;
    }

    public ImageView getImageAdTem() {
        return imageAdTem;
    }

    public ArcDataView getAdvDetect() {
        return advDetect;
    }

    public ArcDataView getAdvHeartrate() {
        return advHeartrate;
    }

    public ArcDataView getAdvBloodsugar() {
        return advBloodsugar;
    }

    public ArcDataView getAdvBloodpressure() {
        return advBloodpressure;
    }

    public FrameLayout getAdTem() {
        return adTem;
    }

    public SquareView getSetting() {
        return setting;
    }

    public RelativeLayout getDetectLayout() {
        return detectLayout;
    }

    public SquareView getSpeak() {
        return speak;
    }

    public void getWeatherInfo(WeatherEntity.WeatherDataEntity weatherDataEntity) {
        liwIcon.setBackgroundResource(WeatherUtil.getImgRes(weatherDataEntity.getImg()));
        liwTemperature.setText(weatherDataEntity.getTemperature() + "°");
        liwInfo.setText(weatherDataEntity.getWeather_info());
        liwSubTemperature.setText(StringUtil.addSeparator(weatherDataEntity.getTemperature_low(), weatherDataEntity.getTemperature_hight()));
        liwPm.setText(weatherDataEntity.getPm25());
        liwQualityDescription.setText(weatherDataEntity.getQuality());

        liwwFirst.setDate(weatherDataEntity.getDate_one());
        liwwFirst.setTemperature(weatherDataEntity.getTemperature_one());
        liwwFirst.setImg(weatherDataEntity.getImg_one());

        liwwSecond.setDate(weatherDataEntity.getDate_two());
        liwwSecond.setTemperature(weatherDataEntity.getTemperature_two());
        liwwSecond.setImg(weatherDataEntity.getImg_two());

        liwwThird.setDate(weatherDataEntity.getDate_three());
        liwwThird.setTemperature(weatherDataEntity.getTemperature_three());
        liwwThird.setImg(weatherDataEntity.getImg_three());

        liwwFourth.setDate(weatherDataEntity.getDate_four());
        liwwFourth.setTemperature(weatherDataEntity.getTemperature_four());
        liwwFourth.setImg(weatherDataEntity.getImg_four());

        lidWeek.setText("星期" + DateUtil.getDate(weatherDataEntity.getDate_one()).substring(1, 2));
    }

    public void getCalendarInfo(CalendarEntity.CalendarDataEntity calendarDataEntity) {
        lidDate.setText(DateUtil.formatDate(calendarDataEntity.getDate()));
        lidLunar.setText(calendarDataEntity.getLunarYear() + calendarDataEntity.getAnimalsYear() + "年" + calendarDataEntity.getLunar());

        List<String> suits = StringUtil.interceptString(calendarDataEntity.getSuit(), ".");
        List<String> avoids = StringUtil.interceptString(calendarDataEntity.getAvoid(), ".");
        lidAlmanacOk.setTexts(suits);
        lidAlmanacNope.setTexts(avoids);
    }
}
