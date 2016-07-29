package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.view.View;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairDetailProprietorContentData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairDetailProprietorContentBean;
import com.techjumper.polyhomeb.entity.event.PhotoViewEvent;
import com.techjumper.polyhomeb.mvp.m.RepairDetailActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.PicViewActivity;
import com.techjumper.polyhomeb.mvp.v.activity.RepairDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class RepairDetailActivityPresenter extends AppBaseActivityPresenter<RepairDetailActivity> {

    private RepairDetailActivityModel mModel = new RepairDetailActivityModel(this);

    private Subscription mSubs1;
    private String mCurrentUrl = "";

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        onPicClicked();
    }

    public List<DisplayBean> getDatas() {
        return mModel.getDatas();
    }

    private void onPicClicked() {

        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = RxBus.INSTANCE.asObservable().subscribe(o -> {
                    if (o instanceof PhotoViewEvent) {
                        PhotoViewEvent event = (PhotoViewEvent) o;
                        mCurrentUrl = event.getCurrentUrl();
                        ArrayList<String> list = new ArrayList<>();
                        list.add("http://h.hiphotos.baidu.com/baike/pic/item/6d81800a19d8bc3efeea65b98a8ba61ea9d345e6.jpg");
                        list.add("http://e.hiphotos.baidu.com/baike/pic/item/b999a9014c086e0659c4dc7507087bf40ad1cba4.jpg");
                        list.add("http://e.hiphotos.baidu.com/baike/pic/item/a5c27d1ed21b0ef4af04b077dec451da81cb3e2e.jpg");

                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.CURRENT_PIC_URL, mCurrentUrl);
                        bundle.putStringArrayList(Constant.ALL_PIC_URL, list);
                        new AcHelper.Builder(getView())
                                .extra(bundle)
                                .target(PicViewActivity.class)
                                .start();
                    }
                }));
    }

    @OnClick(R.id.tv_send)
    public void onClick(View view) {

        List<DisplayBean> datas = mModel.getDatas();

        //业主聊天
        PropertyRepairDetailProprietorContentData propertyRepairDetailProprietorContentData = new PropertyRepairDetailProprietorContentData();
        propertyRepairDetailProprietorContentData.setContent("美狗日杂些想作死就来");
        propertyRepairDetailProprietorContentData.setFailed(false);
        PropertyRepairDetailProprietorContentBean propertyRepairDetailProprietorContentBean = new PropertyRepairDetailProprietorContentBean(propertyRepairDetailProprietorContentData);
        datas.add(propertyRepairDetailProprietorContentBean);

        getView().getAdapter().loadData(datas);
        getView().getAdapter().notifyDataSetChanged();
        getView().getRv().smoothScrollToPosition(getView().getAdapter().getItemCount() + 1);


        //测试用的
        if (b) {
            b = !b;
        } else {
            b = !b;
        }

        getView().processChoosedPicLayout(b);

    }

    boolean b = false;

    public List<DisplayBean> alreadyChoosedPic() {
        return mModel.getAlreadyChoosedPic();
    }

}
