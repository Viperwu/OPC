package com.viper.app.domain.request;

import androidx.collection.SimpleArrayMap;

import com.viper.app.data.repository.RepositoryManger;


/**
 * 请求管理类
 */
public class RequestManger {

    private static final RequestManger INSTANCE = new RequestManger();
    private SimpleArrayMap<String, BaseOPCNodeRequest> baseOPCNodeRequests;
    private SimpleArrayMap<String, BasePageNodeRequest> basePageNodeRequest;
    private RequestManger(){
    }

    private static SimpleArrayMap<String, BaseOPCNodeRequest> getBaseOPCNodeRequests() {
        if (INSTANCE.baseOPCNodeRequests == null){
            INSTANCE.baseOPCNodeRequests = new SimpleArrayMap<>();
        }

        return INSTANCE.baseOPCNodeRequests;
    }

    private static SimpleArrayMap<String, BasePageNodeRequest> getBasePageNodeRequests() {
        if (INSTANCE.basePageNodeRequest == null){
            INSTANCE.basePageNodeRequest = new SimpleArrayMap<>();
        }

        return INSTANCE.basePageNodeRequest;
    }

    public static BaseOPCNodeRequest getBaseOPCNodeRequest(String id){
        if (getBaseOPCNodeRequests().get(id)==null){
            getBaseOPCNodeRequests().put(id,new BaseOPCNodeRequest(RepositoryManger.getBaseOpcDataRepository(id)));
        }
        return getBaseOPCNodeRequests().get(id);
    }
    public static BasePageNodeRequest getBasePageNodeRequest(String id){
        if (getBasePageNodeRequests().get(id)==null){
            getBasePageNodeRequests().put(id,new BasePageNodeRequest(RepositoryManger.getBasePageDataRepository(id)));
        }
        return getBasePageNodeRequests().get(id);
    }


}
