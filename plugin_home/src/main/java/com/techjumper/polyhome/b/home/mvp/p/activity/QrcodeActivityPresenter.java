package com.techjumper.polyhome.b.home.mvp.p.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.techjumper.commonres.entity.event.TimeEvent;
import com.techjumper.commonres.util.CommonDateUtil;
import com.techjumper.commonres.util.StringUtil;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.UserInfoManager;
import com.techjumper.polyhome.b.home.mvp.v.activity.QrcodeActivity;

import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/11/8.
 */

public class QrcodeActivityPresenter extends AppBaseActivityPresenter<QrcodeActivity> {

    private long time;
    private Timer timer = new Timer();
    private ImageView qrcodeImg;

    @OnClick(R.id.bottom_home)
    void home() {
        getView().finish();
    }

    @OnClick(R.id.bottom_back)
    void back() {
        getView().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        time = getView().getTime();
        qrcodeImg = getView().getQrCodeImg();

        if (time == 0L) {
            time = System.currentTimeMillis() / 1000;
        }

        getView().getBottomDate().setText(CommonDateUtil.getTitleNewDate(time));

        addSubscription(
                Observable
                        .<Bitmap>create(subscriber -> {
                            subscriber.onNext(createQRImage(StringUtil.encrypt(UserInfoManager.getFamilyId())));
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
                                    qrcodeImg.setImageBitmap(bitmap);
                                }
                            }
                        })
        );

        addSubscription(RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof TimeEvent) {
                        Log.d("time", "更新时间");
                        TimeEvent event = (TimeEvent) o;
                        if (event.getType() == TimeEvent.QRCODE) {
                            Log.d("submitOnline", "二维码系统更新" + time);
                            if (getView().getBottomDate() != null) {
                                getView().getBottomDate().setText(CommonDateUtil.getTitleNewDate(time));
                            }
                        }
                    }
                }));

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getView().getBottomDate() != null) {
                    if (time != 0L) {
                        time = time + 1;
                        String second = CommonDateUtil.getSecond(time);
                        Log.d("submitOnline", "二维码second: " + second);
                        if (second.equals("00")) {
                            TimeEvent event = new TimeEvent();
                            event.setType(TimeEvent.QRCODE);
                            RxBus.INSTANCE.send(event);
                        }
                    }
                }
            }
        }, 0, 1000);
    }

    public Bitmap createQRImage(String url) {
        Bitmap qrBitmap = null;
        try {
            //判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1) {
                return null;
            }
            int QR_WIDTH = RuleUtils.dp2Px(300);
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
