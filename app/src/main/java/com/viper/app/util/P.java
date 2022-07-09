package com.viper.app.util;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.collection.ArrayMap;

import com.viper.app.R;
import com.viper.app.data.bean.OPCNode;
import com.viper.app.data.bean.PageNode;
import com.viper.app.data.client.SiemensTypeId;
import com.viper.app.ui.page.adapter.BaseMultiAdapter;
import com.viper.app.ui.page.adapter.PageNodeAdapter;
import com.viper.app.ui.state.BasePageViewModel;
import com.viper.app.ui.state.SettingViewModel;
import com.viper.app.ui.view.CallBackView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public final class P {
    private P(){}


    public static void changeItemNodeInfo(View view, PageNode node, BasePageViewModel mState){
        CallBackView.showEditTextDialog(view, U.getString(R.string.configuration_node_info), node.getNodeInfo(), text -> {
            changeNodeInfo(node, text,mState);
        });
    }



    private static void changeNodeInfo(PageNode multi, String info,BasePageViewModel mState) {
        mState.changeNodeInfo(multi, info, b -> {
            if (b) {
                U.showShortToast("节点信息保存成功");
            } else {
                U.showShortToast("无效的节点");
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    public static void clickEvent(PageNode multi, View view, List<PageNode> list, BaseMultiAdapter adapter) {

        switch (multi.getLayout()) {
            case R.layout.page_item_switch:
                break;
            case R.layout.page_item_text:
                break;
            case R.layout.page_item_kv:
                kvClickEvent(multi, view);
                break;
            case R.layout.page_item_spinner:
                CallBackView.showSpinnerEventEditDialog(view,"配置切换选项时需要写入的数值",multi.getMap(), m->{
                    multi.setMap(m);
                    adapter.notifyItemChanged(list.indexOf(multi));
                });
                break;
            case R.layout.page_item_group:
                break;
        }
    }

    public static void kvClickEvent(PageNode multi, View view) {
        List<String> data = new ArrayList<>();
        data.add(U.getString(R.string.write_value));
        CallBackView.showPopup(view, data, i -> {
            if (i == 0) {
                CallBackView.showEditTextDialog(view, "请输入数值", multi.getHoldValue(), text -> {
                    multi.setHoldValue(text.trim());
                });
            }
        });
    }

    public static void writeValueToOpc(View view,PageNode node,BasePageViewModel mState){
        CallBackView.showEditTextDialog(view, U.getString(R.string.write_value),
            node.getValue().get(), n -> mState.reqWriteDataToOPC(node, n.trim(), P::showWriteCallback
            ));
    }

    public static void showWriteCallback(boolean b) {
        if (b) {
            U.showShortToast(U.getSting(R.string.write_success));
        } else {
            U.showShortToast(U.getSting(R.string.write_fail));
        }
    }

    public static void changeItemName(View view,PageNode node,int position, BaseMultiAdapter adapter){
        CallBackView.showEditTextDialog(view, U.getString(R.string.change_item_name),
            String.valueOf(node.getName()), n -> {
                node.setName(n);
                adapter.notifyItemChanged(position);
            });
    }

    public static void changeItemStyle(View view, PageNode node, BaseMultiAdapter adapter) {
        CallBackView.showListDialog(view, new String[]{"样式1", "样式2", "样式3","样式4"}, "", i1 -> {
            switch (i1) {
                case 0:
                    if (node.getParent()==null){
                        node.setLayout(R.layout.page_item_text);
                    }else {
                        node.setLayout(R.layout.page_item_group_text);
                    }

                    P.updateView(adapter);
                    break;
                case 1:
                    if (node.getParent()==null){
                        node.setLayout(R.layout.page_item_kv);
                    }else {
                        node.setLayout(R.layout.page_item_group_kv);
                    }
                    P.updateView(adapter);
                    break;
                case 2:
                    if (node.getParent()==null){
                        node.setLayout(R.layout.page_item_switch);
                    }else {
                        node.setLayout(R.layout.page_item_group_switch);
                    }
                    P.updateView(adapter);
                    break;
                case 3:
                    if (node.getParent()==null){
                        node.setLayout(R.layout.page_item_spinner);
                    }else {
                        node.setLayout(R.layout.page_item_group_spinner);
                    }
                    P.updateView(adapter);
                    break;
            }

        });
    }

    public  static  void updateView(BaseMultiAdapter adapter) {
        adapter.getRecyclerView().setAdapter(adapter);
    }

    public static void pasteNodeSimple(PageNode multi, OPCNode node, int position, SettingViewModel settingViewModel,BasePageViewModel mState,BaseMultiAdapter adapter) {
        if (settingViewModel.getCopyNode().getValue()!=null){
            mState.pasteItemSimple(multi, node);
            adapter.notifyItemChanged(position);
        }
    }

    public static void pasteNodeAll(PageNode multi, OPCNode node, int position, SettingViewModel settingViewModel,BasePageViewModel mState,BaseMultiAdapter adapter) {
        if (settingViewModel.getCopyNode().getValue()!=null){
            mState.pasteItemAll(multi, node);
            adapter.notifyItemChanged(position);
        }
    }

    public static void changeItemWidth(View view, PageNode node, int position,BaseMultiAdapter adapter) {
        CallBackView.showEditTextNumDialog(view, U.getString(R.string.change_item_width),
            String.valueOf(node.getWidth()), node::setWidth);
        adapter.notifyItemChanged(position);
    }


    public static void changeItemHeight(View view, PageNode node, int position,BaseMultiAdapter adapter) {
        CallBackView.showEditTextNumDialog(view, U.getString(R.string.change_item_height),
            String.valueOf(node.getHeight()), h -> {
                node.setHeight(h);
                adapter.notifyItemChanged(position);
            });
    }

    @SuppressLint("NonConstantResourceId")
    public static void styleInfo(PageNode multi, View view) {
        switch (multi.getLayout()) {
            case R.layout.page_item_switch: {
                CallBackView.showTextPopup(view, "这是一个开关类型控件只有配置了布尔量的节点时才有效，打开开关时向节点写入true,关闭时向节点写入false");
            }
            break;
            case R.layout.page_item_text: {
                CallBackView.showTextPopup(view, "这是文本类型的控件只有配置了布尔量的节点时才有效,当值为true时背景显示为绿色,点击时切换状态(向节点写入true/false)");
            }
            break;
            case R.layout.page_item_kv: {
                CallBackView.showTextPopup(view, "这是一个键值对类型的控件左侧显示名称右侧显示数值，可以自定义单击事件");
            }
            break;
            case R.layout.page_item_spinner: {
                CallBackView.showTextPopup(view, "这是下拉列表型控件，可以定义每项对应的数值切换选项时将写入相应的数值");
            }
            break;
            case R.layout.page_item_group: {
                CallBackView.showTextPopup(view, "这是容器控件，可以添加相应的子控件");
            }
            break;
        }
    }
    public static void nodeDetail(View view,PageNode node){
        CallBackView.showLoadingPopup(view, node);
    }

    public static void moveItem(View view, PageNode multi,BaseMultiAdapter adapter,List<PageNode> list) {
        List<String> data = new ArrayList<>();
        data.add("移动到开头");
        data.add("移动到末尾");
        data.add("移动指定位");
        CallBackView.showPopup(view, data, i -> {
            int index = list.indexOf(multi);
            switch (i){
                case 0:{
                    list.remove(multi);
                    adapter.notifyItemRemoved(index);
                    list.add(0,multi);
                    adapter.notifyItemInserted(0);
                }break;
                case 1:{
                    list.remove(multi);
                    adapter.notifyItemRemoved(index);
                    list.add(multi);
                    adapter.notifyItemInserted(list.indexOf(multi));

                }break;
                case 2:{
                    CallBackView.showEditTextNumDialog(view, "请输入要移动的位置", String.valueOf(list.indexOf(multi)), i2 -> {
                        if (i2>=0 && i2<list.size()){
                            list.remove(multi);
                            adapter.notifyItemRemoved(index);
                            list.add(i2,multi);
                            adapter.notifyItemInserted(i2);
                        }
                    });

                }  break;
            }
        });

    }
    public static void removeItem(PageNode node,int position,List<PageNode> list,BaseMultiAdapter adapter,BaseMultiAdapter parentAdapter,int indexOfParent){
       // mState.removeItem(node);
        list.remove(node);
        adapter.notifyItemRemoved(position);
        if (parentAdapter !=null){
            parentAdapter.notifyItemChanged(indexOfParent);
        }

    }

    public static void groupItemAddChild(View view, PageNode node, int position,BaseMultiAdapter adapter) {
        List<String> data = new ArrayList<>();
        data.add("增加一个子控件样式1");
        data.add("增加一个子控件样式2");
        data.add("增加一个子控件样式3");
        data.add("增加一个子控件样式4");
        //data.add("更多样式开发中...");

        CallBackView.showPopup(view, data, i -> {
            switch (i) {
                case 0: groupItemAddChild(R.layout.page_item_group_text,node,position,adapter);break;
                case 1:groupItemAddChild(R.layout.page_item_group_kv,node,position,adapter);break;
                case 2:groupItemAddChild(R.layout.page_item_group_switch,node,position,adapter);break;
                case 3:groupItemAddChild(R.layout.page_item_group_spinner,node,position,adapter);break;
            }
        });

    }

    public static void groupItemAddChild(@LayoutRes int id, PageNode node, int position,BaseMultiAdapter adapter){
        node.addChild(new PageNode(id));
        adapter.notifyItemChanged(position);
    }


    //单击事件
    @SuppressLint("NonConstantResourceId")
    public static  void itemClick(View view, PageNode node, int position,BasePageViewModel mState) {
        switch (node.getLayout()) {
            case R.layout.page_item_switch:
                break;
            case R.layout.page_item_text:
                textItemClick(view, node,mState);
                break;
            case R.layout.page_item_kv:
                kvItemClick(view, node,mState);
                break;
            case R.layout.page_item_spinner:
                // kvItemClick(view, multi);
                break;
        }
    }

    public static void textItemClick(View view, PageNode node,BasePageViewModel mState) {
        if (node.getTypeId() == SiemensTypeId.Bool) {
            String s = node.getValue().get();
            if (!U.isEmpty(s)) {
                try {
                    boolean b = Boolean.parseBoolean(s);
                    if (b) {
                        mState.reqWriteDataToOPC(node, "false", P::showWriteCallback);
                    } else {
                        mState.reqWriteDataToOPC(node, "true", P::showWriteCallback);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // mState.reqWriteDataToOPC(multi,multi.getHoldValue(), this::showWriteCallback);
        }
    }

    public static void kvItemClick(View view, PageNode node,BasePageViewModel mState) {
        if (!U.isEmpty(node.getHoldValue())) {
            mState.reqWriteDataToOPC(node, node.getHoldValue(), P::showWriteCallback);
        }
    }
    //长按事件
    public static void itemLongClick(View view, PageNode node, int position,final BasePageViewModel mState, final BaseMultiAdapter adapter,final SettingViewModel settingViewModel,
                                     List<PageNode> list,final BaseMultiAdapter parentAdapter,int indexOfParent) {
        int layout = node.getLayout();
        LinkedHashMap<Integer, Integer> hashMap = new LinkedHashMap<>();
        if (layout == R.layout.page_item_group){
            /*hashMap.put(R.string.change_item_name, R.drawable.ic_baseline_account_box_24);
            hashMap.put(R.string.add_child, R.drawable.ic_baseline_add_circle_24);
            hashMap.put(R.string.change_item_width, R.drawable.ic_baseline_swap_horiz_24);
            hashMap.put(R.string.change_item_height, R.drawable.ic_baseline_swap_vert_24);
            hashMap.put(R.string.style_info, R.drawable.ic_baseline_error_outline_24);
            hashMap.put(R.string.move_item, R.drawable.ic_baseline_redo_24);
            hashMap.put(R.string.detail_item, R.drawable.ic_round_delete_24);
            CallBackView.showQuickAction(view, hashMap, i -> {
                switch (i) {
                    case 0: P.changeItemName(view,node,position,adapter);break;
                    case 1: P.groupItemAddChild(view,node,position,adapter);break;
                    case 2: P.changeItemWidth(view,node,position,adapter);break;
                    case 3: P.changeItemHeight(view,node,position,adapter);break;
                    case 4: P.styleInfo(node, view);break;
                    case 5: P.moveItem(view,node,mState,adapter);break;
                    case 6: P.removeItem(node,position,mState,adapter);break;
                }
            });*/
        }else {
            hashMap.put(R.string.set_node_info, R.drawable.ic_baseline_article_24);
            if (layout==R.layout.page_item_spinner){
                hashMap.put(R.string.set_spinner_event, R.drawable.ic_baseline_touch_app_24);
            } else {
                hashMap.put(R.string.set_click_event, R.drawable.ic_baseline_touch_app_24);
            }

            hashMap.put(R.string.write_value, R.drawable.ic_baseline_create_24);
            hashMap.put(R.string.change_item_name, R.drawable.ic_baseline_account_box_24);
            hashMap.put(R.string.change_item_style, R.drawable.ic_baseline_data_usage_24);
            hashMap.put(R.string.paste_node_info, R.drawable.ic_baseline_content_paste_24);
            hashMap.put(R.string.paste_node_all_info, R.drawable.ic_round_content_paste_24);
            hashMap.put(R.string.change_item_width, R.drawable.ic_baseline_swap_horiz_24);
            hashMap.put(R.string.change_item_height, R.drawable.ic_baseline_swap_vert_24);
            hashMap.put(R.string.style_info, R.drawable.ic_baseline_error_outline_24);
            hashMap.put(R.string.item_detail, R.drawable.ic_baseline_info_24);
            hashMap.put(R.string.move_item, R.drawable.ic_baseline_redo_24);
            hashMap.put(R.string.detail_item, R.drawable.ic_round_delete_24);

            CallBackView.showQuickAction(view, hashMap, i -> {
                switch (i) {
                    case 0: P.changeItemNodeInfo(view,node,mState);break;
                    case 1: P.clickEvent(node, view,list,adapter);break;
                    case 2: P.writeValueToOpc(view,node,mState);break;
                    case 3: P.changeItemName(view,node,position,adapter);break;
                    case 4: P.changeItemStyle(view,node,adapter);break;
                    case 5: P.pasteNodeSimple(node, settingViewModel.getCopyNode().getValue(), position,settingViewModel,mState,adapter);break;
                    case 6: P.pasteNodeAll(node, settingViewModel.getCopyNode().getValue(), position,settingViewModel,mState,adapter);break;
                    case 7: P.changeItemWidth(view,node,position,adapter);break;
                    case 8: P.changeItemHeight(view,node,position,adapter);break;
                    case 9: P.styleInfo(node, view);break;
                    case 10: P.nodeDetail(view,node);break;
                    case 11: P.moveItem(view,node,adapter,list);break;
                    case 12: P.removeItem(node,position,list,adapter,parentAdapter,indexOfParent);break;
                }

            });
        }

    }


    public static void pasteAllChildNode(View view, PageNode item, int position, PageNodeAdapter adapter, OPCNode parent, ArrayMap<Integer, List<OPCNode>> cache) {
        if (parent!=null){
            List<OPCNode> opcNodes = cache.get(parent.getId());
            if (opcNodes!=null&&!opcNodes.isEmpty()){
                opcNodes.forEach(n->{
                    if (!n.isCanExpand()){
                        PageNode pageNode = new PageNode(R.layout.page_item_group_kv);
                        pageNode.setName(n.getName());
                        //  pageNode.setValue(pageNode.getValue());
                        pageNode.setTypeId(n.getTypeId());
                        pageNode.setNodeInfo(n.getNodeInfo());
                        item.addChild(pageNode);
                    }

                });
                adapter.notifyItemChanged(position);
            }

        }

    }
}
