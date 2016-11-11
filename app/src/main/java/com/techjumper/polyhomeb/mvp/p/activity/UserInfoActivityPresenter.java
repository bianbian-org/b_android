package com.techjumper.polyhomeb.mvp.p.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.file.FileUtils;
import com.techjumper.corelib.utils.window.DialogUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.AvatarEntity;
import com.techjumper.polyhomeb.entity.LoginEntity;
import com.techjumper.polyhomeb.entity.event.AvatarEvent;
import com.techjumper.polyhomeb.entity.event.ChangeEvent;
import com.techjumper.polyhomeb.entity.event.EmailEvent;
import com.techjumper.polyhomeb.entity.event.NicknameEvent;
import com.techjumper.polyhomeb.entity.event.SexEvent;
import com.techjumper.polyhomeb.mvp.m.UserInfoActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.ChangeEmailActivity;
import com.techjumper.polyhomeb.mvp.v.activity.ChangeNickNameActivity;
import com.techjumper.polyhomeb.mvp.v.activity.ChangeSexActivity;
import com.techjumper.polyhomeb.mvp.v.activity.UserInfoActivity;
import com.techjumper.polyhomeb.other.GlideBitmapTransformation;
import com.techjumper.polyhomeb.user.UserManager;
import com.techjumper.polyhomeb.utils.UploadPicUtil;

import java.io.File;
import java.util.Calendar;

import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;

import static android.os.Environment.getExternalStorageDirectory;
import static com.techjumper.corelib.utils.file.FileUtils.createDirsAndFile;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class UserInfoActivityPresenter extends AppBaseActivityPresenter<UserInfoActivity> {

    private UserInfoActivityModel mModel = new UserInfoActivityModel(this);
    private Subscription mSubs1, mSubs2, mSubs3, mSubs4;

    private String mBirthday = UserManager.INSTANCE.getUserInfo(UserManager.KEY_BIRTHDAY);
    private String mNickName = UserManager.INSTANCE.getUserInfo(UserManager.KEY_USER_NAME);
    private String mEmail = UserManager.INSTANCE.getUserInfo(UserManager.KEY_EMAIL);
    private String mSex = UserManager.INSTANCE.getUserInfo(UserManager.KEY_SEX);

    public static final String KEY_NICK_NAME = "key_nick_name";
    public static final String KEY_EMAIL = "key_email";
    public static final String KEY_SEX = "key_sex";

    private String mTempPicPath = getExternalStorageDirectory().getAbsolutePath() + File.separator
            + Config.sParentDirName + File.separator + Config.sAvatarsDirName;
    private static String sCameraPicName = "camera_image_no_crop.jpg";
    private static String sCropPicName = "crop.jpg";

    private Uri mCameraTempUri;

    /**
     * 选择和裁剪图片时候的各种code
     */
    private static final int GALLERY_PICTURE = 0;
    private static final int CAMERA_PICTURE = 1;
    private static final int CROP_PICTURE = 2;

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = RxBus.INSTANCE
                        .asObservable().subscribe(o -> {
                            if (o instanceof NicknameEvent) {
                                NicknameEvent event = (NicknameEvent) o;
                                String nickName = event.getNickName();
                                getView().getTvNickName().setText(nickName);
                                mNickName = nickName;
                                canRightClick();
                            } else if (o instanceof EmailEvent) {
                                EmailEvent event = (EmailEvent) o;
                                String email = event.getEmail();
                                getView().getTvEmail().setText(email);
                                mEmail = email;
                                canRightClick();
                            } else if (o instanceof SexEvent) {
                                SexEvent event = (SexEvent) o;
                                String sex = event.getSex();
                                mSex = sex;
                                if ("1".equals(sex)) {
                                    getView().getTvSex().setText(getView().getString(R.string.male));
                                } else if ("2".equals(sex)) {
                                    getView().getTvSex().setText(getView().getString(R.string.female));
                                } else {
                                    getView().getTvSex().setText("");
                                }
                                canRightClick();
                            }
                        }));
        accessPermissions();  //Rx需要订阅了才会收到事件,所以这里先要开始订阅.接下来的点击事件的第一次点击才会起作用,否则会导致第一次点击之后没有反应,需要点击第一次之后才能响应后续操作.
    }

    //这里来判断能否点击
    private void canRightClick() {
        boolean birthday = mBirthday.equals(UserManager.INSTANCE.getUserInfo(UserManager.KEY_BIRTHDAY));
        boolean nickname = mNickName.equals(UserManager.INSTANCE.getUserInfo(UserManager.KEY_USER_NAME));
        boolean email = mEmail.equals(UserManager.INSTANCE.getUserInfo(UserManager.KEY_EMAIL));
        boolean sex = mSex.equals(UserManager.INSTANCE.getUserInfo(UserManager.KEY_SEX));
        boolean canClick = !birthday || !nickname || !email || !sex;
        getView().canRightClick(canClick);
    }

    public void onTitleRightClick() {
        //提交成功后,修改SP中的数据,然后发送RxBus到侧边栏,改变头像,改变昵称
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs3);
        addSubscription(
                mSubs3 = mModel
                        .setUserInfo(mNickName
                                , mSex
                                , mBirthday
                                , mEmail)
                        .subscribe(new Observer<LoginEntity>() {
                            @Override
                            public void onCompleted() {
                                getView().dismissLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().showError(e);
                                getView().dismissLoading();
                            }

                            @Override
                            public void onNext(LoginEntity loginEntity) {
                                if (!processNetworkResult(loginEntity)) return;
                                //为什么不直接用UserManager.INSTANCE.saveUserInfo(loginEntity)
                                //因为这样做的话,我万一之前修改过家庭,现在家庭不是列表中的第一项,,就会出问题..
                                //所以这里只存入所需的数据即可
                                String birthday = loginEntity.getData().getBirthday();
                                String sex = loginEntity.getData().getSex();
                                String email = loginEntity.getData().getEmail();
                                String username = loginEntity.getData().getUsername();
                                String ticket = loginEntity.getData().getTicket();
                                UserManager.INSTANCE.saveUserInfo(UserManager.KEY_BIRTHDAY, birthday);
                                UserManager.INSTANCE.saveUserInfo(UserManager.KEY_SEX, sex);
                                UserManager.INSTANCE.saveUserInfo(UserManager.KEY_EMAIL, email);
                                UserManager.INSTANCE.saveUserInfo(UserManager.KEY_USER_NAME, username);
                                UserManager.INSTANCE.updateTicket(ticket);
                                ToastUtils.show(getView().getString(R.string.change_user_info_success));
                                RxBus.INSTANCE.send(new ChangeEvent(1)); //发送给侧边栏,更新昵称
                                getView().canRightClick(false);
                            }
                        }));
    }

    @OnClick({R.id.layout_nick_name, R.id.layout_email, R.id.layout_birthday, R.id.layout_sex, R.id.iv_avatar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_nick_name:
                changeNickName();
                break;
            case R.id.layout_email:
                changeEmail();
                break;
            case R.id.layout_birthday:
                showDatePicker();
                break;
            case R.id.layout_sex:
                changeSex();
                break;
            case R.id.iv_avatar:
                accessPermissions();
                break;
        }
    }

    private void changeNickName() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_NICK_NAME, getView().getTvNickName().getText().toString());
        new AcHelper.Builder(getView()).extra(bundle).target(ChangeNickNameActivity.class).start();
    }

    private void changeEmail() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_EMAIL, getView().getTvEmail().getText().toString());
        new AcHelper.Builder(getView()).extra(bundle).target(ChangeEmailActivity.class).start();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(getView(), (view, year, monthOfYear, dayOfMonth) -> {
            String month = monthOfYear + 1 >= 10 ? (monthOfYear + 1) + "" : "0" + (monthOfYear + 1);
            String day = dayOfMonth >= 10 ? day = dayOfMonth + "" : "0" + dayOfMonth;
            mBirthday = year + "-" + month + "-" + day;
            getView().getTvBirthday().setText(mBirthday);
            canRightClick();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void changeSex() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_SEX, mSex);
        new AcHelper.Builder(getView()).extra(bundle).target(ChangeSexActivity.class).start();
    }

    private void accessPermissions() {
        RxUtils.unsubscribeIfNotNull(mSubs4);
        addSubscription(
                mSubs4 = RxView.clicks(getView().getIvAvatar())
                        .compose(RxPermissions.getInstance(getView()).ensure(Manifest.permission.CAMERA))
                        .subscribe(granted -> {
                            if (granted) {
                                showDialog2ChooseAvatar();
                            } else {
                                ToastUtils.show(getView().getString(R.string.no_camera_permission));
                            }
                        }));
    }

    private void showDialog2ChooseAvatar() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getView(), android.R.layout.simple_list_item_1);
        arrayAdapter.add(getView().getString(R.string.camera));
        arrayAdapter.add(getView().getString(R.string.photo_library));
        DialogUtils.getBuilder(getView())
                .title(R.string.choose_avatar)
                .adapter(arrayAdapter, (dialog, itemView, which, text) -> {
                    jump2PicChooseActivity(dialog, which);
                })
                .show();
    }

    private void jump2PicChooseActivity(MaterialDialog dialog, int which) {
        switch (which) {
            case 0:
                choosePicFromCamera();
                break;
            case 1:  //选择相册图片进行裁剪
                choosePicFromGallery();
                break;
        }
        dialog.dismiss();
    }

    /**
     * 从相机选择图片
     */
    private void choosePicFromCamera() {
        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        mCameraTempUri = FileUtils.createDirsAndFile(mTempPicPath, sCameraPicName);
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraTempUri);
        getView().startActivityForResult(openCameraIntent, CAMERA_PICTURE);
    }

    /**
     * 从相册选择图片
     */
    private void choosePicFromGallery() {
        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setType("image/*");
        getView().startActivityForResult(openAlbumIntent, GALLERY_PICTURE);  //开启系统相册，选择图片的界面
    }

    /**
     * 裁剪图片
     */
    private void cropPicture(Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("crop", "true");//设置为裁切
        intent.putExtra("aspectX", 1);//裁切的宽比例
        intent.putExtra("aspectY", 1);//裁切的高比例
        intent.putExtra("outputX", 600);//裁切的宽度
        intent.putExtra("outputY", 600);//裁切的高度
        intent.putExtra("scale", true);//支持缩放
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, createDirsAndFile(mTempPicPath, sCropPicName));//将裁切的结果输出到指定的Uri
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//裁切成的图片的格式
        intent.putExtra("noFaceDetection", true);
        getView().startActivityForResult(intent, CROP_PICTURE);  //开启系统自带的裁剪界面
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getView().RESULT_OK) {
            switch (requestCode) {
                case CAMERA_PICTURE: //拍照之后走到这里
                    cropPicture(mCameraTempUri); // 开始对拍照得到的图片进行裁剪处理(mCameraTempUri图片被存在SD卡本地)
                    break;
                case GALLERY_PICTURE://从相册选择图片后走到这里
                    cropPicture(data.getData()); //开始对相册选择的图片进行剪裁处理
                    break;
                case CROP_PICTURE:
                    getCropedPic(data.getData());
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 得到裁剪后的图片
     */
    private void getCropedPic(Uri uri) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getView().getContentResolver().
                    openInputStream(uri));
            uploadPic(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从相册选择图片并且裁剪之后得到的bitmap
     * 1:设置给IV
     * 2:编码后上传至服务器
     */
    private Observable<String> getBitmap(Bitmap bitmap) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String base64 = UploadPicUtil.bitmap2Base64(bitmap);
                subscriber.onNext(base64);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 上传图片至服务器
     */
    private void uploadPic(Bitmap bitmap) {
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = getBitmap(bitmap)
                        .flatMap(s -> mModel.uploadPic(s))
                        .flatMap(uploadPicEntity -> mModel.updateAvatar(uploadPicEntity.getData().getUrl()))
                        .subscribe(new Observer<AvatarEntity>() {
                            @Override
                            public void onCompleted() {
                                getView().dismissLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().dismissLoading();
                                getView().showError(e);
                            }

                            @Override
                            public void onNext(AvatarEntity entity) {
                                getView().dismissLoading();
                                if (!processNetworkResult(entity)) return;
                                Glide.with(getView()).load(Config.sHost + entity.getData().getCover())
                                        .transform(new GlideBitmapTransformation(getView())).into(getView().getIvAvatar());
                                Glide.with(getView()).load(R.mipmap.icon_avatar_bg)
                                        .transform(new GlideBitmapTransformation(getView())).into(getView().getIvBg());
                                String cover = entity.getData().getCover();
                                UserManager.INSTANCE.saveUserInfo(UserManager.KEY_AVATAR, Config.sHost + cover);
                                ToastUtils.show(getView().getString(R.string.change_avatar_success));
                                RxBus.INSTANCE.send(new AvatarEvent(entity.getData().getCover()));
                            }
                        }));
    }
}
