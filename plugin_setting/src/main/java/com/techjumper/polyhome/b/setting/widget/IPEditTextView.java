package com.techjumper.polyhome.b.setting.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhome.b.setting.R;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by kevin on 16/6/1.
 */
public class IPEditTextView extends FrameLayout {

    @Bind(R.id.first_et)
    EditText firstEt;
    @Bind(R.id.second_et)
    EditText secondEt;
    @Bind(R.id.third_et)
    EditText thirdEt;
    @Bind(R.id.fourth_et)
    EditText fourthEt;

    public IPEditTextView(Context context) {
        super(context);
        initView(context);
    }

    public IPEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public IPEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_ipedittext, this, true);
        ButterKnife.bind(view, this);

        initEditText(firstEt, secondEt);
        initEditText(secondEt, thirdEt);
        initEditText(thirdEt, fourthEt);
    }

    private void initEditText(EditText firstEt, EditText secondEt) {
        RxTextView.textChanges(firstEt)
                .map(charSequence -> {
                    if (charSequence.length() == 3) {
                        secondEt.requestFocus();
                    }
                    return charSequence;
                })
                .debounce(150, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence1 -> {

                });
    }
}
