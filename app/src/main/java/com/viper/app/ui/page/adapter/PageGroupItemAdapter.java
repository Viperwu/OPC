package com.viper.app.ui.page.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.viper.app.R;
import com.viper.app.data.bean.PageNode;
import com.viper.app.data.client.OPCUtil;
import com.viper.app.data.client.SiemensTypeId;
import com.viper.app.databinding.PageItemGroupSpinnerBinding;
import com.viper.app.databinding.PageItemGroupSwitchBinding;
import com.viper.app.databinding.PageItemSpinnerBinding;
import com.viper.app.databinding.PageItemSwitchBinding;
import com.viper.app.ui.state.BasePageViewModel;
import com.viper.app.util.U;
import com.viper.app.util.P;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * page group item适配器
 */
public class PageGroupItemAdapter extends BaseMultiAdapter<PageNode, ViewDataBinding> {
    private final BasePageViewModel mState;

    public PageGroupItemAdapter(Context context, BasePageViewModel mState) {

        super(context, DiffUtils.getInstance().getItemViewTypeItemCallback());
        this.mState = mState;


    }



    @Override
    protected void onBindItem(ViewDataBinding binding, PageNode item, RecyclerView.ViewHolder holder) {
        binding.setVariable(BR.data, item);
        holder.itemView.getLayoutParams().height = U.dip2px(item.getHeight());

        if (binding instanceof PageItemGroupSwitchBinding) {
            initEvent((PageItemGroupSwitchBinding) binding,item,holder);
        }

        if (binding instanceof PageItemGroupSpinnerBinding) {
            initEvent((PageItemGroupSpinnerBinding) binding,item,holder);
        }

    }

    private void initEvent(PageItemGroupSpinnerBinding binding,PageNode item, RecyclerView.ViewHolder holder){
        Spinner spinner = binding.itemValue;
        spinner.setOnLongClickListener(view -> {
            mOnItemLongClickListener.onItemLongClick(holder.itemView, item, holder.getAbsoluteAdapterPosition());
            //spinner.setSelection(5);
            return false;
        });
        LinkedHashMap<String, String> map = item.getMap();

        String str = item.getValue().get();
        ArrayList<String> list = new ArrayList<>();
        AtomicReference<String> s = new AtomicReference<>();
        if (map != null && !map.isEmpty()) {
            map.forEach((k, v) -> {
                list.add(k);
                if (v.equals(str)){
                    s.set(k);
                }
            });
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(holder.itemView.getContext(), R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        if (!U.isEmpty(s.get())) {
            spinner.setSelection(list.indexOf(s.get()));
        }

        item.setInit(false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //  Log.e("TAG", "onItemSelected: initttttttttttttttttt");

                if (item.isInit()){
                    if (map != null && !map.isEmpty()) {
                        String str = item.getValue().get();
                        item.setHoldValue(str);
                        if (!list.get(i).equals(str)) {
                            mState.reqWriteDataToOPC(item, map.get(list.get(i)), P::showWriteCallback);
                        }
                    }
                }else {
                    item.setInit(true);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        item.getValue().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (map != null && !map.isEmpty()) {
                    String str = item.getValue().get();

                    for (int i = 0; i < list.size(); i++) {
                        String s = map.get(list.get(i));
                        if (!U.isEmpty(s) && s.equals(str)) {
                            final int index = i;
                            new Handler(Looper.getMainLooper()).post(() -> spinner.setSelection(index));
                            break;
                        }
                    }
                }
            }
        });
    }


    private void initEvent(PageItemGroupSwitchBinding binding,PageNode item, RecyclerView.ViewHolder holder){
        binding.itemValue.setOnLongClickListener(view -> {
            mOnItemLongClickListener.onItemLongClick(holder.itemView, item, holder.getAbsoluteAdapterPosition());
            return false;
        });
         binding.itemValue.setOnCheckedChangeListener((compoundButton, b) -> {
            // Log.e("Switch", "onCheckedChanged: 状态改变了");
            if (item.getTypeId() == SiemensTypeId.Bool) {
                if (b != Boolean.parseBoolean(item.getValue().get())) {
                    //   Log.e("Switch", "onCheckedChanged: 写入数值");
                    mState.reqWriteDataToOPC(item, b, P::showWriteCallback);
                }

            }
        });
    }


}
