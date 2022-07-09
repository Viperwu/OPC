package com.viper.app.ui.page.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import androidx.databinding.Observable;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.viper.app.R;
import com.viper.app.data.bean.OPCNode;
import com.viper.app.data.bean.PageNode;
import com.viper.app.data.client.OPCUtil;
import com.viper.app.data.client.SiemensTypeId;
import com.viper.app.databinding.PageItemGroupBinding;
import com.viper.app.databinding.PageItemSpinnerBinding;
import com.viper.app.databinding.PageItemSwitchBinding;
import com.viper.app.ui.state.BasePageViewModel;
import com.viper.app.ui.state.BaseSharedOPCDataModel;
import com.viper.app.ui.state.SettingViewModel;
import com.viper.app.ui.view.CallBackView;
import com.viper.app.util.P;
import com.viper.app.util.U;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * page fragment适配器
 */
public class PageNodeAdapter extends BaseMultiAdapter<PageNode, ViewDataBinding> {
    private final BasePageViewModel mState;
    private final SettingViewModel settingViewModel;
    private final ArrayMap<Integer, List<OPCNode>> cache;


    public PageNodeAdapter(Context context, BasePageViewModel mState, SettingViewModel settingViewModel,ArrayMap<Integer, List<OPCNode>> cache) {

        super(context, DiffUtils.getInstance().getItemViewTypeItemCallback());
        this.mState = mState;
        this.settingViewModel = settingViewModel;
        this.cache = cache;

    }

    public RecyclerView getRecyclerView() {
        return recyclerView.get();
    }

    @Override
    protected void onBindItem(ViewDataBinding binding, PageNode item, RecyclerView.ViewHolder holder) {
        binding.setVariable(BR.data, item);
        if (binding instanceof PageItemGroupBinding){
            initEvent((PageItemGroupBinding) binding,item,holder);
        }else {
            holder.itemView.getLayoutParams().height = U.dip2px(item.getHeight());
        }


        if (binding instanceof PageItemSwitchBinding) {
            initEvent((PageItemSwitchBinding) binding,item,holder);
        }

        if (binding instanceof PageItemSpinnerBinding) {
            initEvent((PageItemSpinnerBinding) binding,item,holder);
        }

    }

    private void initEvent(PageItemSpinnerBinding binding,PageNode item, RecyclerView.ViewHolder holder){
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


    private void initEvent(PageItemSwitchBinding binding,PageNode item, RecyclerView.ViewHolder holder){
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

    private void initEvent(PageItemGroupBinding binding,PageNode item, RecyclerView.ViewHolder holder){
        binding.title.getLayoutParams().height = U.dip2px(item.getHeight());
        PageGroupItemAdapter adapter = new PageGroupItemAdapter(holder.itemView.getContext(),mState);
        binding.setVariable(BR.adapter,adapter);
        adapter.setOnItemLongClickListener((view, item12, position) -> P.itemLongClick(view, item12,position,mState,adapter,settingViewModel,item.getChild(),PageNodeAdapter.this,holder.getAbsoluteAdapterPosition()));
        adapter.setOnItemClickListener((view, item1, position) -> P.itemClick(view, item1,position,mState));
        int position = holder.getAbsoluteAdapterPosition();
        binding.title.setOnLongClickListener(view -> {
            LinkedHashMap<Integer, Integer> hashMap = new LinkedHashMap<>();
                hashMap.put(R.string.change_item_name, R.drawable.ic_baseline_account_box_24);
                hashMap.put(R.string.add_child, R.drawable.ic_baseline_add_circle_24);
                hashMap.put(R.string.paste_all_child_node, R.drawable.ic_round_content_paste_24);
                hashMap.put(R.string.change_item_width, R.drawable.ic_baseline_swap_horiz_24);
                hashMap.put(R.string.change_item_height, R.drawable.ic_baseline_swap_vert_24);
                hashMap.put(R.string.style_info, R.drawable.ic_baseline_error_outline_24);
                hashMap.put(R.string.move_item, R.drawable.ic_baseline_redo_24);
                hashMap.put(R.string.detail_item, R.drawable.ic_round_delete_24);
                CallBackView.showQuickAction(view, hashMap, i -> {
                    switch (i) {
                        case 0: P.changeItemName(view,item,position,PageNodeAdapter.this);break;
                        case 1: P.groupItemAddChild(view,item,position,PageNodeAdapter.this);break;
                        case 2: P.pasteAllChildNode(view,item,position,PageNodeAdapter.this,settingViewModel.getCopyNode().getValue(),cache);break;
                        case 3: P.changeItemWidth(view,item,position,PageNodeAdapter.this);break;
                        case 4: P.changeItemHeight(view,item,position,PageNodeAdapter.this);break;
                        case 5: P.styleInfo(item, view);break;
                        case 6: P.moveItem(view,item,PageNodeAdapter.this,mState.pageItems);break;
                        case 7: P.removeItem(item,position,mState.pageItems,PageNodeAdapter.this,null,-1);break;
                    }
                });
            return false;
        });
    }




}
