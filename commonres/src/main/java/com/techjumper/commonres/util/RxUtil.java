package com.techjumper.commonres.util;

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.techjumper.commonres.R;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.window.ToastUtils;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/7/26.
 */

public class RxUtil {

    public static final int TIME = 5000;
    public static final int MAXSIZE = 1000;

    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable.throttleFirst(TIME, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //    public static Subscription limitLength(EditText editText) {
//        return RxTextView.textChanges(editText)
//                .map(charSequence -> {
//                    if (!TextUtils.isEmpty(charSequence)) {
//                        if (charSequence.length() < tempString.length()) {
//                            String s = tempString.substring(tempString.length() - 1, tempString.length());
//                            if (isChinese(s)) {
//                                size -= 2;
//                            } else {
//                                size--;
//                            }
//                            tempString = charSequence.toString();
//                        } else if (charSequence.length() > tempString.length() && size <= MAXSIZE) {
//                            String s = charSequence.toString().substring(charSequence.length() - 1, charSequence.length());
//                            if (isChinese(s)) {
//                                size += 2;
//                            } else {
//                                size++;
//                            }
//                            tempString = charSequence.toString();
//                        }
//                    }
//                    Log.d("String", "size: " + size);
//                    Log.d("String", "tempString: " + tempString);
//                    if (!TextUtils.isEmpty(tempString) && size - MAXSIZE > 0) {
//                        charSequence = charSequence.toString().substring(0, tempString.length() - 1);
//                        editText.setText(charSequence);
//                        editText.setSelection(charSequence.length());
//                    }
//                    return charSequence;
//                })
//                .debounce(1000, TimeUnit.MILLISECONDS)
//                .map(charSequence1 -> {
//                    if (size > MAXSIZE) {
//                        return Utils.appContext.getString(R.string.limit_max);
//                    } else {
//                        return null;
//                    }
//                })
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(s -> {
//                    editText.setError(s);
//                });
//    }
    public static Subscription limitLength(EditText editText) {
        return RxTextView.textChanges(editText)
                .map(charSequence -> {
                    if (!TextUtils.isEmpty(charSequence) && charSequence.length() > MAXSIZE) {
                        editText.setText(charSequence.toString().substring(0, MAXSIZE));
                        editText.setSelection(MAXSIZE);
                    }
                    return charSequence;
                })
                .debounce(1000, TimeUnit.MILLISECONDS)
                .map(charSequence1 -> {
                    if (charSequence1.length() > MAXSIZE) {
                        return Utils.appContext.getString(R.string.limit_max);
                    } else {
                        return null;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    editText.setError(s);
                });
    }

    private static boolean isChinese(String s) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(s);
        return m.matches();
    }

}
