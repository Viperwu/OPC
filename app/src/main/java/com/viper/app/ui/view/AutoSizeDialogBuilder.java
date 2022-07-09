package com.viper.app.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.viper.app.R;
import com.viper.app.data.bean.SpinnerItem;
import com.viper.app.ui.page.adapter.SpinnerEventEditAdapter;
import java.util.ArrayList;
import java.util.List;

public class AutoSizeDialogBuilder extends QMUIDialog.AutoResizeDialogBuilder {

    private final Context mContext;
    private final List<SpinnerItem> list;
    public AutoSizeDialogBuilder(Context context,List<SpinnerItem> list) {
        super(context);
        mContext = context;
        this.list = list;
    }

    @SuppressLint("InflateParams")
    @Override
    public View onBuildContent(@NonNull QMUIDialog dialog, @NonNull Context context) {
        final View dialogView = LayoutInflater.from(mContext)
            .inflate(R.layout.dialog_spinner_event, null);
        RecyclerView recyclerView = dialogView.findViewById(R.id.rv);
        Button button = dialogView.findViewById(R.id.add_button);

        SpinnerEventEditAdapter adapter = new SpinnerEventEditAdapter(mContext,list);
        button.setOnClickListener(view1 -> {
            list.add(new SpinnerItem("",""));
            adapter.notifyItemInserted(list.size());
        });
        recyclerView.setAdapter(adapter);
        QMUIDialog.CustomDialogBuilder customDialogBuilder = new QMUIDialog.CustomDialogBuilder(mContext);

        return dialogView;
    }
}
