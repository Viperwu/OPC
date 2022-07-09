package com.viper.app.ui.page.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import com.viper.app.R;
import com.viper.app.data.bean.PageSetting;
import com.viper.app.databinding.DrawerPageItemBinding;
import com.viper.app.ui.state.SettingViewModel;
import com.viper.base.databinding.recyclerview.adapter.SimpleDataBindingAdapter;
/**
 * 左侧抽屉page item适配器
 */
public class DrawPageItemAdapter extends SimpleDataBindingAdapter<PageSetting, DrawerPageItemBinding> {



    public DrawPageItemAdapter(Context context) {
        super(context, R.layout.drawer_page_item, DiffUtils.getInstance().getPageSettingItemCallbackItemCallback());

    }

    @Override
    protected void onBindItem(DrawerPageItemBinding binding, PageSetting item, RecyclerView.ViewHolder holder) {
      binding.setData(item);


    }


}
