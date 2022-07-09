package com.viper.app.data.bean;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.viper.app.data.client.OPCUtil;
import com.viper.app.data.client.SiemensType;
import com.viper.app.util.U;

import com.viper.opc.client.opcua.sdk.client.model.nodes.variables.BaseDataVariableTypeNode;
import com.viper.opc.client.opcua.sdk.client.model.nodes.variables.PropertyTypeNode;
import com.viper.opc.client.opcua.sdk.client.nodes.UaNode;
import com.viper.opc.client.opcua.sdk.client.nodes.UaVariableNode;
import com.viper.opc.client.opcua.stack.core.types.builtin.DataValue;
import com.viper.opc.client.opcua.stack.core.types.builtin.NodeId;

import java.util.ArrayList;
import java.util.List;


/**
 * opc fragment 的实体类
 */
public class OPCNode {

    protected transient UaNode uaNode;
    protected String name;
    private transient String type;
    private String siemensType;
    private boolean canExpand;
    protected transient boolean isArray;
    protected ObservableField<String> value;
    private transient ObservableBoolean currentSelected;
    protected transient OPCNode parent;
    protected transient NodeId nodeId;
    protected String nodeInfo;
    protected transient String typeInfo;
  //  protected String dataTypeNodeInfo;
   // protected transient NodeId dataType;
 //   protected transient QualifiedName qualifiedName;
  //  protected transient LocalizedText localizedText;

    protected int typeId;

    private transient List<OPCNode> childList;
   // protected boolean isExpand;
    protected transient int parentId;

    public OPCNode(String name, String value) {
        this.name = name;
      //  this.value=value;
        this.value.set(value);
    }

    public OPCNode(UaNode uaNode) {
        this.uaNode = uaNode;

      //  this.type=uaNode.getClass().getSimpleName();
        init(uaNode);
    }

    public OPCNode() {
    }

    private void init(UaNode uaNode) {
        if (uaNode instanceof BaseDataVariableTypeNode||uaNode instanceof PropertyTypeNode){
            canExpand = false;
        }else {
            canExpand = true;
        }
        String name = uaNode.getBrowseName().getName();
        if (!U.isEmpty(name)){
            setName(name);
        }

        setNodeId(uaNode.getNodeId());

        if (uaNode instanceof UaVariableNode) {
            NodeId daType = ((UaVariableNode) uaNode).getDataType();
            if (daType!=null){
                setTypeId(daType.hashCode());
                setTypeInfo(daType.toParseableString());
                String str = SiemensType.getType(daType.hashCode());
                if (!U.isEmpty(str)){
                    setSiemensType(str);
                }
            }

            try {
                DataValue dataValue = ((UaVariableNode) uaNode).readValue();
                setDataValue(dataValue);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public void setCurrentSelected(boolean isCurrentShow) {
        if (currentSelected == null){
            currentSelected = new ObservableBoolean();
        }
        currentSelected.set(isCurrentShow);
    }

    public ObservableBoolean getCurrentSelected() {
        if(currentSelected ==null){
            currentSelected = new ObservableBoolean(false);
        }
        return currentSelected;
    }

    public boolean isRoot() {
        return parent == null;
    }



    public boolean isLeaf() {
        return childList == null || childList.isEmpty();
    }

    public void addChild(OPCNode child) {
        if (childList == null) childList = new ArrayList<>();
        childList.add(child);
        child.parent = this;
        child.parentId = getId();
    }

    public String getSiemensType() {
        return siemensType;
    }

    public void setSiemensType(String siemensType) {
        this.siemensType = siemensType;
    }

    public boolean isCanExpand() {
        return canExpand;
    }


    public OPCNode getParent() {
        return parent;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!name.isEmpty())
            this.name = name.trim();
    }

    public ObservableField<String> getValue() {
        if (value == null){
            value = new ObservableField<>();
        }
        return value;
    }

    public void setValue(String value) {
        if (this.value == null){
            this.value = new ObservableField<>();
        }
        this.value.set(value);
    }

    public String getType() {
        return type;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public NodeId getNodeId() {

        if (nodeId==null){
            if (!U.isEmpty(nodeInfo)){
                try {
                    nodeId = NodeId.parse(nodeInfo);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return nodeId;

    }

    public List<OPCNode> getChildList() {
        return childList;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean setNodeId(NodeId nodeId) {
       if (nodeId!=null){
           if(this.nodeId!=null){
               if (this.nodeId.hashCode()==nodeId.hashCode()){
                   return false;
               }else {
                   return parseNode(nodeId);
               }
           }else {
               return parseNode(nodeId);
           }
       }else {
           return false;
       }
    }

    private boolean parseNode(NodeId nodeId){
        try {
            this.nodeInfo = nodeId.toParseableString();
            this.nodeId = nodeId;
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public String getTypeInfo() {
        return typeInfo;
    }

    public void setTypeInfo(String typeInfo) {
        this.typeInfo = typeInfo;
    }

/* public boolean setDataType(NodeId dataType) {
        try {
            this.dataTypeNodeInfo = dataType.toParseableString();
            this.dataType = dataType;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }*/

    public String getNodeInfo() {
        return nodeInfo;
    }


  /*  public String getDataTypeNodeInfo() {
        return dataTypeNodeInfo;
    }

    public void setDataTypeNodeInfo(String dataTypeNodeInfo) {
        this.dataTypeNodeInfo = dataTypeNodeInfo;
    }*/

    public void setChildList(List<OPCNode> childList) {
        this.childList = childList;
    }

    protected transient boolean flag;

    public void setDataValue(DataValue dataValue) {
        if (dataValue!=null){
            Object o = dataValue.getValue().getValue();
            if (o!=null){
                String str =  OPCUtil.dataValue2Str(this,o);
                if (!U.isEmpty(str)){
                    if (o instanceof Boolean){
                        if (flag){
                            getValue().set(str.toLowerCase());
                        }else {
                            getValue().set(str.toUpperCase());
                        }
                        flag=!flag;
                    }else {
                        getValue().set(str);
                    }

                }

                if (U.isEmpty(type)){
                    setType(o.getClass().getName());
                }
            }

        }
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean array) {
        isArray = array;
    }

    public boolean setNodeInfo(String nodeInfo) {
        if (U.isEmpty(nodeInfo)){
            return false;
        }else {
            if (U.isEmpty(this.nodeInfo)){
                return parseNodeInfo(nodeInfo);
            }else {
                if (this.nodeInfo.equals(nodeInfo)){
                    return false;
                }else {
                    return parseNodeInfo(nodeInfo);
                }
            }
        }
    }

    private boolean parseNodeInfo(String info){
        try {
            NodeId nodeId = NodeId.parse(info);
            this.nodeInfo = info;
            this.nodeId = nodeId;
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    public int getId() {
        if (getNodeId()!=null){
            return getNodeId().hashCode();
        }else {
            return 0;
        }

    }

    public void setParent(OPCNode parent) {
        this.parent = parent;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getDataTypeId(){
        /*int typeId = 0;
        if (getDataType()!=null){
            Object obj = getDataType().getIdentifier();
            if (obj instanceof UInteger){
                typeId = ((UInteger) obj).intValue();
            }
        }*/
        return typeId;
    }

    public void setValue(ObservableField<String> value) {
        this.value=value;
    }

    public UaNode getUaNode() {
        return uaNode;
    }
    public void getNewValue(){
        if (uaNode !=null && uaNode instanceof UaVariableNode){
            try {
                DataValue dataValue = ((UaVariableNode) uaNode).readValue();
                setDataValue(dataValue);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
