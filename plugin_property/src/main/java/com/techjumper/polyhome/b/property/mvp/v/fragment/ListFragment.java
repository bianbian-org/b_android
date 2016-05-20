package com.techjumper.polyhome.b.property.mvp.v.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.steve.creact.library.adapter.CommonRecyclerAdapter;
import com.steve.creact.library.display.DisplayBean;
import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.ComplaintDetailEntity;
import com.techjumper.commonres.entity.ComplaintEntity;
import com.techjumper.commonres.entity.RepairDetailEntity;
import com.techjumper.commonres.entity.RepairEntity;
import com.techjumper.commonres.entity.ReplyEntity;
import com.techjumper.commonres.entity.event.PropertyNormalDetailEvent;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhome.b.property.Constant;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.mvp.p.fragment.ListFragmentPresenter;
import com.techjumper.polyhome.b.property.mvp.v.activity.MainActivity;
import com.techjumper.polyhome.b.property.utils.AdapterUtil;
import com.techjumper.polyhome.b.property.utils.TypeUtil;
import com.techjumper.polyhome.b.property.viewholder.databean.InfoAnnouncementEntityBean;
import com.techjumper.polyhome.b.property.viewholder.databean.InfoComplaintEntityBean;
import com.techjumper.polyhome.b.property.viewholder.databean.InfoRepairEntityBean;
import com.techjumper.polyhome.b.property.viewholder.databean.InfoReplyLeftEntityBean;
import com.techjumper.polyhome.b.property.viewholder.databean.InfoReplyRightEntityBean;

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
    @Bind(R.id.lnd_layout)
    LinearLayout lndLayout;
    @Bind(R.id.lnd_title)
    TextView lndTitle;
    @Bind(R.id.lnd_date)
    TextView lndDate;
    @Bind(R.id.lnd_content)
    TextView lndContent;
    @Bind(R.id.lmd_title)
    TextView lmdTitle;
    @Bind(R.id.lmd_date)
    TextView lmdDate;
    @Bind(R.id.lmd_type)
    TextView lmdType;
    @Bind(R.id.lmd_content)
    TextView lmdContent;
    @Bind(R.id.lmd_list)
    RecyclerView lmdList;
    @Bind(R.id.lmd_layout)
    LinearLayout lmdLayout;
    @Bind(R.id.lmd_message_content)
    EditText lmdMessageContent;

    private CommonRecyclerAdapter adapter;
    private CommonRecyclerAdapter messageAdapter;

    private int type;
    private long sendId;
    private int actionType;

    public static ListFragment getInstance() {
        return new ListFragment();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getSendId() {
        return sendId;
    }

    public void setSendId(long sendId) {
        this.sendId = sendId;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public EditText getLmdMessageContent() {
        return lmdMessageContent;
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        adapter = new CommonRecyclerAdapter();
        messageAdapter = new CommonRecyclerAdapter();

        flList.setLayoutManager(new LinearLayoutManager(getContext()));
        lmdList.setLayoutManager(new LinearLayoutManager(getContext()));

        showListLayout();
    }

    public void getAnnouncements(List<AnnouncementEntity.AnnouncementDataEntity> announcementDataEntities, int page) {
        flTitleAction.setText(R.string.property_call);
        flTitleAction.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_call, 0, 0, 0);
        flTitleAction.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.dp_5));
        setType(MainActivity.ANNOUNCEMENT);
        showListLayout();

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

        adapter.loadData(displayBeans);
        flList.setAdapter(adapter);
    }

    public void getComplaints(List<ComplaintEntity.ComplaintDataEntity> complaintDataEntities, int page) {
        flTitleAction.setText(R.string.property_new_complaint);
        flTitleAction.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_complaint, 0, 0, 0);
        flTitleAction.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.dp_5));
        setType(MainActivity.COMPLAINT);
        showListLayout();

        if (complaintDataEntities.size() == 0 && page == 1) {
            AdapterUtil.clear(adapter);
            return;
        }

        List<DisplayBean> displayBeans = new ArrayList<>();
        if (complaintDataEntities == null || complaintDataEntities.size() == 0)
            return;


        for (int i = 0; i < complaintDataEntities.size(); i++) {
            displayBeans.add(new InfoComplaintEntityBean(complaintDataEntities.get(i)));
        }

        adapter.loadData(displayBeans);
        flList.setAdapter(adapter);
    }

    public void getRepairs(List<RepairEntity.RepairDataEntity> repairDataEntities, int page) {
        flTitleAction.setText(R.string.property_new_repair);
        flTitleAction.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_repair, 0, 0, 0);
        flTitleAction.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.dp_5));
        setType(MainActivity.REPAIR);
        showListLayout();

        if (repairDataEntities.size() == 0 && page == 1) {
            AdapterUtil.clear(adapter);
            return;
        }

        List<DisplayBean> displayBeans = new ArrayList<>();
        if (repairDataEntities == null || repairDataEntities.size() == 0)
            return;


        for (int i = 0; i < repairDataEntities.size(); i++) {
            displayBeans.add(new InfoRepairEntityBean(repairDataEntities.get(i)));
        }

        adapter.loadData(displayBeans);
        flList.setAdapter(adapter);
    }

    public void showLndLayout(PropertyNormalDetailEvent event) {
        if (event == null)
            return;

        lndLayout.setVisibility(View.VISIBLE);
        lmdLayout.setVisibility(View.GONE);
        flList.setVisibility(View.GONE);

        lndTitle.setText(event.getTitle());
        lndDate.setText(event.getDate());
        lndContent.setText(event.getContent());
    }

    public void showComplaintDetailLmdLayout(ComplaintDetailEntity.ComplaintDetailDataEntity entity) {
        if (entity == null)
            return;

        lmdMessageContent.setText("");

        long id = entity.getId();

        lndLayout.setVisibility(View.GONE);
        lmdLayout.setVisibility(View.VISIBLE);
        flList.setVisibility(View.GONE);

        lmdTitle.setText(TypeUtil.getComplanitTypeString(entity.getTypes()));
        lmdContent.setText(entity.getContent());
        lmdDate.setText(entity.getCreated_at().substring(0, 10));
        setSendId(id);
        setActionType(ActionFragment.COMPLAINT);

        int status = entity.getStatus();

        if (status == Constant.STATUS_RESPONSE) {
            lmdType.setBackgroundResource(R.drawable.bg_shape_radius_20c3f3);
            lmdType.setTextColor(getContext().getResources().getColor(R.color.color_20C3F3));
            lmdType.setText(R.string.property_type_response);
        } else if (status == Constant.STATUS_SUBMIT) {
            lmdType.setBackgroundResource(R.drawable.bg_shape_radius_ff9938);
            lmdType.setTextColor(getContext().getResources().getColor(R.color.color_FF9938));
            lmdType.setText(R.string.property_type_submit);
        } else if (status == Constant.STATUS_FINISH) {
            lmdType.setBackgroundResource(R.drawable.bg_shape_radius_4eb738);
            lmdType.setTextColor(getContext().getResources().getColor(R.color.color_4EB738));
            lmdType.setText(R.string.property_type_finish);
        } else {
            lmdType.setBackgroundResource(R.drawable.bg_shape_radius_4eb738);
            lmdType.setTextColor(getContext().getResources().getColor(R.color.color_4EB738));
            lmdType.setText(R.string.property_type_close);
        }

        AdapterUtil.clear(messageAdapter);

        List<ReplyEntity> entities = entity.getReplies();
        if (entities.size() == 0)
            return;

        List<DisplayBean> displayBeans = new ArrayList<>();

        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).getUser_id() == id) {
                displayBeans.add(new InfoReplyRightEntityBean(entities.get(i)));
            } else {
                displayBeans.add(new InfoReplyLeftEntityBean(entities.get(i)));
            }
        }

        messageAdapter.loadData(displayBeans);
        lmdList.setAdapter(messageAdapter);
        lmdList.scrollToPosition(0);
        lmdList.setNestedScrollingEnabled(false);
    }

    public void showRepairDetailLmdLayout(RepairDetailEntity.RepairDetailDataEntity entity) {
        if (entity == null)
            return;

        lmdMessageContent.setText("");
        long id = entity.getId();

        lndLayout.setVisibility(View.GONE);
        lmdLayout.setVisibility(View.VISIBLE);
        flList.setVisibility(View.GONE);

        lmdTitle.setText(TypeUtil.getRepairTitle(entity.getRepair_type(), entity.getRepair_device()));
        lmdContent.setText(entity.getNote());
        lmdDate.setText("10月1日");
        setSendId(id);
        setActionType(ActionFragment.REPAIR);

        int status = entity.getStatus();

        if (status == Constant.STATUS_RESPONSE) {
            lmdType.setBackgroundResource(R.drawable.bg_shape_radius_20c3f3);
            lmdType.setTextColor(getContext().getResources().getColor(R.color.color_20C3F3));
            lmdType.setText(R.string.property_type_response);
        } else if (status == Constant.STATUS_SUBMIT) {
            lmdType.setBackgroundResource(R.drawable.bg_shape_radius_ff9938);
            lmdType.setTextColor(getContext().getResources().getColor(R.color.color_FF9938));
            lmdType.setText(R.string.property_type_submit);
        } else if (status == Constant.STATUS_FINISH) {
            lmdType.setBackgroundResource(R.drawable.bg_shape_radius_4eb738);
            lmdType.setTextColor(getContext().getResources().getColor(R.color.color_4EB738));
            lmdType.setText(R.string.property_type_finish);
        } else {
            lmdType.setBackgroundResource(R.drawable.bg_shape_radius_4eb738);
            lmdType.setTextColor(getContext().getResources().getColor(R.color.color_4EB738));
            lmdType.setText(R.string.property_type_close);
        }

        AdapterUtil.clear(messageAdapter);

        List<ReplyEntity> entities = entity.getReplies();
        if (entities.size() == 0)
            return;

        List<DisplayBean> displayBeans = new ArrayList<>();

        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).getUser_id() == id) {
                displayBeans.add(new InfoReplyRightEntityBean(entities.get(i)));
            } else {
                displayBeans.add(new InfoReplyLeftEntityBean(entities.get(i)));
            }
        }

        messageAdapter.loadData(displayBeans);
        lmdList.setAdapter(messageAdapter);
        lmdList.scrollToPosition(0);
        lmdList.setNestedScrollingEnabled(false);
    }

    public void sendSuccess() {
        ReplyEntity entity = new ReplyEntity();
        entity.setContent(lmdMessageContent.getText().toString());
        entity.setTime("10:10");
        InfoReplyRightEntityBean bean = new InfoReplyRightEntityBean(entity);

        messageAdapter.insertData(0, bean);

        lmdMessageContent.setText("");
    }

    public void showListLayout() {
        lndLayout.setVisibility(View.GONE);
        lmdLayout.setVisibility(View.GONE);
        flList.setVisibility(View.VISIBLE);
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
