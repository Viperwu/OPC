package com.viper.app.ui.page.adapter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.popup.QMUIQuickAction;
import com.viper.app.R;
import com.viper.app.data.bean.PageSetting;
import com.viper.app.databinding.SettingPageItemBinding;
import com.viper.app.ui.state.SettingViewModel;
import com.viper.app.ui.view.CallBackView;
import com.viper.app.util.U;
import com.viper.base.databinding.recyclerview.adapter.SimpleDataBindingAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * setting fragment page 适配器
 */
public class SettingPageItemAdapter extends SimpleDataBindingAdapter<PageSetting, SettingPageItemBinding> {

    private SettingViewModel settingViewModel;
    public SettingPageItemAdapter(Context context, SettingViewModel settingViewModel) {
        super(context, R.layout.setting_page_item, DiffUtils.getInstance().getPageSettingItemCallbackItemCallback());
        this.settingViewModel = settingViewModel;
    }

    @Override
    protected void onBindItem(SettingPageItemBinding binding, PageSetting pageSetting, RecyclerView.ViewHolder holder) {
      binding.setData(pageSetting);
        Context context = holder.itemView.getContext();
        holder.itemView.setOnLongClickListener(view -> {
            longClick(context,view,pageSetting);
            return false;
        });
        binding.tvDataSource.setOnLongClickListener(view -> {
            longClick(context,view,pageSetting);
           return false;
        });
        binding.tvPageName.setOnLongClickListener(view -> {
            longClick(context,view,pageSetting);
            return false;
        });
        binding.tvPageName.setOnClickListener(view -> {

            CallBackView.showEditTextDialog(view,"请输入新的名称",pageSetting.getPageName(), text -> {
                pageSetting.setPageName(text);
                settingViewModel.savePageSettingToLocal(pageSetting);
                notifyItemChanged(holder.getAbsoluteAdapterPosition());
            });

        });

        binding.tvDataSource.setOnClickListener(view -> {
            List<String> data = new ArrayList<>();
            data.add(U.getString(R.string.customer_data_source));

                settingViewModel.opcSettings.forEach(i->{
                    data.add(i.getTitle() + ":" + i.getAddress());
                });

            CallBackView.showPopup(view,data,i->{
                if (i==0){
                    CallBackView.showEditTextDialog(view,"请输入新的数据源地址",pageSetting.getDataSource(), text -> {
                        pageSetting.setDataSource(text.trim());
                        settingViewModel.savePageSettingToLocal(pageSetting);
                        notifyItemChanged(holder.getAbsoluteAdapterPosition());
                    });
                }else {
                    pageSetting.setDataSource(settingViewModel.opcSettings.get(i-1).getAddress());
                    settingViewModel.savePageSettingToLocal(pageSetting);
                    notifyItemChanged(holder.getAbsoluteAdapterPosition());
                }
            });
        });

        binding.tvReadMode.setOnClickListener(view -> {
            List<String> data = new ArrayList<>();
            data.add(U.getString(R.string.read_mode_sub));
            data.add(U.getString(R.string.read_mode_browse));
            CallBackView.showPopup(view,data,i->{
                pageSetting.setReadMode(i);
                settingViewModel.savePageSettingToLocal(pageSetting);
                notifyItemChanged(holder.getAbsoluteAdapterPosition());
            });
        });
    }

    private void longClick(Context context, View view, PageSetting pageSetting){
        QMUIPopups.quickAction(context,
            QMUIDisplayHelper.dp2px(context, 56),
            QMUIDisplayHelper.dp2px(context, 56))
            .shadow(true)
            .skinManager(QMUISkinManager.defaultInstance(context))
            .edgeProtection(QMUIDisplayHelper.dp2px(context, 20))
            .addAction(new QMUIQuickAction.Action().icon(R.drawable.ic_round_delete_24).text(U.getSting(R.string.delete_current)).onClick(
                (quickAction, action, position) -> {
                    quickAction.dismiss();
                    settingViewModel.deletePageSetting(pageSetting);
                }
            ))
            .show(view);
    }



}
