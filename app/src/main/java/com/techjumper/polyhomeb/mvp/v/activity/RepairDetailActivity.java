package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.utils.common.ResourceUtils;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.RepairDetailAdapter;
import com.techjumper.polyhomeb.adapter.RepairDetailPicAdapter;
import com.techjumper.polyhomeb.entity.PropertyRepairDetailEntity;
import com.techjumper.polyhomeb.mvp.p.activity.RepairDetailActivityPresenter;

import java.util.List;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/6
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(RepairDetailActivityPresenter.class)
public class RepairDetailActivity extends AppBaseActivity<RepairDetailActivityPresenter>
        implements View.OnLayoutChangeListener {

    @Bind(R.id.rv)
    RecyclerViewFinal mRv;
    @Bind(R.id.et_reply_content)
    EditText mEtReplyContent;
    @Bind(R.id.layout_root)
    View mRootView;
    @Bind(R.id.layout_static_head)
    View mStaticHead;
    @Bind(R.id.tv_send)
    TextView mTvSend;
    @Bind(R.id.tv_title_)
    TextView mTvTitle;
    @Bind(R.id.tv_content)
    TextView mTvContent;
    @Bind(R.id.btn)
    Button mBtnStatus;
    @Bind(R.id.tv_time)
    TextView mTvTime;

    //默认是有图片
    //当有图片的时候,mLayoutNotice的高度是284DP,其中70DP是mRvReceivedPic,14DP是mRvReceivedPic的margin
    //当没有图片的时候,需要将mLayoutNotice的高度设为200DP
    @Bind(R.id.layout_notice)
    LinearLayout mLayoutNotice;
    @Bind(R.id.rv_received_pic)
    RecyclerViewFinal mRvReceivedPic;

    //屏幕高度
    private int mScreenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int mKeyHeight = 0;

    private RepairDetailAdapter mAdapter;
    private RepairDetailPicAdapter mAdapter_;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_repair_detail);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        processScreenHeightAndIME();
        processEditTextLines();
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.repair_detail);
    }

    @Override
    protected boolean useStatusBarTransform_() {
        return false;
    }

    @Override
    protected boolean canSlide2Close() {
        return false;
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > mKeyHeight)) {
            mStaticHead.setVisibility(View.GONE);
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
            mRv.smoothScrollToPosition(mAdapter.getItemCount() + 1);  //mRv跳至最后一个item
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > mKeyHeight)) {
            mStaticHead.setVisibility(View.VISIBLE);
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    private void processScreenHeightAndIME() {
        //获取屏幕高度
        mScreenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        mKeyHeight = mScreenHeight / 3;
        mRootView.addOnLayoutChangeListener(this);
    }

    private void processEditTextLines() {
        RxTextView.afterTextChangeEvents(mEtReplyContent).subscribe(textViewAfterTextChangeEvent -> {
            //文字行数改变的时候需要让rv跳转到最后一个item
            if (mEtReplyContent.getLineCount() > 1) {
                mRv.smoothScrollToPosition(mAdapter.getItemCount() + 1);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mTvSend.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_BASELINE, R.id.et_content);
                mTvSend.setLayoutParams(layoutParams);
            } else {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mTvSend.getLayoutParams();
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                mTvSend.setLayoutParams(layoutParams);
            }
        });
    }

    public RecyclerViewFinal getRv() {
        return mRv;
    }

    public RepairDetailAdapter getAdapter() {
        return mAdapter;
    }

    public EditText getEtReplyContent() {
        return mEtReplyContent;
    }

    public TextView getTvSend() {
        return mTvSend;
    }

    //处理是不是含有图片
    private void processChoosedPicLayout(boolean hasPic, PropertyRepairDetailEntity propertyRepairDetailEntity) {
        if (hasPic) {
            ViewGroup.LayoutParams layoutParams = mLayoutNotice.getLayoutParams();
            layoutParams.height = RuleUtils.dp2Px(284);
            mLayoutNotice.setLayoutParams(layoutParams);
            mRvReceivedPic.setVisibility(View.VISIBLE);

            mRvReceivedPic.setLayoutManager(new GridLayoutManager(this, 3));
            mAdapter_ = new RepairDetailPicAdapter();
            mRvReceivedPic.setAdapter(mAdapter_);
            mAdapter_.loadData(getPresenter().alreadyChoosedPic(propertyRepairDetailEntity));

        } else {
            mRvReceivedPic.setVisibility(View.GONE);
            ViewGroup.LayoutParams layoutParams = mLayoutNotice.getLayoutParams();
            layoutParams.height = RuleUtils.dp2Px(200);
            mLayoutNotice.setLayoutParams(layoutParams);
        }
    }

    public void onRepairDataReceive(PropertyRepairDetailEntity propertyRepairDetailEntity) {
        mTvTitle.setText(getPresenter().getTitle(propertyRepairDetailEntity.getData().getRepair_type(), propertyRepairDetailEntity.getData().getRepair_device()));
        mTvContent.setText(propertyRepairDetailEntity.getData().getNote());
        mTvTime.setText(getPresenter().getMonthAndDayTime(propertyRepairDetailEntity.getData().getRepair_date()));
        mTvTime.setVisibility(View.VISIBLE);
        mBtnStatus.setText(getPresenter().getStatusName(propertyRepairDetailEntity.getData().getStatus()));
        mBtnStatus.setVisibility(View.VISIBLE);
        if (propertyRepairDetailEntity.getData().getStatus() == 2 || propertyRepairDetailEntity.getData().getStatus() == 3) {
            mBtnStatus.setTextColor(ResourceUtils.getColorResource(R.color.color_acacac));
            mBtnStatus.setEnabled(false);
        } else {
            mBtnStatus.setTextColor(ResourceUtils.getColorResource(R.color.color_37a991));
            mBtnStatus.setEnabled(true);
        }

        String[] imgs = propertyRepairDetailEntity.getData().getImgs();
        if (imgs != null && imgs.length != 0) {
            processChoosedPicLayout(true, propertyRepairDetailEntity);
        } else {
            processChoosedPicLayout(false, null);
        }

        List<PropertyRepairDetailEntity.DataBean.RepliesBean> replies = propertyRepairDetailEntity.getData().getReplies();
        if (replies != null && replies.size() != 0) {
            processHasReply(true, replies);
        } else {
            processHasReply(false, null);
        }
    }

    private void processHasReply(boolean hasReply, List<PropertyRepairDetailEntity.DataBean.RepliesBean> replies) {
        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new RepairDetailAdapter();
        if (hasReply) {
            mRv.setAdapter(mAdapter);
            mAdapter.loadData(getPresenter().getReplyDatas(replies));
        } else {
            mRv.setAdapter(mAdapter);
            mAdapter.loadData(getPresenter().getEmptyData());
        }
    }
}
