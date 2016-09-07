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
import com.techjumper.polyhomeb.adapter.ComplainDetailAdapter;
import com.techjumper.polyhomeb.adapter.ComplainDetailPicAdapter;
import com.techjumper.polyhomeb.entity.PropertyComplainDetailEntity;
import com.techjumper.polyhomeb.mvp.p.activity.ComplainDetailActivityPresenter;

import java.util.List;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(ComplainDetailActivityPresenter.class)
public class ComplainDetailActivity extends AppBaseActivity<ComplainDetailActivityPresenter> {

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
    @Bind(R.id.layout_input)
    RelativeLayout mLayoutInput;

    //屏幕高度
    private int mScreenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int mKeyHeight = 0;

    private ComplainDetailAdapter mAdapter;
    private ComplainDetailPicAdapter mAdapter_;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_complain_detail);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        processScreenHeightAndIME();
        processEditTextLines();
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.complain_detail);
    }

    @Override
    protected boolean useStatusBarTransform_() {
        return false;
    }

    @Override
    protected boolean canSlide2Close() {
        return false;
    }

    private void processScreenHeightAndIME() {
        //获取屏幕高度
        mScreenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        mKeyHeight = mScreenHeight / 3;
//        mRootView.addOnLayoutChangeListener(this);
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

    public ComplainDetailAdapter getAdapter() {
        return mAdapter;
    }

    public EditText getEtReplyContent() {
        return mEtReplyContent;
    }

    public TextView getTvSend() {
        return mTvSend;
    }

    //处理是不是含有图片
    private void processChoosedPicLayout(boolean hasPic, PropertyComplainDetailEntity propertyComplainDetailEntity) {
        if (hasPic) {
            ViewGroup.LayoutParams layoutParams = mLayoutNotice.getLayoutParams();
            layoutParams.height = RuleUtils.dp2Px(284);
            mLayoutNotice.setLayoutParams(layoutParams);
            mRvReceivedPic.setVisibility(View.VISIBLE);

            mRvReceivedPic.setLayoutManager(new GridLayoutManager(this, 3));
            mAdapter_ = new ComplainDetailPicAdapter();
            mRvReceivedPic.setAdapter(mAdapter_);
            mAdapter_.loadData(getPresenter().alreadyChoosedPic(propertyComplainDetailEntity));

        } else {
            mRvReceivedPic.setVisibility(View.GONE);
            ViewGroup.LayoutParams layoutParams = mLayoutNotice.getLayoutParams();
            layoutParams.height = RuleUtils.dp2Px(200);
            mLayoutNotice.setLayoutParams(layoutParams);
        }
    }

    public void onComplainDataReceive(PropertyComplainDetailEntity propertyComplainDetailEntity) {
        mTvTitle.setText(getPresenter().getTitle(propertyComplainDetailEntity.getData().getTypes()));
        mTvContent.setText(propertyComplainDetailEntity.getData().getContent());
        mTvTime.setText(getPresenter().getMonthAndDayTime(propertyComplainDetailEntity.getData().getCreated_at()));
        mTvTime.setVisibility(View.VISIBLE);
        mBtnStatus.setText(getPresenter().getStatusName(propertyComplainDetailEntity.getData().getStatus()));
        mBtnStatus.setVisibility(View.VISIBLE);
        if (propertyComplainDetailEntity.getData().getStatus() == 2 || propertyComplainDetailEntity.getData().getStatus() == 3) {
            mBtnStatus.setTextColor(ResourceUtils.getColorResource(R.color.color_acacac));
            mBtnStatus.setEnabled(false);
        } else {
            mBtnStatus.setTextColor(ResourceUtils.getColorResource(R.color.color_37a991));
            mBtnStatus.setEnabled(true);
        }

        String[] imgs = propertyComplainDetailEntity.getData().getImgs();
        if (imgs != null && imgs.length != 0) {
            processChoosedPicLayout(true, propertyComplainDetailEntity);
        } else {
            processChoosedPicLayout(false, null);
        }

        List<PropertyComplainDetailEntity.DataBean.RepliesBean> replies = propertyComplainDetailEntity.getData().getReplies();
        if (replies != null && replies.size() != 0) {
            processHasReply(true, replies);
        } else {
            processHasReply(false, null);
        }
    }

    private void processHasReply(boolean hasReply, List<PropertyComplainDetailEntity.DataBean.RepliesBean> replies) {
        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ComplainDetailAdapter();
        if (hasReply) {
            mRv.setAdapter(mAdapter);
            mAdapter.loadData(getPresenter().getReplyDatas(replies));
        } else {
            mRv.setAdapter(mAdapter);
            mAdapter.loadData(getPresenter().getEmptyData());
        }
    }

}
