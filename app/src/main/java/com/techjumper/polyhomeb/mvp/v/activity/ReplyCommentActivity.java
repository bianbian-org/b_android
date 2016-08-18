package com.techjumper.polyhomeb.mvp.v.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.utils.common.ResourceUtils;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.corelib.utils.window.StatusbarHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.ReplyCommentActivityPhotoAdapter;
import com.techjumper.polyhomeb.mvp.p.activity.ReplyCommentActivityPresenter;
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
 * Date: 16/8/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(ReplyCommentActivityPresenter.class)
public class ReplyCommentActivity extends AppBaseActivity<ReplyCommentActivityPresenter> {

    @Bind(R.id.layout)
    ScrollView mSv;
    @Bind(R.id.et_content)
    EditText mEtContent;
    @Bind(R.id.rv)
    RecyclerViewFinal mRv;
    @Bind(R.id.tv_input_number)
    TextView mTvInput;
    @Bind(R.id.tv_right)
    TextView mTvRight;
    @Bind(R.id.right_group)
    FrameLayout mLayoutRight;
    @Bind(R.id.title_group)
    View mTitle;

    //屏幕高度
    private int mScreenHeight = 0;

    //软键盘高度
    private int mHeight1, mHeight2, mIMEHeight = 0;

    private ReplyCommentActivityPhotoAdapter mAdapter;
    private ArrayList<String> mChoosedPhoto = new ArrayList<>();

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_replycomment);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRv.setLayoutManager(new GridLayoutManager(this, 1));
        mAdapter = new ReplyCommentActivityPhotoAdapter();
        mRv.setAdapter(mAdapter);
        mAdapter.loadData(getPresenter().getDatas());

        getIMEHeight();

        RxTextView.textChangeEvents(mEtContent).subscribe(textViewTextChangeEvent -> {
            int length = mEtContent.getText().toString().length();
            if (length >= 600) {
                mTvInput.setVisibility(View.VISIBLE);
                mTvInput.setText(String.format(getResources().getString(R.string.input_limit_800), length));
            } else {
                mTvInput.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.reply_comment);
    }

    @Override
    protected boolean showTitleRight() {
        return true;
    }

    @Override
    protected boolean onTitleLeftClick() {
        getPresenter().onTitleLeftClick();
        return true;
    }

    @Override
    protected boolean onTitleRightClick() {
        getPresenter().onTitleRightClick();
        return true;
    }

    @Override
    public void onBackPressed() {
        onTitleLeftClick();
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

            changeRightGroup((mChoosedPhoto.size() == 1
                    || mEtContent.getEditableText().toString().trim().length() != 0) ? true : false);
        }
    }

    public void changeRightGroup(boolean hasContent) {
        if (hasContent) {
            mTvRight.setTextColor(ResourceUtils.getColorResource(R.color.color_ffffff));
        } else {
            mTvRight.setTextColor(ResourceUtils.getColorResource(R.color.color_70ffffff));
        }
        mLayoutRight.setClickable(hasContent);
    }

    private void getIMEHeight() {
        mScreenHeight = getWindowManager().getDefaultDisplay().getHeight();
        SoftKeyboardUtil.observeSoftKeyboard(this, (softKeyboardHeight, visible) -> {
            if (!visible) {
                mHeight1 = softKeyboardHeight;
            } else {
                mHeight2 = softKeyboardHeight;
            }
            mIMEHeight = mHeight2 - mHeight1;
            ViewGroup.LayoutParams layoutParams = mEtContent.getLayoutParams();
            if (visible) {
                layoutParams.height = mScreenHeight - mIMEHeight - mTitle.getHeight() - mRv.getHeight() + RuleUtils.dp2Px(12) - mTvInput.getHeight() - StatusbarHelper.getStatusBarHeightPx(this);
                mEtContent.setLayoutParams(layoutParams);
            } else {
                layoutParams.height = RuleUtils.dp2Px(200);
                mEtContent.setLayoutParams(layoutParams);
            }
        });
    }

    public ArrayList<String> getPhotos() {
        return mChoosedPhoto;
    }

    public ReplyCommentActivityPhotoAdapter getAdapter() {
        return mAdapter;
    }

    public EditText getEtContent() {
        return mEtContent;
    }
}
