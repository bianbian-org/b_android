package com.techjumper.polyhome.b.home.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.techjumper.polyhome.b.home.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kevin on 16/4/19.
 */
public class SquareView extends RelativeLayout {

    @Bind(R.id.content_text)
    TextView contentTv;
    @Bind(R.id.title_text)
    TextView titleTv;
    @Bind(R.id.quarter_text)
    TextView quarterTv;
    @Bind(R.id.quarter_img)
    ImageView quarterImv;
    @Bind(R.id.bg_main)
    RelativeLayout bgMain;
    @Bind(R.id.quarter_layout)
    FrameLayout quarterLayout;

    private int bgColor; //背景颜色
    private int contentImgId; //内容icon的resId（中间大的那块区域）
    private int quarterImgId; //右上角的img的resId
    private String contentText; //内容的文字内容（中间大的那块区域）
    private String titleText; //标题的文字内容（下面那块小的）
    private String quarterText; //右上角的文字内容
    private boolean quarterLayoutVisible; //右上角背景

    public SquareView(Context context) {
        super(context);
    }

    public SquareView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_square, this, true);
        ButterKnife.bind(view, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SquareView);
        bgColor = typedArray.getColor(R.styleable.SquareView_bg_color, getResources().getColor(R.color.color_20c3f3));
        contentImgId = typedArray.getResourceId(R.styleable.SquareView_content_img, 0);
        quarterImgId = typedArray.getResourceId(R.styleable.SquareView_quarter_img, 0);
        contentText = typedArray.getString(R.styleable.SquareView_content_text);
        titleText = typedArray.getString(R.styleable.SquareView_title_text);
        quarterText = typedArray.getString(R.styleable.SquareView_quarter_text);
        quarterLayoutVisible = typedArray.getBoolean(R.styleable.SquareView_quarter_layout_visible, false);

        bgMain.setBackgroundColor(bgColor);

        showContentText(contentText);
        showContentIcon(contentImgId);

        showQuarterText(quarterText);
        showQuarterIcon(quarterImgId);

        titleTv.setText(titleText);
        quarterLayout.setVisibility(quarterLayoutVisible ? VISIBLE : GONE);

        typedArray.recycle();
    }

    //设置中间数据为文本
    public void showContentText(String text) {
        if (!TextUtils.isEmpty(text)) {
            contentTv.setCompoundDrawables(null, null, null, null);
            contentTv.setText(text + "°");
            contentImgId = 0;
        }
    }

    //设置中间数据为文本大小
    public void showContentTextSize(float textSize) {
        contentTv.setTextSize(textSize);
    }

    //设置中间数据为img
    public void showContentIcon(int resId) {
        if (resId != 0) {
            Drawable drawable = getResources().getDrawable(contentImgId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
            contentTv.setCompoundDrawables(drawable, null, null, null);
            contentTv.setText("");
        }
    }

    //设置右上角数据为文本
    public void showQuarterText(String text) {
        if (!TextUtils.isEmpty(text)) {
            quarterLayout.setVisibility(VISIBLE);
            quarterTv.setVisibility(VISIBLE);
            quarterImv.setVisibility(GONE);
            quarterTv.setText(text);
            quarterImgId = 0;
        }
    }

    //设置右上角数据为icon
    public void showQuarterIcon(int resId) {
        if (resId != 0) {
            quarterLayout.setVisibility(VISIBLE);
            quarterTv.setVisibility(GONE);
            quarterImv.setVisibility(VISIBLE);
            quarterImv.setBackgroundResource(resId);
        }
    }

    public void showTitleText(String text){
        if (!TextUtils.isEmpty(text)) {
            titleTv.setText("pm2.5 " + text);
        }
    }
}
