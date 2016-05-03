package com.techjumper.polyhome.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.techjumper.polyhome.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kevin on 16/4/21.
 */
public class ArcDataView extends FrameLayout {

    @Bind(R.id.arcview)
    ArcView arcview;
    @Bind(R.id.content_text)
    TextView contentText;
    @Bind(R.id.subtitle_text)
    TextView subtitleText;
    @Bind(R.id.title_text)
    TextView titleText;

    private int mRadius;
    private int mWidth;
    private int mHeight;
    private int mBgColor;
    private String mTitle;
    private String mSubTitle;
    private String mContentTitle;

    public ArcDataView(Context context) {
        super(context);
    }

    public ArcDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_arc, this, true);
        ButterKnife.bind(view, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcDataView);
        mRadius = (int) typedArray.getDimension(R.styleable.ArcDataView_data_arc_radius, 0f);
        mWidth = (int) typedArray.getDimension(R.styleable.ArcDataView_data_rect_width, 0f);
        mHeight = (int) typedArray.getDimension(R.styleable.ArcDataView_data_rect_height, 0f);
        mBgColor = typedArray.getColor(R.styleable.ArcDataView_data_av_bg_color, getResources().getColor(R.color.color_20c3f3));

        mTitle = typedArray.getString(R.styleable.ArcDataView_data_title_text);
        mSubTitle = typedArray.getString(R.styleable.ArcDataView_data_subtitle_text);
        mContentTitle = typedArray.getString(R.styleable.ArcDataView_data_content_text);

        arcview.setParameter(mRadius, mWidth, mHeight, mBgColor);

        titleText.setText(mTitle);
        subtitleText.setText(mSubTitle);
        setContentText(mContentTitle);

        typedArray.recycle();
    }

    /**
     * 数据的变化
     *
     * @param text
     */
    public void setContentText(String text) {
        contentText.setText(text);
    }
}
