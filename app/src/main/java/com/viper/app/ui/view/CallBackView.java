package com.viper.app.ui.view;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.qmuiteam.qmui.skin.QMUISkinHelper;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.skin.QMUISkinValueBuilder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.popup.QMUIQuickAction;
import com.viper.app.R;
import com.viper.app.data.bean.OPCNode;
import com.viper.app.data.bean.SpinnerItem;
import com.viper.app.data.client.SiemensType;
import com.viper.app.util.U;
import com.viper.opc.client.opcua.stack.core.BuiltinDataType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 弹窗工具类
 */
public final class CallBackView {

    private static QMUIPopup popup;
    private static final int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    private CallBackView() {

    }

    public static void showConfigDialog(View view, Drawable icon, String title, String mess, IStringCallBack callBack) {


        new AlertDialog.Builder(view.getContext())
            .setIcon(icon)
            .setTitle(title)
            .setMessage(mess)
            .setPositiveButton(U.getSting(R.string.sure),
                (dialog, which) -> callBack.callBack(String.valueOf(which)))
            .setNegativeButton(U.getSting(R.string.cancel),
                (dialog, which) -> {
                })
            .show();
    }

    public static void showEditTextDialog(View view, String title, String tipText, IStringCallBack callBack) {
        final EditText editText = new EditText(view.getContext());
        editText.setText(tipText);
        AlertDialog.Builder inputDialog =
            new AlertDialog.Builder(view.getContext());
        inputDialog.setTitle(title).setView(editText);
        inputDialog.setPositiveButton(U.getSting(R.string.sure),
            (dialog, which) -> callBack.callBack(editText.getText().toString()))
            .setNegativeButton(U.getSting(R.string.cancel),
                (dialog, which) -> {
                })
            .show();
    }

    public static void showEditTextNumDialog(View view, String title, String tipText, IIntCallBack callBack) {

        final View dialogView = LayoutInflater.from(view.getContext())
            .inflate(R.layout.dialog_customize, null);
        final EditText editText = dialogView.findViewById(R.id.edit_text);
        editText.setText(tipText);
        new AlertDialog.Builder(view.getContext())
            .setTitle(title)
            .setView(dialogView)
            .setPositiveButton(U.getSting(R.string.sure),
                (dialog, which) -> {
                    // 获取EditView中的输入内容
                    callBack.callBack(Integer.parseInt(editText.getText().toString()));

                })
            .setNegativeButton(U.getSting(R.string.cancel),
                (dialog, which) -> {
                })
            .show();
    }

    public static void showSpinnerEventEditDialog(View view, String title, LinkedHashMap<String, String> map, IMapCallBack callBack) {
        List<SpinnerItem> list = new ArrayList<>();
        if (map != null && !map.isEmpty()) {
            map.forEach((k, v) -> list.add(new SpinnerItem(k, v)));
        }
        AutoSizeDialogBuilder builder = new AutoSizeDialogBuilder(view.getContext(), list);
        builder.setTitle(title)
            .addAction(U.getSting(R.string.cancel), (dialog, index) -> dialog.dismiss())
            .addAction(U.getSting(R.string.sure), (dialog, index) -> {
                LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
                if (!list.isEmpty()) {
                    list.forEach(i -> linkedHashMap.put(i.getKey(), i.getValue()));
                }
                callBack.callBack(linkedHashMap);
                dialog.dismiss();
            })
            .create(mCurrentDialogStyle).show();
    }


    public static void showTextPopup(View view, String content) {
        TextView textView = new TextView(view.getContext());
        textView.setLineSpacing(QMUIDisplayHelper.dp2px(view.getContext(), 4), 1.0f);
        int padding = QMUIDisplayHelper.dp2px(view.getContext(), 20);
        textView.setPadding(padding, padding, padding, padding);
        textView.setText(content);
        QMUISkinValueBuilder builder = QMUISkinValueBuilder.acquire();
        QMUISkinHelper.setSkinValue(textView, builder);
        builder.release();
        popup = QMUIPopups.popup(view.getContext(), QMUIDisplayHelper.dp2px(view.getContext(), 250))
            .preferredDirection(QMUIPopup.DIRECTION_BOTTOM)
            .view(textView)
            .skinManager(QMUISkinManager.defaultInstance(view.getContext()))
            .edgeProtection(QMUIDisplayHelper.dp2px(view.getContext(), 20))
            .offsetX(QMUIDisplayHelper.dp2px(view.getContext(), 20))
            .offsetYIfBottom(QMUIDisplayHelper.dp2px(view.getContext(), 5))
            .shadow(true)
            .arrow(true)
            .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
            .onDismiss(() -> {
                if (popup != null) {
                    popup.dismiss();
                    popup = null;
                }
            })
            .show(view);
    }

    public static void showPopup(View view, List<String> data, IIntCallBack callBack) {
        // popup = U.getQmuiPopup();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), R.layout.simple_list_item, data);
        AdapterView.OnItemClickListener onItemClickListener = (adapterView, view1, i, l) -> {
            //  Toast.makeText(getActivity(), "Item " + (i + 1), Toast.LENGTH_SHORT).show();
            callBack.callBack(i);
            if (popup != null) {
                popup.dismiss();
                popup = null;
            }
        };
        popup = QMUIPopups.listPopup(view.getContext(),
            QMUIDisplayHelper.dp2px(view.getContext(), 250),
            QMUIDisplayHelper.dp2px(view.getContext(), 300),
            adapter,
            onItemClickListener)
            .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
            .preferredDirection(QMUIPopup.DIRECTION_TOP)
            .shadow(true)
            .offsetYIfTop(QMUIDisplayHelper.dp2px(view.getContext(), 5))
            .skinManager(QMUISkinManager.defaultInstance(view.getContext()))
            .onDismiss(() -> {
                // Toast.makeText(getContext(), "onDismiss", Toast.LENGTH_SHORT).show();
            })
            .show(view);
    }

   /* public static void showQuickAction(View view, LinkedHashMap<String,Drawable> data, IIntCallBack callBack){
        if (!data.isEmpty()){
            QMUIQuickAction quickActions = QMUIPopups.quickAction(view.getContext(),
                QMUIDisplayHelper.dp2px(view.getContext(), 56),
                QMUIDisplayHelper.dp2px(view.getContext(), 56))
                .shadow(true)
                .skinManager(QMUISkinManager.defaultInstance(view.getContext()))
                .edgeProtection(QMUIDisplayHelper.dp2px(view.getContext(), 20));

            data.forEach((s,d)->{
                quickActions.addAction(new QMUIQuickAction.Action().icon(d).text(s).onClick(
                    (quickAction, action, position) -> {
                        quickAction.dismiss();
                        callBack.callBack(position);
                    }
                ));
            });
            quickActions.show(view);
        }



    }
*/


    /**
     * @param view     view
     * @param data     名称和icon的map
     * @param callBack 回调函数
     */
    public static void showQuickAction(View view, LinkedHashMap<Integer, Integer> data, IIntCallBack callBack) {
        if (!data.isEmpty()) {
            QMUIQuickAction quickActions = QMUIPopups.quickAction(view.getContext(),
                QMUIDisplayHelper.dp2px(view.getContext(), 56),
                QMUIDisplayHelper.dp2px(view.getContext(), 56))
                .shadow(true)
                .skinManager(QMUISkinManager.defaultInstance(view.getContext()))
                .edgeProtection(QMUIDisplayHelper.dp2px(view.getContext(), 20));

            data.forEach((s, i) -> quickActions.addAction(new QMUIQuickAction.Action().icon(U.getDrawable(i)).text(U.getString(s)).onClick(
                (quickAction, action, position) -> {
                    quickAction.dismiss();
                    callBack.callBack(position);


                }
            )));
            quickActions.show(view);
        }


    }


    public static void showListDialog(View view, String[] data, String title, IIntCallBack callBack) {
        new AlertDialog.Builder(view.getContext())
            .setTitle(title)
            .setItems(data, (dialog, which) -> callBack.callBack(which))
            .show();

    }

    public static void showLoadingPopup(View view, OPCNode opcNode) {
        final TextView textView = new TextView(view.getContext());
        textView.setLineSpacing(QMUIDisplayHelper.dp2px(view.getContext(), 4), 1.0f);
        int padding = QMUIDisplayHelper.dp2px(view.getContext(), 20);
        textView.setPadding(padding, padding, padding, padding);

        textView.setText(new StringBuilder()
            .append("节点信息：").append(opcNode.getNodeInfo()).append("\n")
            .append("java数据类型：").append(opcNode.getType()).append("\n")
            .append("数据类型标识：").append(opcNode.getTypeInfo()).append("\n")
            .append("对应西门子类型：").append(SiemensType.getType(opcNode.getTypeId())).append("\n")
            .append("当前值：").append(opcNode.getValue().get()).append("\n")
            .append("显示名称：").append(opcNode.getName()).append("\n").toString());

        popup = QMUIPopups.popup(view.getContext(), QMUIDisplayHelper.dp2px(view.getContext(), 250))
            .preferredDirection(QMUIPopup.DIRECTION_BOTTOM)
            .view(textView)
            .edgeProtection(QMUIDisplayHelper.dp2px(view.getContext(), 20))
            .dimAmount(0.6f)
            .skinManager(QMUISkinManager.defaultInstance(view.getContext()))
            .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
            .onDismiss(() -> {
                if (popup != null) {
                    popup.dismiss();
                    popup = null;
                }
            })
            .show(view);
       /* textView.post(new Runnable() {
            @Override
            public void run() {
                UaVariableNode variableNode = request.getVariableNode(opcNode.getNodeId());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("节点信息：").append(opcNode.getNodeInfo()).append("\"n")
                    .append("数据类型：").append(opcNode.getType()).append("\"n")
                    .append("当前值：").append(opcNode.getValue()).append("\"n")
                    .append("显示名称：").append(opcNode.getName()).append("\"n")
                    ;
                stringBuilder.append("");
                textView.setText(stringBuilder.toString());
            }
        });*/
    }


}
