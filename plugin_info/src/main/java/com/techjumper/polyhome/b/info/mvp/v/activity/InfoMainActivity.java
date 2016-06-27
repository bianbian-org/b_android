package com.techjumper.polyhome.b.info.mvp.v.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.steve.creact.library.adapter.CommonRecyclerAdapter;
import com.steve.creact.library.display.DisplayBean;
import com.techjumper.commonres.ComConstant;
import com.techjumper.commonres.PluginConstant;
import com.techjumper.commonres.UserInfoEntity;
import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.commonres.entity.NoticeEntity;
import com.techjumper.commonres.entity.event.BackEvent;
import com.techjumper.commonres.entity.event.InfoDetailEvent;
import com.techjumper.commonres.entity.event.InfoTypeEvent;
import com.techjumper.commonres.entity.event.PropertyNormalDetailEvent;
import com.techjumper.commonres.entity.event.loadmoreevent.LoadmoreInfoEvent;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.info.R;
import com.techjumper.polyhome.b.info.UserInfoManager;
import com.techjumper.polyhome.b.info.mvp.p.activity.InfoMainActivityPresenter;
import com.techjumper.polyhome.b.info.viewholder.databean.InfoAnnouncementEntityBean;
import com.techjumper.polyhome.b.info.viewholder.databean.InfoEntityBean;
import com.techjumper.polyhome.b.info.widget.AdapterUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;

@Presenter(InfoMainActivityPresenter.class)
public class InfoMainActivity extends AppBaseActivity {

    private int backType = BackEvent.FINISH;

    @Bind(R.id.title_announcement)
    RadioButton titleAnnouncement;
    @Bind(R.id.title_system)
    RadioButton titleSystem;
    @Bind(R.id.title_order)
    RadioButton titleOrder;
    @Bind(R.id.title_medical)
    RadioButton titleMedical;
    @Bind(R.id.title_date)
    TextView titleDate;
    @Bind(R.id.info_list)
    RecyclerView infoList;
    @Bind(R.id.title_rg)
    RadioGroup titleRg;
    @Bind(R.id.info_detail_title)
    TextView infoDetailTitle;
    @Bind(R.id.info_detail_date)
    TextView infoDetailDate;
    @Bind(R.id.info_detail_type)
    TextView infoDetailType;
    @Bind(R.id.info_detail_content)
    TextView infoDetailContent;
    @Bind(R.id.info_detail)
    LinearLayout infoDetail;
    @Bind(R.id.lnd_title)
    TextView lndTitle;
    @Bind(R.id.lnd_date)
    TextView lndDate;
    @Bind(R.id.lnd_content)
    WebView lndContent;
    @Bind(R.id.lnd_layout)
    LinearLayout lndLayout;

    private int type = NoticeEntity.PROPERTY;
    private LinearLayoutManager manager = new LinearLayoutManager(this);

    @OnClick(R.id.bottom_back)
    void back() {
        if (backType == BackEvent.FINISH) {
            finish();
        } else if (backType == BackEvent.INFO_LIST) {
            infoDetail.setVisibility(View.GONE);
            infoList.setVisibility(View.VISIBLE);
            lndLayout.setVisibility(View.GONE);
            backType = BackEvent.FINISH;
        }
    }

    private CommonRecyclerAdapter adapter;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_info_main);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        infoList.setLayoutManager(manager);
        adapter = new CommonRecyclerAdapter();

        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            type = bundle.getInt(PluginConstant.KEY_INFO_TYPE);
            if (type == NoticeEntity.PROPERTY) {
                titleAnnouncement.setChecked(true);
            } else if (type == NoticeEntity.SYSTEM) {
                titleSystem.setChecked(true);
            } else if (type == NoticeEntity.MEDICAL) {
                titleMedical.setChecked(true);
            } else {
                titleOrder.setChecked(true);
            }

            UserInfoEntity userInfoEntity = new UserInfoEntity();
            userInfoEntity.setUser_id(bundle.getLong(PluginConstant.KEY_INFO_USER_ID));
            userInfoEntity.setTicket(bundle.getString(PluginConstant.KEY_INFO_TICKET));
            userInfoEntity.setId(bundle.getLong(PluginConstant.KEY_INFO_FAMILY_ID));
//            userInfoEntity.setUser_id(367);
//            userInfoEntity.setTicket("508169e8a7e186d0e3e9c9f3991ad619b630fc28");
//            userInfoEntity.setId(438);
            UserInfoManager.saveUserInfo(userInfoEntity);
        }

        setType(type);

        titleRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
//                    case R.id.title_all:
//                        break;
                    case R.id.title_announcement:
                        type = NoticeEntity.PROPERTY;
                        RxBus.INSTANCE.send(new InfoTypeEvent(type));
                        break;
                    case R.id.title_system:
                        type = NoticeEntity.SYSTEM;
                        RxBus.INSTANCE.send(new InfoTypeEvent(type));
                        break;
                    case R.id.title_order:
                        type = NoticeEntity.ORDER;
                        RxBus.INSTANCE.send(new InfoTypeEvent(type));
                        break;
                    case R.id.title_medical:
                        type = NoticeEntity.MEDICAL;
                        RxBus.INSTANCE.send(new InfoTypeEvent(type));
                        break;
                }
            }
        });

        addSubscription(
                RxBus.INSTANCE.asObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            if (o instanceof InfoDetailEvent) {
                                InfoDetailEvent infoDetailEvent = (InfoDetailEvent) o;
                                infoDetail.setVisibility(View.VISIBLE);
                                infoList.setVisibility(View.GONE);
                                lndLayout.setVisibility(View.GONE);

                                infoDetailTitle.setText(infoDetailEvent.getTitle());
                                infoDetailDate.setText(infoDetailEvent.getCreated_at());
                                infoDetailContent.setText(infoDetailEvent.getContent());

                                int type = infoDetailEvent.getType();

                                if (type == InfoEntity.TYPE_SYSTEM) {
                                    infoDetailType.setBackgroundResource(R.drawable.bg_shape_radius_20c3f3);
                                    infoDetailType.setTextColor(getResources().getColor(R.color.color_20C3F3));
                                    infoDetailType.setText(R.string.info_system);
                                } else if (type == InfoEntity.TYPE_ORDER) {
                                    infoDetailType.setBackgroundResource(R.drawable.bg_shape_radius_ff9938);
                                    infoDetailType.setTextColor(getResources().getColor(R.color.color_FF9938));
                                    infoDetailType.setText(R.string.info_order);
                                } else if (type == InfoEntity.TYPE_MEDICAL) {
                                    infoDetailType.setBackgroundResource(R.drawable.bg_shape_radius_4eb738);
                                    infoDetailType.setTextColor(getResources().getColor(R.color.color_4EB738));
                                    infoDetailType.setText(R.string.info_medical);
                                }

                                RxBus.INSTANCE.send(new BackEvent(BackEvent.INFO_LIST));
                            } else if (o instanceof BackEvent) {
                                BackEvent event = (BackEvent) o;
                                backType = event.getType();
                            }
                        })
        );

        infoList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (adapter != null && String.valueOf(adapter.getItemCount()).equals(ComConstant.PAGESIZE)) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        int lastVisiblePosition = manager.findLastVisibleItemPosition();
                        if (lastVisiblePosition >= manager.getItemCount() - 1) {
                            RxBus.INSTANCE.send(new LoadmoreInfoEvent(type));
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    public void getList(List<InfoEntity.InfoDataEntity.InfoItemEntity> infoEntityTemporaries, int page) {
        List<DisplayBean> displayBeans = new ArrayList<>();
        if (infoEntityTemporaries == null || infoEntityTemporaries.size() == 0)
            return;


        for (int i = 0; i < infoEntityTemporaries.size(); i++) {
            displayBeans.add(new InfoEntityBean(infoEntityTemporaries.get(i)));
        }

        if (page == 1) {
            adapter.loadData(displayBeans);
            infoList.setAdapter(adapter);
        } else {
            adapter.insertData(adapter.getItemCount(), displayBeans);
        }

        infoDetail.setVisibility(View.GONE);
        lndLayout.setVisibility(View.GONE);
        infoList.setVisibility(View.VISIBLE);

        RxBus.INSTANCE.send(new BackEvent(BackEvent.FINISH));
    }

    public void readMessage(String result) {
        if (Boolean.valueOf(result) == true) {

        }
    }

    public void getAnnouncements(List<AnnouncementEntity.AnnouncementDataEntity> announcementDataEntities, int page) {

        infoList.setVisibility(View.VISIBLE);
        infoDetail.setVisibility(View.GONE);
        lndLayout.setVisibility(View.GONE);

        if (announcementDataEntities.size() == 0 && page == 1) {
            AdapterUtil.clear(adapter);
            return;
        }

        List<DisplayBean> displayBeans = new ArrayList<>();

        if (announcementDataEntities == null || announcementDataEntities.size() == 0)
            return;

        for (int i = 0; i < announcementDataEntities.size(); i++) {
            displayBeans.add(new InfoAnnouncementEntityBean(announcementDataEntities.get(i)));
        }

        if (page == 1) {
            adapter.loadData(displayBeans);
            infoList.setAdapter(adapter);
        } else {
            adapter.insertData(adapter.getItemCount(), displayBeans);
        }


        RxBus.INSTANCE.send(new BackEvent(BackEvent.FINISH));
    }

    public void showLndLayout(PropertyNormalDetailEvent event) {
        if (event == null)
            return;

        lndLayout.setVisibility(View.VISIBLE);
        infoDetail.setVisibility(View.GONE);
        infoList.setVisibility(View.GONE);

        lndTitle.setText(event.getTitle());
        lndDate.setText(event.getDate());

        lndContent.loadDataWithBaseURL(null, event.getContent(), "text/html", "utf-8", null);
        lndContent.getSettings().setJavaScriptEnabled(true);
        lndContent.setWebChromeClient(new WebChromeClient());

        RxBus.INSTANCE.send(new BackEvent(BackEvent.INFO_LIST));
    }
}

