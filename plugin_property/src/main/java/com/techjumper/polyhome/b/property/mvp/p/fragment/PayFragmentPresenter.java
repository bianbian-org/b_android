package com.techjumper.polyhome.b.property.mvp.p.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.techjumper.commonres.ComConstant;
import com.techjumper.commonres.entity.AlipayEntity;
import com.techjumper.commonres.entity.BaseEntity;
import com.techjumper.commonres.entity.PayEntity;
import com.techjumper.commonres.entity.WxpayEntity;
import com.techjumper.commonres.entity.event.BackEvent;
import com.techjumper.commonres.entity.event.PayEvent;
import com.techjumper.commonres.entity.event.PayShowEvent;
import com.techjumper.commonres.entity.event.PropertyListEvent;
import com.techjumper.commonres.entity.event.loadmoreevent.LoadmorePresenterEvent;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhome.b.property.Constant;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.mvp.m.PayFragmentModel;
import com.techjumper.polyhome.b.property.mvp.v.activity.MainActivity;
import com.techjumper.polyhome.b.property.mvp.v.fragment.PayFragment;
import com.techjumper.polyhome.b.property.utils.StringUtil;
import com.techjumper.polyhome.b.property.widget.PayCheckView;

import java.util.Hashtable;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/11/9.
 */

public class PayFragmentPresenter extends AppBaseFragmentPresenter<PayFragment> {
    private PayFragmentModel model = new PayFragmentModel(this);
    private int pageNo = 1;
    private int status = PayEntity.NO;

    private PayCheckView detailWechat;
    private PayCheckView detailAlipay;
    private PayEntity.PayItemEntity entity;
    private ImageView qrImg;

    private String payType = ComConstant.PAY_WECHAT;

    @OnCheckedChanged(R.id.rb_no)
    void checkNo(boolean check) {
        if (check) {
            getOrders(PayEntity.NO);
        }
    }

    @OnCheckedChanged(R.id.rb_yes)
    void checkYes(boolean check) {
        if (check) {
            getOrders(PayEntity.YES);
        }
    }

    @OnClick(R.id.detail_wechat)
    void wechat() {
        if (!detailWechat.isCheck()) {
            detailWechat.setCheck(true);
            detailAlipay.setCheck(false);
        }

        payType = ComConstant.PAY_WECHAT;
    }

    @OnClick(R.id.detail_alipay)
    void alipay() {
        if (!detailAlipay.isCheck()) {
            detailWechat.setCheck(false);
            detailAlipay.setCheck(true);
        }

        payType = ComConstant.PAY_ALIPAY;
    }

    @OnClick(R.id.detail_submit)
    void submitPay() {
        if (entity == null)
            return;

        if (payType == ComConstant.PAY_WECHAT) {
            getWxpay(StringUtil.getIp(getView().getActivity()), entity.getOrder_number());
        } else {
            getAlipay(StringUtil.getIp(getView().getActivity()), entity.getOrder_number());
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getOrders(status);

        detailWechat = getView().getDetailWechat();
        detailAlipay = getView().getDetailAlipay();
        qrImg = getView().getResultQrcode();

        addSubscription(RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof LoadmorePresenterEvent) {
                        LoadmorePresenterEvent event = (LoadmorePresenterEvent) o;
                        int type = event.getType();
                        pageNo++;
                        if (type == MainActivity.PAY) {
                            getOrders(status);
                        }
                    } else if (o instanceof PropertyListEvent) {
                        if (getView().getType() == MainActivity.PAY) {
                            getOrders(status);
                        }
                        RxBus.INSTANCE.send(new BackEvent(BackEvent.FINISH));
                    } else if (o instanceof PayShowEvent) {
                        PayShowEvent event = (PayShowEvent) o;
                        entity = event.getItemEntity();
                        getView().showPayDetail(event.getItemEntity());
                    } else if (o instanceof PayEvent) {
                        PayEvent event = (PayEvent) o;
                        if (event.getType() == PayEvent.DETAIL) {
                            getOrders(status);
                        } else {
                            getView().showPayDetail(entity);
                        }
                    }
                }));
    }

    public void getOrders(int status) {
        addSubscription(model.getOrders(status, pageNo)
                .subscribe(new Subscriber<PayEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(PayEntity payEntity) {
                        if (!processNetworkResult(payEntity, false))
                            return;

                        if (payEntity == null
                                || payEntity.getData() == null
                                || payEntity.getData().getOrders() == null)
                            return;

                        PayEntity.PayDataEntity payDataEntity = payEntity.getData();

                        if (status == PayEntity.YES) {
                            getView().getTotalPrice().setText(String.format(getView().getString(R.string.property_pay_total_yes), payDataEntity.getTotal_price()));
                        } else {
                            getView().getTotalPrice().setText(String.format(getView().getString(R.string.property_pay_total_no), payDataEntity.getTotal_price()));
                        }
                        getView().getOrders(payEntity.getData().getOrders(), pageNo);
                    }
                }));
    }

    public void getWxpay(String ip, String order_number) {
        addSubscription(model.getWxpay(ip, order_number)
                .subscribe(new Subscriber<WxpayEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(WxpayEntity wxpayEntity) {
                        if (!processNetworkResult(wxpayEntity, false))
                            return;

                        if (wxpayEntity == null
                                || wxpayEntity.getData() == null
                                || wxpayEntity.getData().getWxpay() == null)
                            return;

                        if (entity == null)
                            return;

                        WxpayEntity.WxpayItemEntity itemEntity = wxpayEntity.getData().getWxpay();

                        if (itemEntity == null || TextUtils.isEmpty(itemEntity.getCode_url())) {
                            ToastUtils.show("出错啦~请稍后再试");
                            return;
                        }

                        getQrCode(wxpayEntity.getData().getWxpay().getCode_url());
                        getView().showPayResult(entity);
                    }
                }));
    }

    public void getAlipay(String ip, String order_number) {
        addSubscription(model.getAlipay(ip, order_number)
                .subscribe(new Subscriber<AlipayEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(AlipayEntity alipayEntity) {
                        if (!processNetworkResult(alipayEntity, false))
                            return;

                        if (alipayEntity == null
                                || alipayEntity.getData() == null)
                            return;

                        if (entity == null)
                            return;

                        String codeUrl = alipayEntity.getData().getAlipay();

                        if (TextUtils.isEmpty(codeUrl)) {
                            ToastUtils.show("出错啦~请稍后再试");
                            return;
                        }

                        getQrCode(codeUrl);
                        getView().showPayResult(entity);
                    }
                }));
    }

    private void getQrCode(String url) {
        addSubscription(
                Observable
                        .<Bitmap>create(subscriber -> {
                            subscriber.onNext(createQRImage(url));
                            subscriber.onCompleted();
                        })
                        .compose(CommonWrap.wrap())
                        .subscribe(new Subscriber<Bitmap>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Bitmap bitmap) {
                                if (bitmap != null) {
                                    qrImg.setImageBitmap(bitmap);
                                }
                            }
                        })
        );
    }

    public Bitmap createQRImage(String url) {
        Bitmap qrBitmap = null;
        try {
            //判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1) {
                return null;
            }
            int QR_WIDTH = RuleUtils.dp2Px(200);
            int QR_HEIGHT = QR_WIDTH;
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
//                        pixels[y * QR_WIDTH + x] = 0xff37a991;
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
//                        pixels[y * QR_WIDTH + x] = 0x00000000;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            qrBitmap = bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return qrBitmap;
    }
}
