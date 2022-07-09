package com.viper.app.ui.page.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.popup.QMUIQuickAction;
import com.viper.app.R;
import com.viper.app.data.bean.OPCNode;
import com.viper.app.data.bean.OPCSetting;
import com.viper.app.databinding.OpcAppbarItemBinding;
import com.viper.app.ui.state.BaseOpcViewModel;
import com.viper.app.ui.state.SettingViewModel;
import com.viper.app.util.U;
import com.viper.base.databinding.recyclerview.adapter.SimpleDataBindingAdapter;

import java.lang.ref.WeakReference;
/**
 * opc fragment appbar适配器
 */
public class OpcAppBarItemAdapter extends SimpleDataBindingAdapter<OPCNode, OpcAppbarItemBinding> {

    private WeakReference<RecyclerView> recyclerView;


    public OpcAppBarItemAdapter(Context context) {
        super(context, R.layout.opc_appbar_item, DiffUtils.getInstance().getOPCItemCallback());

    }

    @Override
    protected void onBindItem(OpcAppbarItemBinding binding, OPCNode item, RecyclerView.ViewHolder holder) {
      binding.setData(item);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = new WeakReference<>(recyclerView);

    }

    public RecyclerView getRecyclerView() {
        return recyclerView.get();
    }
}
