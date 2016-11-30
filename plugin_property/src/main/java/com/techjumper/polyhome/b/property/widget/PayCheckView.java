package com.techjumper.polyhome.b.property.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techjumper.polyhome.b.property.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kevin on 16/11/10.
 */

public class PayCheckView extends LinearLayout {
    @Bind(R.id.icon)
    ImageView icon;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.check)
    CheckBox check;
    private String textString;
    private boolean isCheck;
    private int ImgRes;

    public PayCheckView(Context context) {
        super(context);
    }

    public PayCheckView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_check_pay_style, this, true);
        ButterKnife.bind(view, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PayCheckView);
        ImgRes = typedArray.getResourceId(R.styleable.PayCheckView_pay_icon, R.mipmap.icon_alipay);
        textString = typedArray.getString(R.styleable.PayCheckView_pay_text);
        isCheck = typedArray.getBoolean(R.styleable.PayCheckView_isCheck, false);

        icon.setImageResource(ImgRes);
        text.setText(textString);
        check.setChecked(isCheck);

        typedArray.recycle();
    }

    public void setCheck(boolean isCheck) {
        check.setChecked(isCheck);
    }

    public boolean isCheck() {
        return check.isChecked();
    }
}
