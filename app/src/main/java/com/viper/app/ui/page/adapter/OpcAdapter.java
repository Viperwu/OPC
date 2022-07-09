package com.viper.app.ui.page.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.viper.app.data.bean.OPCNode;
import com.viper.app.databinding.OpcItemBinding;
import com.viper.base.databinding.recyclerview.adapter.SimpleDataBindingAdapter;
import com.viper.app.R;

/**
 * opc fragment 适配器
 */
public class OpcAdapter extends SimpleDataBindingAdapter<OPCNode, OpcItemBinding>{


    @SuppressLint("NotifyDataSetChanged")
    public OpcAdapter(Context context) {

        super(context, R.layout.opc_item, DiffUtils.getInstance().getOPCItemCallback());
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

    }
    @Override
    protected void onBindItem(OpcItemBinding binding, OPCNode node, RecyclerView.ViewHolder holder) {
        binding.setData(node);

    }



}
