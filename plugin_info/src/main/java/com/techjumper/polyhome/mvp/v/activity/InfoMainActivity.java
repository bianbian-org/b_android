package com.techjumper.polyhome.mvp.v.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.steve.creact.library.adapter.CommonRecyclerAdapter;
import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhome.InfoEntityTemporary;
import com.techjumper.polyhome.R;
import com.techjumper.polyhome.mvp.p.activity.InfoMainActivityPresenter;
import com.techjumper.polyhome.viewholder.databean.InfoEntityBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

@Presenter(InfoMainActivityPresenter.class)
public class InfoMainActivity extends AppBaseActivity {

    @Bind(R.id.title_date)
    TextView titleDate;
    @Bind(R.id.info_list)
    RecyclerView infoList;
    @Bind(R.id.title_rg)
    RadioGroup titleRg;

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
                switch (checkedId) {
                    case R.id.title_all:
                        break;
                    case R.id.title_system:
                        break;
                    case R.id.title_order:
                        break;
                    case R.id.title_medical:
                        break;
                }
            }
        });
    }

    public void getList(List<InfoEntityTemporary> infoEntityTemporaries) {
        List<DisplayBean> displayBeans = new ArrayList<>();
        if (infoEntityTemporaries == null || infoEntityTemporaries.size() == 0)
            return;


        for (int i = 0; i < infoEntityTemporaries.size(); i++) {
            displayBeans.add(new InfoEntityBean(infoEntityTemporaries.get(i)));
        }

        adapter.loadData(displayBeans);
        infoList.setAdapter(adapter);
    }
}

