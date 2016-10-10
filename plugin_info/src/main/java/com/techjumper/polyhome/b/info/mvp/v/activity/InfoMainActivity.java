package com.techjumper.polyhome.b.info.mvp.v.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.techjumper.commonres.entity.HeartbeatTimeEntity;
import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.commonres.entity.NoticeEntity;
import com.techjumper.commonres.entity.event.BackEvent;
import com.techjumper.commonres.entity.event.HeartbeatEvent;
import com.techjumper.commonres.entity.event.InfoDetailEvent;
import com.techjumper.commonres.entity.event.InfoTypeEvent;
import com.techjumper.commonres.entity.event.PropertyNormalDetailEvent;
import com.techjumper.commonres.entity.event.StayEvent;
import com.techjumper.commonres.entity.event.TimeEvent;
import com.techjumper.commonres.entity.event.loadmoreevent.LoadmoreInfoEvent;
import com.techjumper.commonres.util.CommonDateUtil;
import com.techjumper.commonres.util.PluginEngineUtil;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.plugincommunicateengine.IPluginMessageReceiver;
import com.techjumper.plugincommunicateengine.PluginEngine;
import com.techjumper.plugincommunicateengine.entity.core.SaveInfoEntity;
import com.techjumper.polyhome.b.info.R;
import com.techjumper.polyhome.b.info.UserInfoManager;
import com.techjumper.polyhome.b.info.mvp.p.activity.InfoMainActivityPresenter;
import com.techjumper.polyhome.b.info.viewholder.databean.InfoAnnouncementEntityBean;
import com.techjumper.polyhome.b.info.viewholder.databean.InfoEntityBean;
import com.techjumper.polyhome.b.info.widget.AdapterUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;

@Presenter(InfoMainActivityPresenter.class)
public class InfoMainActivity extends AppBaseActivity {
    private int backType = BackEvent.FINISH;

    @Bind(R.id.title_repair)
    RadioButton titleRepair;
    @Bind(R.id.title_suggest)
    RadioButton titleSuggest;
    @Bind(R.id.repair_unread_num)
    TextView repairUnreadNum;
    @Bind(R.id.suggest_unread_num)
    TextView suggestUnreadNum;
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
    @Bind(R.id.bottom_title)
    TextView bottomTitle;
    @Bind(R.id.bottom_date)
    TextView bottomDate;
    @Bind(R.id.system_unread_num)
    TextView systemUnreadNum;
    @Bind(R.id.order_unread_num)
    TextView orderUnreadNum;
    @Bind(R.id.medical_unread_num)
    TextView medicalUnreadNum;

    private int type = NoticeEntity.PROPERTY;
    private LinearLayoutManager manager = new LinearLayoutManager(this);
    private List<NoticeEntity.Unread> unreads = new ArrayList<>();
    private int systemNum = 0;
    private long time = 0L;

    public long getTime() {
        return time;
    }

    private IPluginMessageReceiver mIPluginMessageReceiver = (code, message, extras) -> {
        if (code == PluginEngine.CODE_GET_SAVE_INFO) {
            SaveInfoEntity saveInfoEntity = com.techjumper.plugincommunicateengine.utils.GsonUtils.fromJson(message, SaveInfoEntity.class);
            if (saveInfoEntity == null || saveInfoEntity.getData() == null)
                return;

            HashMap<String, String> hashMap = saveInfoEntity.getData().getValues();
            if (hashMap == null)
                return;

            if (hashMap == null || hashMap.size() == 0)
                return;

            String name = saveInfoEntity.getData().getName();

            if (name.equals(ComConstant.FILE_HEARTBEATTIME)) {
                Log.d("infosubmitOnline", "获取本地心跳时间为: " + (TextUtils.isEmpty(message) ? "没有文件" : message));

                HeartbeatTimeEntity entity = GsonUtils.fromJson(message, HeartbeatTimeEntity.class);
                if (entity != null &&
                        entity.getData() != null &&
                        entity.getData().getValues() != null &&
                        entity.getData().getValues().getTime() != null) {
                    time = Long.valueOf(entity.getData().getValues().getTime());
                    RxBus.INSTANCE.send(new HeartbeatEvent(time));
                }
            }
        }
    };

    @OnClick(R.id.bottom_back)
    void back() {
        if (backType == BackEvent.FINISH) {
            updateNotices();
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

    public TextView getBottomDate() {
        return bottomDate;
    }

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_info_main);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bottomTitle.setText(R.string.info_title);

        PluginEngine.getInstance().
                registerReceiver(mIPluginMessageReceiver);

        infoList.setLayoutManager(manager);
        adapter = new CommonRecyclerAdapter();

        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            Log.d("info", bundle.toString());
            type = bundle.getInt(PluginConstant.KEY_INFO_TYPE);
            if (type == NoticeEntity.PROPERTY) {
                titleAnnouncement.setChecked(true);
            } else if (type == NoticeEntity.ORDER) {
                titleOrder.setChecked(true);
            } else if (type == NoticeEntity.MEDICAL) {
                titleMedical.setChecked(true);
            } else if (type == NoticeEntity.SYSTEM) {
                titleSystem.setChecked(true);
            } else if (type == NoticeEntity.REPAIR) {
                titleRepair.setChecked(true);
            } else {
                titleSuggest.setChecked(true);
            }

            String unreadString = bundle.getString(PluginConstant.KEY_INFO_UNREAD);
            NoticeEntity.InfoUnread infoUnread = GsonUtils.fromJson(unreadString, NoticeEntity.InfoUnread.class);

            unreads = infoUnread.getUnreads();
            if (unreads.size() > 0) {
                for (int i = 0; i < unreads.size(); i++) {
                    NoticeEntity.Unread unread = unreads.get(i);
                    if (unread.getType() == NoticeEntity.ORDER) {
                        if (unread.getCount() > 0) {
                            orderUnreadNum.setText(String.valueOf(unread.getCount()));
                            orderUnreadNum.setVisibility(View.VISIBLE);
                        }
                    } else if (unread.getType() == NoticeEntity.MEDICAL) {
                        if (unread.getCount() > 0) {
                            medicalUnreadNum.setText(String.valueOf(unread.getCount()));
                            medicalUnreadNum.setVisibility(View.VISIBLE);
                        }
                    } else if (unread.getType() == NoticeEntity.SYSTEM) {
                        if (unread.getCount() > 0) {
                            systemUnreadNum.setText(String.valueOf(unread.getCount()));
                            systemUnreadNum.setVisibility(View.VISIBLE);
                        }
                    } else if (unread.getType() == NoticeEntity.REPAIR) {
                        if (unread.getCount() > 0) {
                            repairUnreadNum.setText(String.valueOf(unread.getCount()));
                            repairUnreadNum.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (unread.getCount() > 0) {
                            suggestUnreadNum.setText(String.valueOf(unread.getCount()));
                            suggestUnreadNum.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            Log.d("cf", "id" + bundle.getLong(PluginConstant.KEY_INFO_USER_ID));
            Log.d("UserInfoEntity", "ticket" + bundle.getString(PluginConstant.KEY_INFO_TICKET));
            Log.d("UserInfoEntity", "family_id" + bundle.getLong(PluginConstant.KEY_INFO_FAMILY_ID));
            UserInfoEntity userInfoEntity = new UserInfoEntity();
            userInfoEntity.setUser_id(bundle.getLong(PluginConstant.KEY_INFO_USER_ID));
            userInfoEntity.setTicket(bundle.getString(PluginConstant.KEY_INFO_TICKET));
            userInfoEntity.setId(bundle.getLong(PluginConstant.KEY_INFO_FAMILY_ID));
            UserInfoManager.saveUserInfo(userInfoEntity);
        }
//        UserInfoEntity userInfoEntity = new UserInfoEntity();
//        userInfoEntity.setUser_id(362);
//        userInfoEntity.setTicket("745090aaa098235c03790ac8bbc597e222ce7031");
//        userInfoEntity.setId(434);
//        UserInfoManager.saveUserInfo(userInfoEntity);

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
                    case R.id.title_repair:
                        type = NoticeEntity.REPAIR;
                        RxBus.INSTANCE.send(new InfoTypeEvent(type));
                        break;
                    case R.id.title_suggest:
                        type = NoticeEntity.SUGGEST;
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
                                    infoDetailType.setText(R.string.info_system);
                                } else if (type == InfoEntity.TYPE_ORDER) {
                                    infoDetailType.setBackgroundResource(R.drawable.bg_shape_radius_ff9938);
                                    infoDetailType.setText(R.string.info_order);
                                } else if (type == InfoEntity.TYPE_MEDICAL) {
                                    infoDetailType.setBackgroundResource(R.drawable.bg_shape_radius_4eb738);
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

        PluginEngineUtil.getHeartbeatTime();
    }

    public void getList(InfoEntity.InfoResultEntity infoResultEntity, int page) {

        infoDetail.setVisibility(View.GONE);
        lndLayout.setVisibility(View.GONE);
        infoList.setVisibility(View.VISIBLE);

        List<InfoEntity.InfoResultEntity.InfoItemEntity> infoEntityTemporaries = infoResultEntity.getMessages();
        int num = infoResultEntity.getUn_read();

        if (infoEntityTemporaries == null)
            return;

        if (infoEntityTemporaries.size() == 0 && page == 1) {
            AdapterUtil.clear(adapter);
            return;
        }

        if (num == 0) {
            if (type == NoticeEntity.SYSTEM) {
                systemUnreadNum.setText("0");
                systemUnreadNum.setVisibility(View.GONE);
            } else if (type == NoticeEntity.ORDER) {
                orderUnreadNum.setText("0");
                orderUnreadNum.setVisibility(View.GONE);
            } else if (type == NoticeEntity.MEDICAL) {
                medicalUnreadNum.setText("0");
                medicalUnreadNum.setVisibility(View.GONE);
            } else if (type == NoticeEntity.REPAIR) {
                repairUnreadNum.setText("0");
                repairUnreadNum.setVisibility(View.GONE);
            } else if (type == NoticeEntity.SUGGEST) {
                suggestUnreadNum.setText("0");
                suggestUnreadNum.setVisibility(View.GONE);
            }
        } else {
            if (type == NoticeEntity.SYSTEM) {
                systemUnreadNum.setText(String.valueOf(num));
                systemUnreadNum.setVisibility(View.VISIBLE);
            } else if (type == NoticeEntity.ORDER) {
                orderUnreadNum.setText(String.valueOf(num));
                orderUnreadNum.setVisibility(View.VISIBLE);
            } else if (type == NoticeEntity.MEDICAL) {
                medicalUnreadNum.setText(String.valueOf(num));
                medicalUnreadNum.setVisibility(View.VISIBLE);
            } else if (type == NoticeEntity.REPAIR) {
                repairUnreadNum.setText(String.valueOf(num));
                repairUnreadNum.setVisibility(View.VISIBLE);
            } else if (type == NoticeEntity.SUGGEST) {
                suggestUnreadNum.setText(String.valueOf(num));
                suggestUnreadNum.setVisibility(View.VISIBLE);
            }
        }

        List<DisplayBean> displayBeans = new ArrayList<>();

        for (int i = 0; i < infoEntityTemporaries.size(); i++) {
            displayBeans.add(new InfoEntityBean(infoEntityTemporaries.get(i)));
        }

        if (page == 1) {
            adapter.loadData(displayBeans);
            infoList.setAdapter(adapter);
        } else {
            adapter.insertData(adapter.getItemCount(), displayBeans);
        }

        RxBus.INSTANCE.send(new BackEvent(BackEvent.FINISH));
    }

    public void readMessage(String result, int type) {
        if (Boolean.valueOf(result) == true) {
            if (type == NoticeEntity.MEDICAL) {
                int medicalNum = Integer.valueOf(medicalUnreadNum.getText().toString());
                if (medicalNum == 1) {
                    medicalUnreadNum.setText("0");
                    medicalUnreadNum.setVisibility(View.GONE);
                } else {
                    medicalNum--;
                    medicalUnreadNum.setText(String.valueOf(medicalNum));
                    medicalUnreadNum.setVisibility(View.VISIBLE);
                }
            } else if (type == NoticeEntity.ORDER) {
                int num = Integer.valueOf(orderUnreadNum.getText().toString());
                if (num == 1) {
                    orderUnreadNum.setText("0");
                    orderUnreadNum.setVisibility(View.GONE);
                } else {
                    num--;
                    orderUnreadNum.setText(String.valueOf(num));
                    orderUnreadNum.setVisibility(View.VISIBLE);
                }
            } else if (type == NoticeEntity.SYSTEM) {
                int num = Integer.valueOf(systemUnreadNum.getText().toString());
                if (num == 1) {
                    systemUnreadNum.setText("0");
                    systemUnreadNum.setVisibility(View.GONE);
                } else {
                    num--;
                    systemUnreadNum.setText(String.valueOf(num));
                    systemUnreadNum.setVisibility(View.VISIBLE);
                }
            } else if (type == NoticeEntity.REPAIR) {
                int num = Integer.valueOf(repairUnreadNum.getText().toString());
                if (num == 1) {
                    repairUnreadNum.setText("0");
                    repairUnreadNum.setVisibility(View.GONE);
                } else {
                    num--;
                    repairUnreadNum.setText(String.valueOf(num));
                    repairUnreadNum.setVisibility(View.VISIBLE);
                }
            } else if (type == NoticeEntity.SUGGEST) {
                int num = Integer.valueOf(suggestUnreadNum.getText().toString());
                if (num == 1) {
                    suggestUnreadNum.setText("0");
                    suggestUnreadNum.setVisibility(View.GONE);
                } else {
                    num--;
                    suggestUnreadNum.setText(String.valueOf(num));
                    suggestUnreadNum.setVisibility(View.VISIBLE);
                }
            }
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

    private void updateNotices() {
        Intent intent = new Intent();
        intent.setAction("action_push_receive");
        intent.putExtra("key_extra", "updateNotice");
        sendBroadcast(intent);
    }
}

