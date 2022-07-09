package com.viper.app.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.viper.app.R;


/**
 * 跑马灯view根据文字长度开启跑马灯效果
 */
@SuppressLint("AppCompactCustomView")
public class MarqueeView extends androidx.appcompat.widget.AppCompatTextView {

    private boolean isFocused;


    public MarqueeView(@NonNull Context context) {
        super(context);


    }

    public MarqueeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public MarqueeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        if (isOverFlowed()){
            setMarqueeRepeatLimit(-1);
            setFocusable(true);
            setGravity(Gravity.FILL_VERTICAL);
            setEllipsize(TextUtils.TruncateAt.MARQUEE);
            setSingleLine();
            setFocusableInTouchMode(true);
            setHorizontallyScrolling(true);
            isFocused = true;
        }else {
            if (getId() == R.id.value){
                setGravity(Gravity.END|Gravity.CENTER);
            }
            isFocused = false;
        }
    }


    @Override
    public boolean isFocused() {
        if (this.isFocused){
            return true;
        }else {
           return super.isFocused();
        }
    }

    public boolean isOverFlowed(){
        if (getId() == R.id.type){
            return getText().toString().length() > 10;
        }else {
            return getText().toString().length() > 15;
        }


    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
