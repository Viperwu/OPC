package com.viper.app.ui.page.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.viper.app.data.bean.PageNode;
import com.viper.base.databinding.recyclerview.adapter.BaseDataBindingAdapter;

import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * 多布局适配器根据item getLayout（）方法返回布局
 */
public abstract class BaseMultiAdapter <M extends PageNode, B extends ViewDataBinding> extends BaseDataBindingAdapter<M, B> {

    protected WeakReference<RecyclerView> recyclerView;
    public BaseMultiAdapter(Context context, @NonNull DiffUtil.ItemCallback<M> diffCallback) {
        super(context, diffCallback);
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        return position;
    }
    @Override
    protected int getLayoutResId(int viewType) {
        return getCurrentList().get(viewType).getLayout();

    }

    //获取GridLayoutManager配置参数
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
        if (manager != null) {
            manager.setSpanCount(100);
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getCurrentList().get(position).getWidth();
                }
            });
        }
        ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        this.recyclerView = new WeakReference<>(recyclerView);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView.get();
    }
}
