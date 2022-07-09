package com.viper.app.ui.page.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.viper.app.R;
import com.viper.app.data.bean.SpinnerItem;

import java.util.List;


public class SpinnerEventEditAdapter extends RecyclerView.Adapter<SpinnerEventEditAdapter.ViewHolder> {
    private final List<SpinnerItem> mList;
    private final Context context;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.spinner_event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int pos = position;
        SpinnerItem item = mList.get(position);
        holder.key.setText(item.getKey());
        holder.key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mList.get(pos).setKey(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.value.setText(item.getValue());

        holder.value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mList.get(pos).setValue(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        QMUIKeyboardHelper.showKeyboard(holder.key, true);
        QMUIKeyboardHelper.showKeyboard(holder.value, true);

        holder.delete.setOnClickListener(view -> {
            mList.remove(position);
            SpinnerEventEditAdapter.this.notifyItemChanged(pos);
        });
    }

    public SpinnerEventEditAdapter(Context context,List<SpinnerItem> mList) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        EditText key;
        EditText value;
        ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            key = itemView.findViewById(R.id.item_key);
            value = itemView.findViewById(R.id.item_value);
            delete = itemView.findViewById(R.id.image);
        }
    }
}
