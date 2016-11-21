package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyData;
import com.techjumper.polyhomeb.entity.MarqueeTextInfoEntity;
import com.techjumper.polyhomeb.mvp.v.activity.ComplainDetailActivity;
import com.techjumper.polyhomeb.mvp.v.activity.PaymentActivity;
import com.techjumper.polyhomeb.mvp.v.activity.PlacardDetailActivity;
import com.techjumper.polyhomeb.mvp.v.activity.PropertyDetailActivity;
import com.techjumper.polyhomeb.mvp.v.activity.RepairDetailActivity;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "PropertyDataBean", data = PropertyData.class)
public class PropertyViewHolder extends BaseRecyclerViewHolder<PropertyData> {

    public static final int LAYOUT_ID = R.layout.item_home_page_property;

    private Subscription mSubs1;

    public PropertyViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(PropertyData data) {
        if (data == null) return;
        //        removeHandler();
        RxUtils.unsubscribeIfNotNull(mSubs1);

        setMarqueeTextData(data.getNotice());

        //公告
        setOnClickListener(R.id.placard, v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.KEY_CURRENT_BUTTON, Constant.VALUE_PLACARD);
            new AcHelper.Builder((Activity) getContext())
                    .target(PropertyDetailActivity.class)
                    .extra(bundle)
                    .start();
        });

//        boolean hasAuthority = UserManager.INSTANCE.hasAuthority();
        boolean family = UserManager.INSTANCE.isFamily();
        //维修
        setOnClickListener(R.id.repair, v -> {
            if (family) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.KEY_CURRENT_BUTTON, Constant.VALUE_REPAIR);
                new AcHelper.Builder((Activity) getContext())
                        .target(PropertyDetailActivity.class)
                        .extra(bundle)
                        .start();
            } else {
                ToastUtils.show(getContext().getString(R.string.no_authority));
            }
        });

        //投诉
        setOnClickListener(R.id.complaint, v -> {
            if (family) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.KEY_CURRENT_BUTTON, Constant.VALUE_COMPLAINT);
                new AcHelper.Builder((Activity) getContext())
                        .target(PropertyDetailActivity.class)
                        .extra(bundle)
                        .start();
            } else {
                ToastUtils.show(getContext().getString(R.string.no_authority));
            }
        });

        //缴费
        setOnClickListener(R.id.payment, v -> {
            if (family) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.KEY_CURRENT_BUTTON, Constant.VALUE_PAYMENT);
                new AcHelper.Builder((Activity) getContext())
                        .target(PaymentActivity.class)
                        .extra(bundle)
                        .start();
            } else {
                ToastUtils.show(getContext().getString(R.string.no_authority));
            }
        });
    }

    private void setMarqueeTextData(MarqueeTextInfoEntity entity) {
        if (entity == null) {
            setText(R.id.tv_notice, getContext().getString(R.string.im_no_message));
            setOnClickListener(R.id.btn_checkout_property, null);
        } else {
            List<MarqueeTextInfoEntity.DataBean.MessagesBean> messages = entity.getData().getMessages();
//            Message message = mHandler.obtainMessage();
//            message.what = 0;
//            message.obj = messages;
//            mHandler.sendMessageDelayed(message, 1000);
            setData(messages.get(0));
            mSubs1 = Observable.interval(10, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Long>() {
                        @Override
                        public void onCompleted() {
                            RxUtils.unsubscribeIfNotNull(mSubs1);
                        }

                        @Override
                        public void onError(Throwable e) {
                            JLog.e(e.getMessage().toString());
                        }

                        @Override
                        public void onNext(Long aLong) {
                            MarqueeTextInfoEntity.DataBean.MessagesBean messagesBean
                                    = messages.get((int) (aLong >= messages.size() ? aLong % messages.size() : aLong));
                            setData(messagesBean);
                        }
                    });
        }
    }

//    private int count = -1;

//    private Handler mHandler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0:
//                    count++;
//                    List<MarqueeTextInfoEntity.DataBean.MessagesBean> beans = (List<MarqueeTextInfoEntity.DataBean.MessagesBean>) msg.obj;
//                    MarqueeTextInfoEntity.DataBean.MessagesBean messagesBean = beans.get(count >= beans.size() ? count % beans.size() : count);
//                    mTv.setText("");
//                    mTv.setText(messagesBean.getContent());
//                    setOnClickListener(R.id.btn_checkout_property, null);
//                    setOnClickListener(R.id.btn_checkout_property, v -> {
//                        jump2Activity(messagesBean);
//                    });
//                    Message message = mHandler.obtainMessage();
//                    message.what = 0;
//                    message.obj = beans;
//                    mHandler.sendMessageDelayed(message, 10000);
//                    break;
//            }
//            return false;
//        }
//    });

    private void setData(MarqueeTextInfoEntity.DataBean.MessagesBean messagesBean) {
        setText(R.id.tv_notice, "");
        setText(R.id.tv_notice, messagesBean.getContent());
        setOnClickListener(R.id.btn_checkout_property, null);
        setOnClickListener(R.id.btn_checkout_property, v -> {
            jump2Activity(messagesBean);
        });
    }

    private void jump2Activity(MarqueeTextInfoEntity.DataBean.MessagesBean messagesBean) {
        String content = messagesBean.getContent();
        int has_read = messagesBean.getHas_read();
        String obj_id = messagesBean.getObj_id();
        String title = messagesBean.getTitle();
        int types = messagesBean.getTypes();
        int created_at = messagesBean.getCreated_at();

        if (TextUtils.isEmpty(obj_id)) return;
        //1-系统信息 2-订单信息 4-物业信息
        switch (types) {
            case 2:
                Bundle bundle2 = new Bundle();
//                bundle2.putInt(Constant.PLACARD_DETAIL_ID, data.getId());
                bundle2.putString(Constant.PLACARD_DETAIL_CONTENT, content);
                bundle2.putString(Constant.PLACARD_DETAIL_TIME, created_at + "");
                bundle2.putString(Constant.PLACARD_DETAIL_TITLE, title);
                bundle2.putString(Constant.PLACARD_DETAIL_TYPE, types + "");
                new AcHelper.Builder((Activity) getContext()).extra(bundle2).target(PlacardDetailActivity.class).start();
                break;
            case 4:
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.PROPERTY_REPAIR_DATA_ID, Integer.parseInt(obj_id));
                new AcHelper.Builder((Activity) getContext()).extra(bundle).target(RepairDetailActivity.class).start();
                break;
            case 5:
                Bundle bundle1 = new Bundle();
                bundle1.putInt(Constant.PROPERTY_COMPLAIN_DATA_ID, Integer.parseInt(obj_id));
                new AcHelper.Builder((Activity) getContext()).extra(bundle1).target(ComplainDetailActivity.class).start();
                break;
        }
    }

//    public void removeHandler() {
//        if (mHandler == null) return;
//        mHandler.removeMessages(0);
//    }

}
