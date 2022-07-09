package com.viper.app.ui.page.helper;

import android.view.View;
import android.widget.SeekBar;




public class DefaultInterface {

    public interface OnSeekBarChangeListener extends SeekBar.OnSeekBarChangeListener {
        @Override
        default void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }

        @Override
        default void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        default void onStopTrackingTouch(SeekBar seekBar) {
        }
    }

}
