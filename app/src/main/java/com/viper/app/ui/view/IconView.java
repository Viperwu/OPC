package com.viper.app.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.viper.app.util.U;

@SuppressLint("AppCompactCustomView")
public class IconView extends androidx.appcompat.widget.AppCompatTextView {


    public IconView(@NonNull Context context) {
        super(context);
        init();
    }

    public IconView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IconView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setTypeface(U.getTypeface());
    }

}
