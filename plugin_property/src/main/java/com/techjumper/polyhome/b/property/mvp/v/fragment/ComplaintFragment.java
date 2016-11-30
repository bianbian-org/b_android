package com.techjumper.polyhome.b.property.mvp.v.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.steve.creact.library.adapter.CommonRecyclerAdapter;
import com.steve.creact.library.display.DisplayBean;
import com.techjumper.commonres.ComConstant;
import com.techjumper.commonres.entity.ComplaintDetailEntity;
import com.techjumper.commonres.entity.ComplaintEntity;
import com.techjumper.commonres.entity.ReplyEntity;
import com.techjumper.commonres.entity.event.BackEvent;
import com.techjumper.commonres.entity.event.loadmoreevent.LoadmorePresenterEvent;
import com.techjumper.commonres.util.CommonDateUtil;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.property.Constant;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.UserInfoManager;
import com.techjumper.polyhome.b.property.mvp.p.fragment.ComplaintFragmentPresenter;
import com.techjumper.polyhome.b.property.mvp.v.activity.MainActivity;
import com.techjumper.polyhome.b.property.utils.AdapterUtil;
import com.techjumper.polyhome.b.property.utils.TypeUtil;
import com.techjumper.polyhome.b.property.viewholder.databean.InfoComplaintEntityBean;
import com.techjumper.polyhome.b.property.viewholder.databean.InfoReplyLeftEntityBean;
import com.techjumper.polyhome.b.property.viewholder.databean.InfoReplyRightEntityBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
@Presenter(ComplaintFragmentPresenter.class)
public class ComplaintFragment extends AppBaseFragment<ComplaintFragmentPresenter> {

    @Bind(R.id.list)
    RecyclerView list;
    @Bind(R.id.title_action)
    TextView titleAction;
    @Bind(R.id.layout_complaint)
    LinearLayout layoutComplaint;
    @Bind(R.id.lac_checkbox_complaint_complaints)
    RadioButton lacCheckboxComplaintComplaints;
    @Bind(R.id.lac_checkbox_complaint_suggest)
    RadioButton lacCheckboxComplaintSuggest;
    @Bind(R.id.lac_property_complaint_praise)
    RadioButton lacPropertyComplaintPraise;
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.lac_mobile)
    EditText lacMobile;
    @Bind(R.id.textView3)
    TextView textView3;
    @Bind(R.id.lac_content)
    EditText lacContent;
    @Bind(R.id.lac_submit)
    TextView lacSubmit;
    @Bind(R.id.lac_layout)
    LinearLayout lacLayout;
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
    @Bind(R.id.scrollView)
    NestedScrollView scrollView;
    @Bind(R.id.lmd_message_content)
    EditText lmdMessageContent;
    @Bind(R.id.lmd_message_send)
    TextView lmdMessageSend;
    @Bind(R.id.lmd_layout)
    LinearLayout lmdLayout;

    @OnClick(R.id.title_action)
    void titleAction() {
        showAction();
    }

    private CommonRecyclerAdapter adapter;
    private CommonRecyclerAdapter messageAdapter;
    private long sendId;
    private LinearLayoutManager manager = new LinearLayoutManager(getActivity());
    private int type;
    private int showType;
    private long infoId;

    public long getSendId() {
        return sendId;
    }

    public void setSendId(long sendId) {
        this.sendId = sendId;
    }

    public EditText getLmdMessageContent() {
        return lmdMessageContent;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public EditText getLacContent() {
        return lacContent;
    }

    public EditText getLacMobile() {
        return lacMobile;
    }

    public int getShowType() {
        return showType;
    }

    public long getInfoId() {
        return infoId;
    }

    public static ComplaintFragment getInstance(int type, int showType, long infoId) {
        ComplaintFragment complaintFragment = new ComplaintFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.TYPE, type);
        bundle.putInt(MainActivity.SHOWTYPE, showType);
        bundle.putLong(MainActivity.ID, infoId);
        complaintFragment.setArguments(bundle);
        return complaintFragment;
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_complaint, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        type = getArguments().getInt(MainActivity.TYPE);
        showType = getArguments().getInt(MainActivity.SHOWTYPE);
        infoId = getArguments().getLong(MainActivity.ID);

        adapter = new CommonRecyclerAdapter();
        messageAdapter = new CommonRecyclerAdapter();
        list.setLayoutManager(manager);
        lmdList.setLayoutManager(new LinearLayoutManager(getContext()));

        lacMobile.setText(UserInfoManager.getMobile());

        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (adapter != null && String.valueOf(adapter.getItemCount()).equals(ComConstant.PAGESIZE)) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        int lastVisiblePosition = manager.findLastVisibleItemPosition();
                        if (lastVisiblePosition >= manager.getItemCount() - 1) {
                            RxBus.INSTANCE.send(new LoadmorePresenterEvent(type));
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

    public void getComplaints(List<ComplaintEntity.ComplaintDataEntity> complaintDataEntities, int page) {
        showList();

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

        if (page == 1) {
            adapter.loadData(displayBeans);
            list.setAdapter(adapter);
        } else {
            adapter.insertData(adapter.getItemCount(), displayBeans);
        }
    }

    public void showComplaintDetailLmdLayout(ComplaintDetailEntity.ComplaintDetailDataEntity entity) {
        if (entity == null)
            return;

        lmdMessageContent.setText("");

        long id = entity.getId();
        long user_id = Long.valueOf(UserInfoManager.getUserId());

        showDetail();

        lmdTitle.setText(TypeUtil.getComplanitTypeString(entity.getTypes()));
        lmdContent.setText(entity.getContent());
        lmdDate.setText(entity.getCreated_at().substring(0, 10));
        setSendId(id);

        int status = entity.getStatus();

        if (status == Constant.STATUS_RESPONSE) {
            lmdType.setBackgroundResource(R.drawable.bg_shape_radius_20c3f3);
            lmdType.setText(R.string.property_type_response);
        } else if (status == Constant.STATUS_SUBMIT) {
            lmdType.setBackgroundResource(R.drawable.bg_shape_radius_ff9938);
            lmdType.setText(R.string.property_type_submit);
        } else if (status == Constant.STATUS_FINISH) {
            lmdType.setBackgroundResource(R.drawable.bg_shape_radius_4eb738);
            lmdType.setText(R.string.property_type_finish);
        } else {
            lmdType.setBackgroundResource(R.drawable.bg_shape_radius_4eb738);
            lmdType.setText(R.string.property_type_close);
        }

        AdapterUtil.clear(messageAdapter);

        List<ReplyEntity> entities = entity.getReplies();
        if (entities.size() == 0)
            return;

        List<DisplayBean> displayBeans = new ArrayList<>();

        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).getUser_id() == user_id) {
                displayBeans.add(new InfoReplyRightEntityBean(entities.get(i)));
            } else {
                displayBeans.add(new InfoReplyLeftEntityBean(entities.get(i)));
            }
        }

        messageAdapter.loadData(displayBeans);
        lmdList.setAdapter(messageAdapter);
        scrollView.scrollTo(0, 0);
        lmdList.setNestedScrollingEnabled(false);
    }

    public void sendSuccess() {
        ReplyEntity entity = new ReplyEntity();
        entity.setContent(lmdMessageContent.getText().toString());
        entity.setTime(CommonDateUtil.getTime());
        InfoReplyRightEntityBean bean = new InfoReplyRightEntityBean(entity);

        List<DisplayBean> displayBeans = new ArrayList<>();

        if (messageAdapter.getItemCount() == 0) {
            displayBeans.add(bean);
            messageAdapter.loadData(displayBeans);
            lmdList.setAdapter(messageAdapter);
        } else {
            messageAdapter.insertData(0, bean);
        }

        lmdMessageContent.setText("");

        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(lmdMessageContent.getWindowToken(), 0);
    }

    private void showList() {
        layoutComplaint.setVisibility(View.VISIBLE);
        lacLayout.setVisibility(View.GONE);
        lmdLayout.setVisibility(View.GONE);
    }

    private void showDetail() {
        layoutComplaint.setVisibility(View.GONE);
        lacLayout.setVisibility(View.GONE);
        lmdLayout.setVisibility(View.VISIBLE);
    }

    private void showAction() {
        layoutComplaint.setVisibility(View.GONE);
        lacLayout.setVisibility(View.VISIBLE);
        lmdLayout.setVisibility(View.GONE);

        RxBus.INSTANCE.send(new BackEvent(BackEvent.PROPERTY_LIST));
    }
}
