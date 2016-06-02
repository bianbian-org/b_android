package com.techjumper.polyhome.b.info.mvp.v.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.steve.creact.library.adapter.CommonRecyclerAdapter;
import com.steve.creact.library.display.DisplayBean;
import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.commonres.entity.event.InfoDetailEvent;
import com.techjumper.commonres.entity.event.InfoTypeEvent;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.info.R;
import com.techjumper.polyhome.b.info.mvp.p.activity.InfoMainActivityPresenter;
import com.techjumper.polyhome.b.info.viewholder.databean.InfoEntityBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;

@Presenter(InfoMainActivityPresenter.class)
public class InfoMainActivity extends AppBaseActivity {

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

    @OnClick(R.id.bottom_back)
    void back() {
        if (infoDetail.getVisibility() == View.VISIBLE) {
            infoDetail.setVisibility(View.GONE);
            infoList.setVisibility(View.VISIBLE);
        }
    }

    private CommonRecyclerAdapter adapter;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_info_main);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        infoList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonRecyclerAdapter();

        titleRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int type = -1;
                switch (checkedId) {
                    case R.id.title_all:
                        break;
                    case R.id.title_system:
                        type = InfoEntity.TYPE_SYSTEM;
                        break;
                    case R.id.title_order:
                        type = InfoEntity.TYPE_ORDER;
                        break;
                    case R.id.title_medical:
                        type = InfoEntity.TYPE_MEDICAL;
                        break;
                }

                RxBus.INSTANCE.send(new InfoTypeEvent(type));
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


                            }
                        })
        );
    }

    public void getList(List<InfoEntity.InfoDataEntity.InfoItemEntity> infoEntityTemporaries) {
        List<DisplayBean> displayBeans = new ArrayList<>();
        if (infoEntityTemporaries == null || infoEntityTemporaries.size() == 0)
            return;


        for (int i = 0; i < infoEntityTemporaries.size(); i++) {
            displayBeans.add(new InfoEntityBean(infoEntityTemporaries.get(i)));
        }

        adapter.loadData(displayBeans);
        infoList.setAdapter(adapter);

        infoDetail.setVisibility(View.GONE);
        infoList.setVisibility(View.VISIBLE);
    }

    public void readMessage(String result) {
        if (Boolean.valueOf(result) == true) {

        }
    }
}

