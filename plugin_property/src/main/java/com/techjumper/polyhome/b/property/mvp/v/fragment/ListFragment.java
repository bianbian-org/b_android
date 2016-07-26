package com.techjumper.polyhome.b.property.mvp.v.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.steve.creact.library.adapter.CommonRecyclerAdapter;
import com.steve.creact.library.display.DisplayBean;
import com.techjumper.commonres.ComConstant;
import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.ComplaintDetailEntity;
import com.techjumper.commonres.entity.ComplaintEntity;
import com.techjumper.commonres.entity.RepairDetailEntity;
import com.techjumper.commonres.entity.RepairEntity;
import com.techjumper.commonres.entity.ReplyEntity;
import com.techjumper.commonres.entity.event.PropertyNormalDetailEvent;
import com.techjumper.commonres.entity.event.loadmoreevent.LoadmorePresenterEvent;
import com.techjumper.commonres.util.CommonDateUtil;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.property.Constant;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.UserInfoManager;
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

    public static final String LISTTYPE = "listtype";
    public static final String SHOWTYPE = "showtype";
    public static final String INFOID = "infoid";

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
    WebView lndContent;
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
    @Bind(R.id.scrollView)
    NestedScrollView scrollView;
    @Bind(R.id.fl_title_repair)
    RadioButton flTitleRepair;
    @Bind(R.id.fl_title_complaint)
    RadioButton flTitleComplaint;
    @Bind(R.id.fl_title_announcement)
    RadioButton flTitleAnnouncement;
    @Bind(R.id.lac_layout)
    LinearLayout lacLayout;
    @Bind(R.id.lar_layout)
    LinearLayout larLayout;
    @Bind(R.id.list_layout)
    LinearLayout listLayout;
    @Bind(R.id.lac_mobile)
    EditText lacMobile;
    @Bind(R.id.lac_content)
    EditText lacContent;
    @Bind(R.id.lar_mobile)
    EditText larMobile;
    @Bind(R.id.lar_content)
    EditText larContent;

    private CommonRecyclerAdapter adapter;
    private CommonRecyclerAdapter messageAdapter;
    private LinearLayoutManager manager = new LinearLayoutManager(getActivity());

    private int type;
    private long sendId;
    private int actionType;
    private int listType;

    private long infoId;
    private int showType;

    public static ListFragment getInstance(int listType, int showType, long infoId) {
        ListFragment listFragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LISTTYPE, listType);
        bundle.putInt(SHOWTYPE, showType);
        bundle.putLong(INFOID, infoId);
        listFragment.setArguments(bundle);
        return listFragment;
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

    public long getInfoId() {
        return infoId;
    }

    public int getShowType() {
        return showType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public EditText getLmdMessageContent() {
        return lmdMessageContent;
    }

    public int getListType() {
        return listType;
    }

    public void setListType(int listType) {
        this.listType = listType;
    }

    public LinearLayout getLacLayout() {
        return lacLayout;
    }

    public LinearLayout getLarLayout() {
        return larLayout;
    }

    public EditText getLarContent() {
        return larContent;
    }

    public EditText getLarMobile() {
        return larMobile;
    }

    public EditText getLacContent() {
        return lacContent;
    }

    public EditText getLacMobile() {
        return lacMobile;
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            listType = bundle.getInt(LISTTYPE, MainActivity.REPAIR);
            showType = bundle.getInt(SHOWTYPE);
            infoId = bundle.getLong(INFOID);
        }

        Log.d("wowo", "list获取showType :" + showType + "list获取infoId :" + infoId);

        if (listType == MainActivity.ANNOUNCEMENT) {
            flTitleAnnouncement.setChecked(true);
        } else if (listType == MainActivity.REPAIR) {
            flTitleRepair.setChecked(true);
        } else {
            flTitleComplaint.setChecked(true);
        }

        setListType(listType);

        adapter = new CommonRecyclerAdapter();
        messageAdapter = new CommonRecyclerAdapter();

        flList.setLayoutManager(manager);
        lmdList.setLayoutManager(new LinearLayoutManager(getContext()));

        lmdMessageContent.setImeOptions(EditorInfo.IME_ACTION_DONE);

        flList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        lacMobile.setText(UserInfoManager.getMobile());
        larMobile.setText(UserInfoManager.getMobile());

        if (showType == -1) {
            showListLayout();
        }
    }

    public void getAnnouncements(List<AnnouncementEntity.AnnouncementDataEntity> announcementDataEntities, int page) {
        flTitleAction.setVisibility(View.GONE);
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

        if (page == 1) {
            adapter.loadData(displayBeans);
            flList.setAdapter(adapter);
        } else {
            adapter.insertData(adapter.getItemCount(), displayBeans);
        }
    }

    public void getComplaints(List<ComplaintEntity.ComplaintDataEntity> complaintDataEntities, int page) {
        flTitleAction.setText(R.string.property_new_complaint);
        flTitleAction.setVisibility(View.VISIBLE);
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

        if (page == 1) {
            adapter.loadData(displayBeans);
            flList.setAdapter(adapter);
        } else {
            adapter.insertData(adapter.getItemCount(), displayBeans);
        }
    }

    public void getRepairs(List<RepairEntity.RepairDataEntity> repairDataEntities, int page) {
        flTitleAction.setText(R.string.property_new_repair);
        flTitleAction.setVisibility(View.VISIBLE);
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

        if (page == 1) {
            adapter.loadData(displayBeans);
            flList.setAdapter(adapter);
        } else {
            adapter.insertData(adapter.getItemCount(), displayBeans);
        }
    }

    public void showLndLayout(PropertyNormalDetailEvent event) {
        if (event == null)
            return;

        showWebview();

        lndTitle.setText(event.getTitle());
        lndDate.setText(event.getDate());

        lndContent.loadDataWithBaseURL(null, event.getContent(), "text/html", "utf-8", null);
        lndContent.getSettings().setJavaScriptEnabled(true);
        lndContent.setWebChromeClient(new WebChromeClient());
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
        setActionType(ActionFragment.COMPLAINT);

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
        setActionType(ActionFragment.REPAIR);

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

    public void showListLayout() {
        lacLayout.setVisibility(View.GONE);
        larLayout.setVisibility(View.GONE);
        lndLayout.setVisibility(View.GONE);
        lmdLayout.setVisibility(View.GONE);
        listLayout.setVisibility(View.VISIBLE);
    }

    public void showActionComplaint() {
        lacLayout.setVisibility(View.VISIBLE);
        larLayout.setVisibility(View.GONE);
        lndLayout.setVisibility(View.GONE);
        lmdLayout.setVisibility(View.GONE);
        listLayout.setVisibility(View.GONE);
    }

    public void showActionRepair() {
        lacLayout.setVisibility(View.GONE);
        larLayout.setVisibility(View.VISIBLE);
        lndLayout.setVisibility(View.GONE);
        lmdLayout.setVisibility(View.GONE);
        listLayout.setVisibility(View.GONE);
    }

    public void showDetail() {
        lacLayout.setVisibility(View.GONE);
        larLayout.setVisibility(View.GONE);
        lndLayout.setVisibility(View.GONE);
        lmdLayout.setVisibility(View.VISIBLE);
        listLayout.setVisibility(View.GONE);
    }

    public void showWebview() {
        lacLayout.setVisibility(View.GONE);
        larLayout.setVisibility(View.GONE);
        lndLayout.setVisibility(View.VISIBLE);
        lmdLayout.setVisibility(View.GONE);
        listLayout.setVisibility(View.GONE);
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
