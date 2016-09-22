package com.techjumper.polyhomeb.mvp.p.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.window.DialogUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.lib2.utils.PicassoHelper;
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
import com.techjumper.polyhomeb.utils.PicUtils;
import com.techjumper.polyhomeb.utils.UploadPicUtil;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;

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

    private String mLocalAvatarPath = "";

    /**
     * 选择和裁剪图片时候的各种code
     */
    private static final int CHOOSE_PICTURE = 0;
    private static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    private static Uri tempUri;

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
                                getView().getTvSex().setText(sex);
                                //收到的sex,是字符串的男和女,不是1和2;SP和服务器返回的是1和2
                                if (getView().getString(R.string.male).equals(sex)) {
                                    mSex = "1";
                                } else if (getView().getString(R.string.female).equals(sex)) {
                                    mSex = "2";
                                } else {
                                    mSex = "";
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
        bundle.putString(KEY_SEX, getView().getTvSex().getText().toString());
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
                Intent openCameraIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                tempUri = Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory().getAbsolutePath() + File.separator + Config.sParentDirName + File.separator + Config.sAvatarsDirName, "image.jpg"));
                // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                getView().startActivityForResult(openCameraIntent, TAKE_PICTURE);
                break;
            case 1:
                Intent openAlbumIntent = new Intent(
                        Intent.ACTION_GET_CONTENT);
                openAlbumIntent.setType("image/*");
                getView().startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                break;
        }
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getView().RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     */
    protected void startPhotoZoom(Uri uri) {
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        getView().startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            String imagePath = PicUtils.savePhoto(
                    photo
                    , Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Config.sParentDirName + File.separator + Config.sAvatarsDirName
                    , String.valueOf(System.currentTimeMillis()));
            mLocalAvatarPath = imagePath;
            uploadPicToServer(imagePath);
        }
    }

    /**
     * 上传图片一些列操作
     */
    private void uploadPicToServer(String imagePath) {
        //首先将图片设置给iv
        getView().showLoading();
        Glide.with(getView()).load(imagePath).transform(new GlideBitmapTransformation(getView())).into(getView().getIvAvatar());
        Glide.with(getView()).load(R.mipmap.icon_avatar_bg).transform(new GlideBitmapTransformation(getView())).into(getView().getIvBg());
        new Thread(() -> {
            String base64 = transformCode(imagePath);
            getView().runOnUiThread(() -> uploadPic(base64));
        }).start();
    }

    /**
     * Base64转码
     */
    private String transformCode(String imagePath) {
        return UploadPicUtil.bitmap2Base64(imagePath);
    }

    /**
     * 上传图片至服务器
     */
    private void uploadPic(String base64) {
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = mModel.uploadPic(base64)
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
                                String cover = entity.getData().getCover();
                                UserManager.INSTANCE.saveUserInfo(UserManager.KEY_AVATAR, Config.sHost + cover);
                                UserManager.INSTANCE.saveUserInfo(UserManager.KEY_LOCAL_AVATAR, mLocalAvatarPath);
                                ToastUtils.show(getView().getString(R.string.change_avatar_success));
                                RxBus.INSTANCE.send(new AvatarEvent(entity.getData().getCover()));

                                saveAvatarToDisk();
                            }
                        }));
    }

    /**
     * 缓存头像到本地
     */
    private void saveAvatarToDisk() {
        String avatarUrl = UserManager.INSTANCE.getUserInfo(UserManager.KEY_AVATAR);
        if (!TextUtils.isEmpty(avatarUrl)) {
            new Thread(() -> {
                try {
                    Bitmap bitmap = PicassoHelper.load(avatarUrl).get();
                    String path = PicUtils.savePhoto(
                            bitmap
                            , Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Config.sParentDirName + File.separator + Config.sAvatarsDirName
                            , String.valueOf(System.currentTimeMillis()) + "avatar");
                    if (!TextUtils.isEmpty(path)) {
                        UserManager.INSTANCE.saveUserInfo(UserManager.KEY_LOCAL_AVATAR, path);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        }
    }

}
