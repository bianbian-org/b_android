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
import com.techjumper.commonres.entity.RepairDetailEntity;
import com.techjumper.commonres.entity.RepairEntity;
import com.techjumper.commonres.entity.ReplyEntity;
import com.techjumper.commonres.entity.event.BackEvent;
import com.techjumper.commonres.entity.event.loadmoreevent.LoadmorePresenterEvent;
import com.techjumper.commonres.util.CommonDateUtil;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.property.Constant;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.UserInfoManager;
import com.techjumper.polyhome.b.property.mvp.p.fragment.RepairFragmentPresenter;
import com.techjumper.polyhome.b.property.mvp.v.activity.MainActivity;
import com.techjumper.polyhome.b.property.utils.AdapterUtil;
import com.techjumper.polyhome.b.property.utils.TypeUtil;
import com.techjumper.polyhome.b.property.viewholder.databean.InfoRepairEntityBean;
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
@Presenter(RepairFragmentPresenter.class)
public class RepairFragment extends AppBaseFragment<RepairFragmentPresenter> {

    @Bind(R.id.list)
    RecyclerView list;
    @Bind(R.id.lar_checkbox_repair_windows)
    RadioButton larCheckboxRepairWindows;
    @Bind(R.id.lar_checkbox_repair_wall)
    RadioButton larCheckboxRepairWall;
    @Bind(R.id.lar_checkbox_repair_lift)
    RadioButton larCheckboxRepairLift;
    @Bind(R.id.lar_checkbox_repair_water)
    RadioButton larCheckboxRepairWater;
    @Bind(R.id.lar_checkbox_repair_lock)
    RadioButton larCheckboxRepairLock;
    @Bind(R.id.lar_checkbox_repair_common)
    RadioButton larCheckboxRepairCommon;
    @Bind(R.id.lar_checkbox_repair_personal)
    RadioButton larCheckboxRepairPersonal;
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.lar_mobile)
    EditText larMobile;
    @Bind(R.id.lar_content)
    EditText larContent;
    @Bind(R.id.lar_submit)
    TextView larSubmit;
    @Bind(R.id.lar_layout)
    LinearLayout larLayout;
    @Bind(R.id.layout_repair)
    LinearLayout layoutRepair;
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
    @Bind(R.id.lmd_message_content)
    EditText lmdMessageContent;
    @Bind(R.id.lmd_message_send)
    TextView lmdMessageSend;
    @Bind(R.id.lmd_layout)
    LinearLayout lmdLayout;
    @Bind(R.id.scrollView)
    NestedScrollView scrollView;

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

    public EditText getLmdMessageContent() {
        return lmdMessageContent;
    }

    public long getSendId() {
        return sendId;
    }

    public void setSendId(long sendId) {
        this.sendId = sendId;
    }

    public EditText getLarMobile() {
        return larMobile;
    }

    public EditText getLarContent() {
        return larContent;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public int getShowType() {
        return showType;
    }

    public long getInfoId() {
        return infoId;
    }

    public static RepairFragment getInstance(int type, int showType, long infoId) {
        RepairFragment repairFragment = new RepairFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.TYPE, type);
        bundle.putInt(MainActivity.SHOWTYPE, showType);
        bundle.putLong(MainActivity.ID, infoId);
        repairFragment.setArguments(bundle);
        return repairFragment;
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repair, null);
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

        larMobile.setText(UserInfoManager.getMobile());

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

    public void getRepairs(List<RepairEntity.RepairDataEntity> repairDataEntities, int page) {
        showList();

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

        if (page == 1) {
            adapter.loadData(displayBeans);
            list.setAdapter(adapter);
        } else {
            adapter.insertData(adapter.getItemCount(), displayBeans);
        }
    }

    public void showRepairDetailLmdLayout(RepairDetailEntity.RepairDetailDataEntity entity) {
        if (entity == null)
            return;

        lmdMessageContent.setText("");

        long id = entity.getId();
        long user_id = Long.valueOf(UserInfoManager.getUserId());

        showDetail();

        lmdTitle.setText(TypeUtil.getRepairTitle(entity.getRepair_type(), entity.getRepair_device()));
        lmdContent.setText(entity.getNote());
        lmdDate.setText(entity.getRepair_date().substring(0, 10));
        setSendId(id);
//        setActionType(ActionFragment.REPAIR);

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

        RxBus.INSTANCE.send(new BackEvent(BackEvent.PROPERTY_LIST));
    }

    private void showList() {
        layoutRepair.setVisibility(View.VISIBLE);
        larLayout.setVisibility(View.GONE);
        lmdLayout.setVisibility(View.GONE);
    }

    private void showDetail() {
        layoutRepair.setVisibility(View.GONE);
        larLayout.setVisibility(View.GONE);
        lmdLayout.setVisibility(View.VISIBLE);
    }

    private void showAction() {
        layoutRepair.setVisibility(View.GONE);
        larLayout.setVisibility(View.VISIBLE);
        lmdLayout.setVisibility(View.GONE);

        RxBus.INSTANCE.send(new BackEvent(BackEvent.PROPERTY_LIST));
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
}
