package com.techjumper.polyhome.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.techjumper.polyhome.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kevin on 16/4/21.
 */
public class AlmanacView extends RelativeLayout {

    private String title;
    private String text1;
    private String text2;
    private String text3;
    @Bind(R.id.almanac_title)
    TextView almanacTitle;
    @Bind(R.id.almanac_text_1)
    TextView almanacText1;
    @Bind(R.id.almanac_text_2)
    TextView almanacText2;
    @Bind(R.id.almanac_text_3)
    TextView almanacText3;

    public AlmanacView(Context context) {
        super(context);
    }

    public AlmanacView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_almanac, this, true);
        ButterKnife.bind(view, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AlmanacView);
        title = typedArray.getString(R.styleable.AlmanacView_almanac_title);
        text1 = typedArray.getString(R.styleable.AlmanacView_almanac_text1);
        text2 = typedArray.getString(R.styleable.AlmanacView_almanac_text2);
        text3 = typedArray.getString(R.styleable.AlmanacView_almanac_text3);

        almanacTitle.setText(title);
        setText1(text1);
        setText2(text2);
        setText3(text3);

        typedArray.recycle();
    }

    public void setText1(String text) {
        if (!TextUtils.isEmpty(text)) {
            almanacText1.setText(text);
        }
    }

    public void setText2(String text) {
        if (!TextUtils.isEmpty(text)) {
            almanacText2.setText(text);
        }
    }

    public void setText3(String text) {
        if (!TextUtils.isEmpty(text)) {
            almanacText3.setText(text);
        }
    }
}
