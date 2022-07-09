package com.viper.app.ui.page.adapter;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.popup.QMUIQuickAction;
import com.viper.app.R;
import com.viper.app.data.bean.OPCSetting;
import com.viper.app.databinding.SettingOpcItemBinding;
import com.viper.app.ui.state.SettingViewModel;
import com.viper.app.ui.view.CallBackView;
import com.viper.app.util.U;
import com.viper.base.databinding.recyclerview.adapter.SimpleDataBindingAdapter;

import com.viper.opc.client.opcua.stack.core.Identifiers;
import com.viper.opc.client.opcua.stack.core.types.builtin.NodeId;

import java.util.ArrayList;
import java.util.List;

/**
 * setting fragment opc 适配器
 */
public class SettingOpcItemAdapter extends SimpleDataBindingAdapter<OPCSetting, SettingOpcItemBinding> {
    private SettingViewModel settingViewModel;

    public SettingOpcItemAdapter(FragmentActivity activity, SettingViewModel settingViewModel) {
        super(activity, R.layout.setting_opc_item, DiffUtils.getInstance().getSettingItemCallback());

        this.settingViewModel = settingViewModel;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onBindItem(SettingOpcItemBinding binding, OPCSetting item, RecyclerView.ViewHolder holder) {
        binding.setData(item);
        Context context = holder.itemView.getContext();
        holder.itemView.setOnLongClickListener(view -> {
            longClick(context,view,item);
            return false;
        });

        binding.title.setOnClickListener(view -> {
            CallBackView.showEditTextDialog(view, "请输入新的名称", item.getTitle(), text -> {
                item.setTitle(text.trim());
                settingViewModel.saveOpcSettingToLocal(item);
                notifyItemChanged(holder.getPosition());
            });
        });

        binding.title.setOnLongClickListener(view -> {
            longClick(context,view,item);
            return false;
        });

        binding.tvAddress.setOnLongClickListener(view -> {
            longClick(context,view,item);
            return false;
        });


        binding.tvAddress.setOnClickListener(view -> {
            CallBackView.showEditTextDialog(view, "请输入新的OPC地址", item.getAddress(), text -> {
                item.setAddress(text.trim());
                settingViewModel.saveOpcSettingToLocal(item);
                notifyItemChanged(holder.getPosition());
            });
        });
        binding.tvNode.setOnLongClickListener(view -> {
            longClick(context,view,item);
            return false;
        });
        binding.tvNodeName.setOnLongClickListener(view -> {
            longClick(context,view,item);
            return false;
        });
        binding.tvNode.setOnClickListener(view -> changeNodeId(view,item,holder.getPosition()));
        binding.tvNodeName.setOnClickListener(view -> changeNodeId(view,item,holder.getPosition()));

    }

    private void longClick(Context context, View view, OPCSetting opcSetting){
        QMUIPopups.quickAction(context,
            QMUIDisplayHelper.dp2px(context, 56),
            QMUIDisplayHelper.dp2px(context, 56))
            .shadow(true)
            .skinManager(QMUISkinManager.defaultInstance(context))
            .edgeProtection(QMUIDisplayHelper.dp2px(context, 20))
            .addAction(new QMUIQuickAction.Action().icon(R.drawable.ic_round_delete_24).text(U.getSting(R.string.delete_current)).onClick(
                (quickAction, action, position) -> {
                    quickAction.dismiss();
                    settingViewModel.deleteOpcSetting(opcSetting);
                }
            ))
            .show(view);
    }


    private void changeNodeId(View view,OPCSetting item,int position){
        List<String> data = new ArrayList<>();
        data.add(U.getString(R.string.root_node));
        data.add(U.getString(R.string.plc_node));
        data.add(U.getString(R.string.data_node));
        data.add(U.getString(R.string.input_node));
        data.add(U.getString(R.string.output_node));
        data.add(U.getString(R.string.memory_node));
        data.add(U.getString(R.string.customer_node));
        CallBackView.showPopup(view, data, i -> {
            NodeId nodeId = null;
            switch (i){
                case 0:nodeId = Identifiers.RootFolder;break;
                case 1:nodeId = new NodeId(3,"PLC");break;
                case 2:nodeId = new NodeId(3,"DataBlocksGlobal");break;
                case 3:nodeId = new NodeId(3,"Inputs");break;
                case 4:nodeId = new NodeId(3,"Outputs");break;
                case 5:nodeId = new NodeId(3,"Memory");break;
                case 6:showChangeNodeIdDialog(view,item,position);item.setBrowseNodeShortName(data.get(i));break;
            }

            if (nodeId!=null){
                item.setNodeId(nodeId);
                item.setBrowseNodeShortName(data.get(i));
                settingViewModel.saveOpcSettingToLocal(item);
                notifyItemChanged(position);
            }
        });

    }

    private void showChangeNodeIdDialog(View view,OPCSetting item,int position){

        CallBackView.showEditTextDialog(view, "自定义节点", item.getBrowseNodeInfo(), text -> {
            item.setNodeId(NodeId.parseOrNull(text.trim()));
            settingViewModel.saveOpcSettingToLocal(item);
            notifyItemChanged(position);
        });
    }

}
