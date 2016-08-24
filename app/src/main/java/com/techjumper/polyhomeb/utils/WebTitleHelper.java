package com.techjumper.polyhomeb.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.techjumper.corelib.utils.UI;
import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.polyhomeb.R;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/17
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class WebTitleHelper {

    public static final String NATIVE_METHOD_RETURN = "NativeReturn";
    public static final String NATIVE_METHOD_MENU = "NativeMenu";
    public static final String NATIVE_METHOD_NEW_ARTICLE = "NativeNewArticle";

    public static Builder create(View view) {
        return new Builder(view);
    }

    public static class Builder {
        private Object obj;
        private View.OnClickListener leftFirstClickListener;
        private View.OnClickListener leftSecondClickListener;
        private View.OnClickListener rightFirstClickListener;
        private View.OnClickListener rightSecondClickListener;
        private String titleText;
        private boolean showLeftFirst;
        private boolean showLeftSecond;
        private boolean showRightFirst;
        private boolean showRightSecond;
        private boolean leftFirstIsTextView;
        private boolean leftSecondIsTextView;
        private boolean rightFirstIsTextView;
        private boolean rightSecondIsTextView;
        private String leftFirstPicUrl;
        private String leftSecondPicUrl;
        private String rightFirstPicUrl;
        private String rightSecondPicUrl;
        private String leftFirstText;
        private String leftSecondText;
        private String rightFirstText;
        private String rightSecondText;
        private String leftFirstMethod;
        private String leftSecondMethod;
        private String rightFirstMethod;
        private String rightSecondMethod;

        private Builder(Object obj) {
            this.obj = obj;
        }

        public Builder title(String text) {
            titleText = text;
            return this;
        }

        public Builder leftFirstClick(View.OnClickListener listener) {
            leftFirstClickListener = listener;
            return this;
        }

        public Builder leftSecondClick(View.OnClickListener listener) {
            leftSecondClickListener = listener;
            return this;
        }

        public Builder rightFirstClick(View.OnClickListener listener) {
            rightFirstClickListener = listener;
            return this;
        }

        public Builder rightSecondClick(View.OnClickListener listener) {
            rightSecondClickListener = listener;
            return this;
        }

        public Builder showRightFirst(boolean right) {
            showRightFirst = right;
            return this;
        }

        public Builder showRightSecond(boolean right) {
            showRightSecond = right;
            return this;
        }

        public Builder showLeftFirst(boolean right) {
            showLeftFirst = right;
            return this;
        }

        public Builder showLeftSecond(boolean right) {
            showLeftSecond = right;
            return this;
        }

        public Builder leftFirstIsTextView(boolean leftFirstIsTextView) {
            this.leftFirstIsTextView = leftFirstIsTextView;
            return this;
        }

        public Builder leftSecondIsTextView(boolean leftSecondIsTextView) {
            this.leftSecondIsTextView = leftSecondIsTextView;
            return this;
        }

        public Builder rightFirstIsTextView(boolean rightFirstIsTextView) {
            this.rightFirstIsTextView = rightFirstIsTextView;
            return this;
        }

        public Builder rightSecondIsTextView(boolean rightSecondIsTextView) {
            this.rightSecondIsTextView = rightSecondIsTextView;
            return this;
        }

        public Builder setLeftFirstPicUrl(String url) {
            this.leftFirstPicUrl = url;
            return this;
        }

        public Builder setLeftSecondPicUrl(String url) {
            this.leftSecondPicUrl = url;
            return this;
        }

        public Builder setRightFirstPicUrl(String url) {
            this.rightFirstPicUrl = url;
            return this;
        }

        public Builder setRightSecondPicUrl(String url) {
            this.rightSecondPicUrl = url;
            return this;
        }

        public Builder setLeftFirstText(String text) {
            this.leftFirstText = text;
            return this;
        }

        public Builder setLeftSecondText(String text) {
            this.leftSecondText = text;
            return this;
        }

        public Builder setRightFirstText(String text) {
            this.rightFirstText = text;
            return this;
        }

        public Builder setRightSecondText(String text) {
            this.rightSecondText = text;
            return this;
        }

        public Builder setLeftFirstIconType(String type) {
            this.leftFirstMethod = type;
            return this;
        }

        public Builder setLeftSecondIconType(String type) {
            this.leftSecondMethod = type;
            return this;
        }

        public Builder setRightFirstIconType(String type) {
            this.rightFirstMethod = type;
            return this;
        }

        public Builder setRightSecondIconType(String type) {
            this.rightSecondMethod = type;
            return this;
        }

        public void process() {
            UI ui;
            if (obj instanceof Activity) {
                ui = UI.create((Activity) obj);
            } else {
                ui = UI.create((View) obj);
            }
            TextView tv = ui.findById(R.id.tv_title);
            if (tv != null) {
                tv.setText(titleText);
            }
            View left_first = ui.findById(R.id.left_group_first);
            View left_second = ui.findById(R.id.left_group_second);
            View right_first = ui.findById(R.id.right_group_first);
            View right_second = ui.findById(R.id.right_group_second);
            TextView leftFirstTv = ui.findById(R.id.left_first_tv);
            ImageView leftFirstIv = ui.findById(R.id.left_first_iv);
            TextView leftSecondTv = ui.findById(R.id.left_second_tv);
            ImageView leftSecondIv = ui.findById(R.id.left_second_iv);
            TextView rightFirstTv = ui.findById(R.id.right_first_tv);
            ImageView rightFirstIv = ui.findById(R.id.right_first_iv);
            TextView rightSecondTv = ui.findById(R.id.right_second_tv);
            ImageView rightSecondIv = ui.findById(R.id.right_second_iv);
            if (leftFirstTv != null) {
                leftFirstTv.setVisibility(leftFirstIsTextView ? View.VISIBLE : View.GONE);
                if (leftFirstIsTextView)
                    leftFirstTv.setText(leftFirstText);
            }
            if (leftFirstIv != null) {
                leftFirstIv.setVisibility(!leftFirstIsTextView ? View.VISIBLE : View.GONE);
                if (!leftFirstIsTextView) {
                    if (!TextUtils.isEmpty(leftFirstPicUrl)) {
                        PicassoHelper.getDefault().load(leftFirstPicUrl).into(leftFirstIv);
                    } else {
                        processNativeIcon(leftFirstIv, leftFirstMethod);
                    }
                }
            }
            if (leftSecondTv != null) {
                leftSecondTv.setVisibility(leftSecondIsTextView ? View.VISIBLE : View.GONE);
                if (leftSecondIsTextView)
                    leftSecondTv.setText(leftSecondText);
            }
            if (leftSecondIv != null) {
                leftSecondIv.setVisibility(!leftSecondIsTextView ? View.VISIBLE : View.GONE);
                if (!leftSecondIsTextView) {
                    if (!TextUtils.isEmpty(leftSecondPicUrl)) {
                        PicassoHelper.getDefault().load(leftSecondPicUrl).into(leftSecondIv);
                    } else {
                        processNativeIcon(leftSecondIv, leftSecondMethod);
                    }
                }
            }
            if (rightFirstTv != null) {
                rightFirstTv.setVisibility(rightFirstIsTextView ? View.VISIBLE : View.GONE);
                if (rightFirstIsTextView)
                    rightFirstTv.setText(rightFirstText);
            }
            if (rightFirstIv != null) {
                rightFirstIv.setVisibility(!rightFirstIsTextView ? View.VISIBLE : View.GONE);
                if (!rightFirstIsTextView) {
                    if (!TextUtils.isEmpty(rightFirstPicUrl)) {
                        PicassoHelper.getDefault().load(rightFirstPicUrl).into(rightFirstIv);
                    } else {
                        processNativeIcon(rightFirstIv, rightFirstMethod);
                    }
                }
            }
            if (rightSecondTv != null) {
                rightSecondTv.setVisibility(rightSecondIsTextView ? View.VISIBLE : View.GONE);
                if (rightSecondIsTextView)
                    rightSecondTv.setText(rightSecondText);
            }
            if (rightSecondIv != null) {
                rightSecondIv.setVisibility(!rightSecondIsTextView ? View.VISIBLE : View.GONE);
                if (!rightSecondIsTextView) {
                    if (!TextUtils.isEmpty(rightSecondPicUrl)) {
                        PicassoHelper.getDefault().load(rightSecondPicUrl).into(rightSecondIv);
                    } else {
                        processNativeIcon(rightSecondIv, rightSecondMethod);
                    }
                }
            }
            if (left_first != null) {
                left_first.setVisibility(showLeftFirst ? View.VISIBLE : View.GONE);
                left_first.setOnClickListener(leftFirstClickListener);
            }
            if (left_second != null) {
                left_second.setVisibility(showLeftSecond ? View.VISIBLE : View.GONE);
                left_second.setOnClickListener(leftSecondClickListener);
            }
            if (right_first != null) {
                right_first.setVisibility(showRightFirst ? View.VISIBLE : View.GONE);
                right_first.setOnClickListener(rightFirstClickListener);
            }
            if (right_second != null) {
                right_second.setVisibility(showRightSecond ? View.VISIBLE : View.GONE);
                right_second.setOnClickListener(rightSecondClickListener);
            }
        }

        private void processNativeIcon(ImageView imageView, String method) {
            switch (method) {
                case NATIVE_METHOD_RETURN:   //return
                    PicassoHelper.getDefault().load(R.mipmap.icon_back).into(imageView);
                    break;
                case NATIVE_METHOD_MENU:   //homeMenu
                    PicassoHelper.getDefault().load(R.mipmap.icon_title_menu).into(imageView);
                    break;
                case NATIVE_METHOD_NEW_ARTICLE:   //add
                    PicassoHelper.getDefault().load(R.mipmap.icon_add).into(imageView);
                    break;
                case "person":
                    PicassoHelper.getDefault().load(R.mipmap.icon_person).into(imageView);
                    break;
                default:  //onlineMethod,所以这里就不管了,前面处理过了已经
                    break;
            }
        }
    }
}
