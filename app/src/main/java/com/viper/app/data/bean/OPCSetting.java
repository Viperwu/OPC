package com.viper.app.data.bean;

import com.viper.app.R;
import com.viper.app.util.U;

import com.viper.opc.client.opcua.stack.core.Identifiers;
import com.viper.opc.client.opcua.stack.core.types.builtin.NodeId;


/**
 * setting的实体类
 */
public class OPCSetting {
    private long id;
    private String title;
    private String address;
    private String browseNodeShortName;
    private String browseNodeInfo;
    private transient NodeId nodeId;


    /*public OPCSetting(String key) {
        address = U.getSting(R.string.opc_def_uri);
      //  nodeId = Identifiers.RootFolder;
        browseNodeShortName = U.getSting(R.string.root_node);
        browseNodeInfo = Identifiers.RootFolder.toParseableString();
        this.title = key;
    }*/

    public OPCSetting(long id) {
        address = U.getSting(R.string.opc_def_uri);
        //  nodeId = Identifiers.RootFolder;
        browseNodeShortName = U.getSting(R.string.root_node);
        browseNodeInfo = Identifiers.RootFolder.toParseableString();
        title = U.getSting(R.string.opc_def_name);
        this.id = id;
    }

   /* public OPCSetting() {
        address = U.getSting(R.string.opc_def_uri);
        //  nodeId = Identifiers.RootFolder;
        browseNodeShortName = U.getSting(R.string.root_node);
        browseNodeInfo = Identifiers.RootFolder.toParseableString();
        title = U.getSting(R.string.opc_def_name);
        this.id = System.currentTimeMillis();
    }*/

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBrowseNodeShortName() {
        return browseNodeShortName;
    }

    public void setBrowseNodeShortName(String browseNodeShortName) {
        this.browseNodeShortName = browseNodeShortName;
    }

    public String getBrowseNodeInfo() {
        return browseNodeInfo;
    }

    public void setBrowseNodeInfo(String browseNodeInfo) {
        this.browseNodeInfo = browseNodeInfo;
    }

    public NodeId getNodeId() {
        if (nodeId == null){
            /*try {
                nodeId = NodeId.parse(browseNodeInfo);
            }catch (Exception e){
                e.printStackTrace();
            }*/

            nodeId = NodeId.parseOrNull(browseNodeInfo);
        }
        return nodeId;
    }

    public void setNodeId(NodeId nodeId) {
        try {
            browseNodeInfo = nodeId.toParseableString();
        }catch (Exception e){
            e.printStackTrace();
        }

        this.nodeId = nodeId;
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
