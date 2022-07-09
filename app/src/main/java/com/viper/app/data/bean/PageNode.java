package com.viper.app.data.bean;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import com.viper.app.R;
import com.viper.app.util.U;
import com.viper.opc.client.opcua.sdk.client.nodes.UaVariableNode;
import com.viper.opc.client.opcua.stack.core.types.builtin.DataValue;
import com.viper.opc.client.opcua.stack.core.types.builtin.NodeId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * page fragment 的实体类
 */
public class PageNode extends OPCNode {

    protected int height = 50;

    protected int width = 100;
    protected String holdValue;
    protected LinkedHashMap<String,String> map;
   protected  @LayoutRes int layout;
    protected transient boolean init;
   protected boolean nodeInfoSync;
   protected transient boolean subscribed;
   protected List<PageNode> child;

    public PageNode(@LayoutRes int layout) {
        this.layout = layout;
        this.name= U.getSting(R.string.this_is_name);
       // this.value="这是数值";
        setValue(U.getSting(R.string.this_is_value));
    }
    public PageNode(){
        this(R.layout.page_item_text);
    }

    public PageNode(@LayoutRes int layout, String name) {
        this.layout = layout;
        this.name = name;
    }

    public  @LayoutRes int getLayout(){
        return layout;
    }


    public void setLayout(@LayoutRes int layout) {
        this.layout = layout;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if (height>1000){
            this.height = 1000;
        }else this.height = Math.max(height, 20);

    }

    public void setWidth(int width) {
       if (width< 20){
            this.width = 20;
        }else this.width = Math.min(width, 100);
    }

    public int getWidth() {
        return width;
    }


    public boolean setNodeInfo(String nodeInfo) {
        if (super.setNodeInfo(nodeInfo)){
            nodeInfoSync = false;
            holdValue = "";
            return true;
        }else {
            return false;
        }
    }


    /*public boolean setDataType(NodeId dataType) {
        if (super.setDataType(dataType)){
            nodeInfoSync = true;
            return true;
        }else {
            return false;
        }
    }*/

    @Override
    public boolean setNodeId(NodeId nodeId) {
        if (super.setNodeId(nodeId)){
            resetAttribute();
            return true;
        }
        return false;
    }

    private void resetAttribute(){
        holdValue = "";
        subscribed = false;
    }


    public boolean isNodeInfoSync() {
        return nodeInfoSync;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public String getHoldValue() {
        return holdValue;
    }

    public void setHoldValue(String holdValue) {
        this.holdValue = holdValue;
    }

    public LinkedHashMap<String, String> getMap() {
        return map;
    }

    public void setMap(LinkedHashMap<String, String> map) {
        this.map = map;
    }

    public boolean isInit() {
        return init;
    }

    public List<PageNode> getChild() {
        return child;
    }

    public void setChild(List<PageNode> child) {
        this.child = child;
    }


    public void addChild(@NonNull PageNode child) {
        if (this.child == null) this.child = new ArrayList<>();
        this.child.add(child);
        child.parent = this;
        child.parentId = getId();
    }

    public void setInit(boolean init) {
        this.init = init;
    }
}
