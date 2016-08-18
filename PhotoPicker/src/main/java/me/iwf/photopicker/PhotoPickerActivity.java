package me.iwf.photopicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.techjumper.corelib.utils.window.StatusbarHelper;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.event.OnItemCheckListener;
import me.iwf.photopicker.fragment.ImagePagerFragment;
import me.iwf.photopicker.fragment.PhotoPickerFragment;

import static android.widget.Toast.LENGTH_LONG;
import static me.iwf.photopicker.PhotoPicker.DEFAULT_COLUMN_NUMBER;
import static me.iwf.photopicker.PhotoPicker.DEFAULT_MAX_COUNT;
import static me.iwf.photopicker.PhotoPicker.EXTRA_GRID_COLUMN;
import static me.iwf.photopicker.PhotoPicker.EXTRA_MAX_COUNT;
import static me.iwf.photopicker.PhotoPicker.EXTRA_ORIGINAL_PHOTOS;
import static me.iwf.photopicker.PhotoPicker.EXTRA_PREVIEW_ENABLED;
import static me.iwf.photopicker.PhotoPicker.EXTRA_SHOW_CAMERA;
import static me.iwf.photopicker.PhotoPicker.EXTRA_SHOW_GIF;
import static me.iwf.photopicker.PhotoPicker.KEY_SELECTED_PHOTOS;

public class PhotoPickerActivity extends AppCompatActivity implements View.OnClickListener {

    private PhotoPickerFragment pickerFragment;
    private ImagePagerFragment imagePagerFragment;

    private int maxCount = DEFAULT_MAX_COUNT;

    /**
     * to prevent multiple calls to inflate menu
     */
//    private boolean menuIsInflated = false;

    private boolean showGif = false;
    private int columnNumber = DEFAULT_COLUMN_NUMBER;
    private ArrayList<String> originalPhotos = null;


    //    private MenuItem menuDoneItem;
    private TextView mRight;   //tv_right
    private FrameLayout mRightLayout;  //right_group

    private TextView mTitle; //tv_title

    private FrameLayout mBack;  //left_group


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean showCamera = getIntent().getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        boolean showGif = getIntent().getBooleanExtra(EXTRA_SHOW_GIF, false);
        boolean previewEnabled = getIntent().getBooleanExtra(EXTRA_PREVIEW_ENABLED, true);

        setShowGif(showGif);

        setContentView(R.layout.__picker_activity_photo_picker);


        mRight = (TextView) findViewById(R.id.tv_right);
        mRightLayout = (FrameLayout) findViewById(R.id.right_group);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mBack = (FrameLayout) findViewById(R.id.left_group);
        mTitle.setText("选择图片");

        mBack.setOnClickListener(this);
        mRightLayout.setOnClickListener(this);


//        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        setTitle(R.string.__picker_title);
//
//        ActionBar actionBar = getSupportActionBar();
//
//        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            actionBar.setElevation(25);
//        }

        maxCount = getIntent().getIntExtra(EXTRA_MAX_COUNT, DEFAULT_MAX_COUNT);
        columnNumber = getIntent().getIntExtra(EXTRA_GRID_COLUMN, DEFAULT_COLUMN_NUMBER);
        originalPhotos = getIntent().getStringArrayListExtra(EXTRA_ORIGINAL_PHOTOS);

        pickerFragment = (PhotoPickerFragment) getSupportFragmentManager().findFragmentByTag("tag");
        if (pickerFragment == null) {
            pickerFragment = PhotoPickerFragment
                    .newInstance(showCamera, showGif, previewEnabled, columnNumber, maxCount, originalPhotos);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, pickerFragment, "tag")
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
        }

        pickerFragment.getPhotoGridAdapter().setOnItemCheckListener(new OnItemCheckListener() {
            @Override
            public boolean OnItemCheck(int position, Photo photo, final boolean isCheck, int selectedItemCount) {

                int total = selectedItemCount + (isCheck ? -1 : 1);

//                menuDoneItem.setEnabled(total > 0);
                mRight.setEnabled(total > 0);

                if (total > maxCount) {
                    Toast.makeText(getActivity(), getString(R.string.__picker_over_max_count_tips, maxCount),
                            LENGTH_LONG).show();
                    return false;
                }

                mRight.setText(getString(R.string.__picker_done_with_count, total, maxCount));

                if (maxCount <= 1) {
                    List<Photo> photos = pickerFragment.getPhotoGridAdapter().getSelectedPhotos();
                    if (!photos.contains(photo)) {
                        photos.clear();
                        pickerFragment.getPhotoGridAdapter().notifyDataSetChanged();
                    }
                    return true;
                }

//                if (total > maxCount) {  8/16将这里注释,然后移动到int total下面,因为当总数量只能选择一张的时候,无法弹出toast,并且return false
//                    Toast.makeText(getActivity(), getString(R.string.__picker_over_max_count_tips, maxCount),
//                            LENGTH_LONG).show();
//                    return false;
//                }
//                menuDoneItem.setTitle(getString(R.string.__picker_done_with_count, total, maxCount));

//                mRight.setText(getString(R.string.__picker_done_with_count, total, maxCount));  8/16将这里注释,然后移动到int total下面,因为当总数量只能选择一张的时候,title会不显示已选择数量
                return true;
            }
        });


        if (originalPhotos != null && originalPhotos.size() > 0) {
//            menuDoneItem.setEnabled(true);
//            menuDoneItem.setTitle(
//                    getString(R.string.__picker_done_with_count, originalPhotos.size(), maxCount));
            mRight.setEnabled(true);
            mRight.setText(getString(R.string.__picker_done_with_count, originalPhotos.size(), maxCount));
        } else {
//            menuDoneItem.setEnabled(false);
            mRight.setEnabled(false);
            mRight.setText(getString(R.string.__picker_done_with_count, originalPhotos.size(), maxCount));
        }

        StatusbarHelper.Builder statusbarBuidler = StatusbarHelper.from(this)
                .noActionBar(true)
                .setLightStatusBar(false)
                .setTransparentStatusbar(true);

        statusbarBuidler.setActionbarView(findViewById(R.id.title_group));

        onStatusbarTransform(statusbarBuidler).process();

    }

    protected StatusbarHelper.Builder onStatusbarTransform(StatusbarHelper.Builder builder) {
        return builder;
    }

    /**
     * Overriding this method allows us to run our exit animation first, then exiting
     * the activity when it complete.
     */
    @Override
    public void onBackPressed() {
        if (imagePagerFragment != null && imagePagerFragment.isVisible()) {
            imagePagerFragment.runExitAnimation(new Runnable() {
                public void run() {
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStack();
                    }
                }
            });
        } else {
            super.onBackPressed();
        }
    }


    public void addImagePagerFragment(ImagePagerFragment imagePagerFragment) {
        this.imagePagerFragment = imagePagerFragment;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, this.imagePagerFragment)
                .addToBackStack(null)
                .commit();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!menuIsInflated) {
//            getMenuInflater().inflate(R.menu.__picker_menu_picker, menu);
//            menuDoneItem = menu.findItem(R.id.done);
//            if (originalPhotos != null && originalPhotos.size() > 0) {
//                menuDoneItem.setEnabled(true);
//                menuDoneItem.setTitle(
//                        getString(R.string.__picker_done_with_count, originalPhotos.size(), maxCount));
//            } else {
//                menuDoneItem.setEnabled(false);
//            }
//            menuIsInflated = true;
//            return true;
//        }
//        return false;
//    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            super.onBackPressed();
//            return true;
//        }
//
//        if (item.getItemId() == R.id.done) {
//            Intent intent = new Intent();
//            ArrayList<String> selectedPhotos = pickerFragment.getPhotoGridAdapter().getSelectedPhotoPaths();
//            intent.putStringArrayListExtra(KEY_SELECTED_PHOTOS, selectedPhotos);
//            setResult(RESULT_OK, intent);
//            finish();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public PhotoPickerActivity getActivity() {
        return this;
    }

    public boolean isShowGif() {
        return showGif;
    }

    public void setShowGif(boolean showGif) {
        this.showGif = showGif;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.left_group) {

            onBackPressed();

        } else if (i == R.id.right_group) {

            Intent intent = new Intent();
            ArrayList<String> selectedPhotos = pickerFragment.getPhotoGridAdapter().getSelectedPhotoPaths();
            intent.putStringArrayListExtra(KEY_SELECTED_PHOTOS, selectedPhotos);
            setResult(RESULT_OK, intent);
            finish();


        }
    }
}
