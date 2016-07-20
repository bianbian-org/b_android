package com.techjumper.polyhomeb.mvp.v.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.NewRepairActivityPhotoAdapter;
import com.techjumper.polyhomeb.mvp.p.activity.NewRepairActivityPresenter;

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

    private NewRepairActivityPhotoAdapter mAdapter;
    private ArrayList<String> mChoosedPhoto = new ArrayList<>();

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_new_repair);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRv.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        mAdapter = new NewRepairActivityPhotoAdapter();
        mRv.setAdapter(mAdapter);
        mAdapter.loadData(getPresenter().getDatas());
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

    public ArrayList<String> getPhotos() {
        return mChoosedPhoto;
    }

}
