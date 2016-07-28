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

import com.techjumper.commonres.entity.NoticeEntity;
import com.techjumper.commonres.entity.event.NoticeEvent;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.mvp.p.fragment.PloyhomeFragmentPresenter;
import com.techjumper.polyhome.b.home.widget.MyTextureView;
import com.techjumper.polyhome.b.home.widget.SquareView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
@Presenter(PloyhomeFragmentPresenter.class)
public class PloyhomeFragment extends AppBaseFragment<PloyhomeFragmentPresenter> {

    @Bind(R.id.fp_temperature)
    SquareView fpTemperature;
    @Bind(R.id.fp_restrict)
    SquareView fpRestrict;
    @Bind(R.id.notice_num)
    TextView noticeNum;
    @Bind(R.id.notice_title)
    TextView noticeTitle;
    @Bind(R.id.notice_content)
    TextView noticeContent;
    @Bind(R.id.image_ad)
    ImageView ad;
    @Bind(R.id.video_ad)
    MyTextureView textureView;
    @Bind(R.id.jujia)
    SquareView jujia;
    @Bind(R.id.smarthome)
    SquareView smarthome;
    @Bind(R.id.property)
    SquareView property;
    @Bind(R.id.notice_layout)
    RelativeLayout noticeLayout;
    @Bind(R.id.shopping)
    SquareView shopping;
    @Bind(R.id.ad)
    FrameLayout ad_layout;

    private Timer timer = new Timer();
    private int position = 0;
    private List<NoticeEntity.Unread> unreads = new ArrayList<>();

    public TextView getNoticeTitle() {
        return noticeTitle;
    }

    public TextView getNoticeContent() {
        return noticeContent;
    }

    public SquareView getFpTemperature() {
        return fpTemperature;
    }

    public SquareView getFpRestrict() {
        return fpRestrict;
    }

    public ImageView getAd() {
        return ad;
    }

    public MyTextureView getTextureView() {
        return textureView;
    }

    public static PloyhomeFragment getInstance() {
        return new PloyhomeFragment();
    }

    public List<NoticeEntity.Unread> getUnreads() {
        return unreads;
    }

    public SquareView getProperty() {
        return property;
    }

    public SquareView getShopping() {
        return shopping;
    }

    public RelativeLayout getNoticeLayout() {
        return noticeLayout;
    }

    public SquareView getSmarthome() {
        return smarthome;
    }

    public SquareView getJujia() {
        return jujia;
    }

    public FrameLayout getAd_layout() {
        return ad_layout;
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ployhome, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    public void initNotices(NoticeEntity.NoticeDataEntity entity) {
        if (entity == null)
            return;

        if (timer != null) {
            timer.cancel();
        }

        if (unreads != null && unreads.size() > 0)
            unreads.clear();

        unreads = entity.getUnread();
        if (unreads != null) {
            int num = 0;
            if (unreads.size() > 0) {
                for (int i = 0; i < unreads.size(); i++) {
                    num += unreads.get(i).getCount();
                }
            }
            noticeNum.setText(String.valueOf(num));
        }

        List<NoticeEntity.Message> messages = entity.getMessages();
        position = 0;

        if (messages != null && messages.size() > 0) {
            if (messages.size() == 1) {
                NoticeEntity.Message message = messages.get(0);
                RxBus.INSTANCE.send(new NoticeEvent(message.getTitle(), message.getContent(), message.getTypes()));
            } else {
                timer = new Timer();

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        NoticeEntity.Message message = messages.get(position);

                        RxBus.INSTANCE.send(new NoticeEvent(message.getTitle(), message.getContent(), message.getTypes()));

                        if (position == messages.size() - 1) {
                            position = 0;
                        } else {
                            position++;
                        }
                    }
                }, 1000, 3000);
            }
        } else {
            RxBus.INSTANCE.send(new NoticeEvent(getString(R.string.info_not_new), "", -1));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
