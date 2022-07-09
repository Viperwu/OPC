package com.viper.app.ui.page.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.viper.app.data.bean.PageNode;
import com.viper.app.data.bean.OPCNode;
import com.viper.app.data.bean.OPCSetting;
import com.viper.app.data.bean.PageSetting;


public class DiffUtils {


    private DiffUtil.ItemCallback<OPCNode> opcNodeItemCallback;
    private DiffUtil.ItemCallback<OPCSetting> settingItemCallback;
    private DiffUtil.ItemCallback<PageSetting> pageSettingItemCallbackItemCallback;
    private DiffUtil.ItemCallback<PageNode> iItemViewTypeItemCallback;

    private DiffUtils() {
    }

    private static final DiffUtils S_DIFF_UTILS = new DiffUtils();

    public static DiffUtils getInstance() {
        return S_DIFF_UTILS;
    }


    @SuppressLint("DiffUtilEquals")
    public DiffUtil.ItemCallback<OPCNode> getOPCItemCallback() {
        if (opcNodeItemCallback == null) {
            opcNodeItemCallback = new DiffUtil.ItemCallback<OPCNode>() {
                @Override
                public boolean areItemsTheSame(@NonNull OPCNode oldItem, @NonNull OPCNode newItem) {
                    return oldItem.getNodeId().hashCode()==newItem.getNodeId().hashCode();
                }


                @Override
                public boolean areContentsTheSame(@NonNull OPCNode oldItem, @NonNull OPCNode newItem) {
                    if (oldItem.getValue()!=null && newItem.getValue()!=null){
                        return oldItem.getValue().equals(newItem.getValue());
                    }else {
                        return false;
                    }

                }
            };
        }
        return opcNodeItemCallback;
    }

    public DiffUtil.ItemCallback<PageSetting> getPageSettingItemCallbackItemCallback() {
        if (pageSettingItemCallbackItemCallback == null) {
            pageSettingItemCallbackItemCallback = new DiffUtil.ItemCallback<PageSetting>() {

                @Override
                public boolean areItemsTheSame(@NonNull PageSetting oldItem, @NonNull PageSetting newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(@NonNull PageSetting oldItem, @NonNull PageSetting newItem) {
                    return oldItem.getId()==newItem.getId();
                }
            };
        }
        return pageSettingItemCallbackItemCallback;
    }

    public DiffUtil.ItemCallback<OPCSetting> getSettingItemCallback() {
        if (settingItemCallback == null) {
            settingItemCallback = new DiffUtil.ItemCallback<OPCSetting>() {

                @Override
                public boolean areItemsTheSame(@NonNull OPCSetting oldItem, @NonNull OPCSetting newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(@NonNull OPCSetting oldItem, @NonNull OPCSetting newItem) {
                    return oldItem.getNodeId().hashCode()==newItem.getNodeId().hashCode();
                }
            };
        }
        return settingItemCallback;
    }

    @SuppressLint("DiffUtilEquals")
    public DiffUtil.ItemCallback<PageNode> getItemViewTypeItemCallback() {
        if (iItemViewTypeItemCallback == null) {
            iItemViewTypeItemCallback = new DiffUtil.ItemCallback<PageNode>() {

                @Override
                public boolean areItemsTheSame(@NonNull PageNode oldItem, @NonNull PageNode newItem) {
                    return oldItem.getName().equals(newItem.getName());
                }


                @Override
                public boolean areContentsTheSame(@NonNull PageNode oldItem, @NonNull PageNode newItem) {
                 //   return oldItem.equals(newItem);
                    if (oldItem.getValue()!=null && newItem.getValue()!=null){
                        return oldItem.getValue().equals(newItem.getValue());
                    }else {
                        return false;
                    }
                }
            };
        }
        return iItemViewTypeItemCallback;
    }
}
