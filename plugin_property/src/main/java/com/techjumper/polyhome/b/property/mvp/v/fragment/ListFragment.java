package com.techjumper.polyhome.b.property.mvp.v.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.steve.creact.library.adapter.CommonRecyclerAdapter;
import com.steve.creact.library.display.DisplayBean;
import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.hehe.AnnounHehe;
import com.techjumper.polyhome.b.property.hehe.ComplaintHehe;
import com.techjumper.polyhome.b.property.hehe.RepairHehe;
import com.techjumper.polyhome.b.property.mvp.p.fragment.ListFragmentPresenter;
import com.techjumper.polyhome.b.property.viewholder.databean.InfoAnnounHeheBean;
import com.techjumper.polyhome.b.property.viewholder.databean.InfoComplaintHeheBean;
import com.techjumper.polyhome.b.property.viewholder.databean.InfoRepairHeheBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
@Presenter(ListFragmentPresenter.class)
public class ListFragment extends AppBaseFragment<ListFragmentPresenter> {

    @Bind(R.id.fl_title_rg)
    RadioGroup flTitleRg;
    @Bind(R.id.fl_title_action)
    TextView flTitleAction;
    @Bind(R.id.fl_list)
    RecyclerView flList;

    private CommonRecyclerAdapter adapter;

    public static ListFragment getInstance() {
        return new ListFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        adapter = new CommonRecyclerAdapter();

        flList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void getAnnounHehes(List<AnnounHehe> infoEntityTemporaries) {
        List<DisplayBean> displayBeans = new ArrayList<>();
        if (infoEntityTemporaries == null || infoEntityTemporaries.size() == 0)
            return;


        for (int i = 0; i < infoEntityTemporaries.size(); i++) {
            displayBeans.add(new InfoAnnounHeheBean(infoEntityTemporaries.get(i)));
        }

        adapter.loadData(displayBeans);
        flList.setAdapter(adapter);
    }

    public void getComplaintHehes(List<ComplaintHehe> infoEntityTemporaries) {
        List<DisplayBean> displayBeans = new ArrayList<>();
        if (infoEntityTemporaries == null || infoEntityTemporaries.size() == 0)
            return;


        for (int i = 0; i < infoEntityTemporaries.size(); i++) {
            displayBeans.add(new InfoComplaintHeheBean(infoEntityTemporaries.get(i)));
        }

        adapter.loadData(displayBeans);
        flList.setAdapter(adapter);
    }

    public void getRepairHehes(List<RepairHehe> infoEntityTemporaries) {
        List<DisplayBean> displayBeans = new ArrayList<>();
        if (infoEntityTemporaries == null || infoEntityTemporaries.size() == 0)
            return;


        for (int i = 0; i < infoEntityTemporaries.size(); i++) {
            displayBeans.add(new InfoRepairHeheBean(infoEntityTemporaries.get(i)));
        }

        adapter.loadData(displayBeans);
        flList.setAdapter(adapter);
    }
}
