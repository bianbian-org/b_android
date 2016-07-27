package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.RepairDetailAdapter;
import com.techjumper.polyhomeb.mvp.p.activity.RepairDetailActivityPresenter;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(RepairDetailActivityPresenter.class)
public class RepairDetailActivity extends AppBaseActivity<RepairDetailActivityPresenter> implements View.OnLayoutChangeListener {

    @Bind(R.id.rv)
    RecyclerViewFinal mRv;
    @Bind(R.id.et_content)
    EditText mEtContent;
    @Bind(R.id.layout_root)
    View mRootView;
    @Bind(R.id.layout_static_head)
    View mStaticHead;
    @Bind(R.id.tv_send)
    TextView mTvSend;

    //屏幕高度
    private int mScreenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int mKeyHeight = 0;

    private RepairDetailAdapter mAdapter;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_repair_detail);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new RepairDetailAdapter();
        mRv.setAdapter(mAdapter);
        mAdapter.loadData(getPresenter().getDatas());

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
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > mKeyHeight)) {
            mStaticHead.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
            mRv.smoothScrollToPosition(mAdapter.getItemCount() + 1);  //mRv跳至最后一个item
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > mKeyHeight)) {
            mStaticHead.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
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
        RxTextView.afterTextChangeEvents(mEtContent).subscribe(textViewAfterTextChangeEvent -> {
            //文字行数改变的时候需要让rv跳转到最后一个item
            if (mEtContent.getLineCount() > 1) {
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

}
