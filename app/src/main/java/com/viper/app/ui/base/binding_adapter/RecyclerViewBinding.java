package com.viper.app.ui.base.binding_adapter;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.viper.app.ui.state.BaseSharedOPCDataModel;

public class RecyclerViewBinding {



    @BindingAdapter(value = {"initRecyclerView"}, requireAll = false)
    public void initRecyclerView(RecyclerView recyclerView, BaseSharedOPCDataModel mShared){


    }
}
