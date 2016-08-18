package com.techjumper.polyhomeb.mvp.v.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.corelib.utils.window.StatusbarHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.NewRepairActivityPhotoAdapter;
import com.techjumper.polyhomeb.mvp.p.activity.NewRepairActivityPresenter;
import com.techjumper.polyhomeb.utils.SoftKeyboardUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/20
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(NewRepairActivityPresenter.class)
public class NewRepairActivity extends AppBaseActivity<NewRepairActivityPresenter> {

    @Bind(R.id.rv)
    RecyclerViewFinal mRv;
    @Bind(R.id.iv_triangle_first)
    ImageView mIvTriangleDevice;
    @Bind(R.id.iv_triangle_second)
    ImageView mIvTriangleType;
    @Bind(R.id.tv_type_first)
    TextView mTvDevice;
    @Bind(R.id.tv_type_second)
    TextView mTvType;
    @Bind(R.id.et_content)
    EditText mEtContent;
    @Bind(R.id.right_group)
    FrameLayout mRightGroup;
    @Bind(R.id.layout_static_head)
    LinearLayout mStaticHead;
    @Bind(R.id.layout)
    ScrollView mSv;
    @Bind(R.id.tv_input_number)
    TextView mTvInput;
    @Bind(R.id.title_group)
    View mTitle;
    @Bind(R.id.et_phone)
    EditText mEtPhone;

    //屏幕高度
    private int mScreenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int mKeyHeight = 0;

    private boolean isIMEVisible = false;

    //软键盘高度
    private int mHeight1, mHeight2, mIMEHeight = 0;

    private NewRepairActivityPhotoAdapter mAdapter;
    private ArrayList<String> mChoosedPhoto = new ArrayList<>();

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_new_repair);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRv.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new NewRepairActivityPhotoAdapter();
        mRv.setAdapter(mAdapter);
        mAdapter.loadData(getPresenter().getDatas());

        getIMEHeight();
        processScreenHeightAndIME();
        dispatchScrollEvent();

        RxTextView.textChangeEvents(mEtContent).subscribe(textViewTextChangeEvent -> {
            int length = mEtContent.getText().toString().length();
            if (length >= 800) {
                mTvInput.setVisibility(View.VISIBLE);
                mTvInput.setText(String.format(getResources().getString(R.string.input_limit_1000), length));
            } else {
                mTvInput.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected boolean useStatusBarTransform_() {
        return false;
    }

    @Override
    protected boolean showTitleRight() {
        return true;
    }

    @Override
    protected boolean onTitleRightClick() {
        getPresenter().onTitleRightClick();
        return true;
    }

    @Override
    public String getLayoutTitle() {
        return getResources().getString(R.string.new_repair);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            mChoosedPhoto.clear();
            if (photos != null) {
                mChoosedPhoto.addAll(photos);
            }

            mAdapter.loadData(getPresenter().getDatas());
            mAdapter.notifyDataSetChanged();
        }
    }

    private void processScreenHeightAndIME() {
        //获取屏幕高度
        mScreenHeight = getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        mKeyHeight = mScreenHeight / 3;
    }

    private void getIMEHeight() {
        SoftKeyboardUtil.observeSoftKeyboard(this, (softKeyboardHeight, visible) -> {
            if (!visible) {
                mHeight1 = softKeyboardHeight;
            } else {
                mHeight2 = softKeyboardHeight;
            }
            mIMEHeight = mHeight2 - mHeight1;

            if (mEtContent.hasFocus()) {  //只有当输入内容的时候才会进行以下操作,输入手机号码的时候则不会
                if (visible && !isIMEVisible) {
                    mStaticHead.setVisibility(View.GONE);
//                JLog.e("mScreenHeight..." + mScreenHeight + "mIMEHeight..." + mIMEHeight + "mTitle.getHeight()..." + mTitle.getHeight() + " mTvInput.getHeight()..." + mTvInput.getHeight());
                    ViewGroup.LayoutParams layoutParams = mEtContent.getLayoutParams();
                    layoutParams.height = mScreenHeight - mIMEHeight - mTitle.getHeight() - mRv.getHeight() - mTvInput.getHeight() - RuleUtils.dp2Px(14) - StatusbarHelper.getStatusBarHeightPx(this);
                    mEtContent.setLayoutParams(layoutParams);
                    mEtContent.requestLayout();
                    mSv.smoothScrollTo(0, 0);
                    isIMEVisible = true;
                } else if (!visible && isIMEVisible) {
                    mStaticHead.setVisibility(View.VISIBLE);
                    ViewGroup.LayoutParams layoutParams = mEtContent.getLayoutParams();
                    layoutParams.height = RuleUtils.dp2Px(200);
                    mEtContent.setLayoutParams(layoutParams);
                    mEtContent.requestLayout();
                    isIMEVisible = false;
                }
            }
        });
    }

    private void dispatchScrollEvent() {
        mEtContent.setOnTouchListener((v, event) -> {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mSv.requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_UP:
                    mSv.requestDisallowInterceptTouchEvent(false);
                    break;
            }
            return false;
        });
    }

    public ArrayList<String> getPhotos() {
        return mChoosedPhoto;
    }

    public NewRepairActivityPhotoAdapter getAdapter() {
        return mAdapter;
    }

    public ImageView getIvTriangleDevice() {
        return mIvTriangleDevice;
    }

    public ImageView getIvTriangleType() {
        return mIvTriangleType;
    }

    public TextView getTvDevice() {
        return mTvDevice;
    }

    public TextView getTvType() {
        return mTvType;
    }

    public EditText getEtContent() {
        return mEtContent;
    }

    public FrameLayout getRightGroup() {
        return mRightGroup;
    }

    public EditText getEtPhone() {
        return mEtPhone;
    }


}
