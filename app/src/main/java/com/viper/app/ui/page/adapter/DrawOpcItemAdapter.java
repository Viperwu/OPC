package com.viper.app.ui.page.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.viper.app.R;
import com.viper.app.data.bean.OPCSetting;
import com.viper.app.databinding.DrawerOpcItemBinding;
import com.viper.base.databinding.recyclerview.adapter.SimpleDataBindingAdapter;

/**
 * 左侧抽屉opc item适配器
 */
public class DrawOpcItemAdapter extends SimpleDataBindingAdapter<OPCSetting, DrawerOpcItemBinding> {

    public DrawOpcItemAdapter(Context context) {
        super(context, R.layout.drawer_opc_item, DiffUtils.getInstance().getSettingItemCallback());
    }

    @Override
    protected void onBindItem(DrawerOpcItemBinding binding, OPCSetting item, RecyclerView.ViewHolder holder) {
      binding.setData(item);
    }


}
