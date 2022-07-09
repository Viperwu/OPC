package com.viper.app.data.repository;

import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;


/**
 * 仓库管理类
 */
public class RepositoryManger {
    private static final RepositoryManger INSTANCE = new RepositoryManger();

    private ArrayMap<String, BaseOpcDataRepository> opcDataRepositorys;
    private ArrayMap<String, BasePageDataRepository> pageDataRepositorys;
    private RepositoryManger(){

    }


    public static ArrayMap<String, BaseOpcDataRepository> getOpcDataRepository() {
        if (INSTANCE.opcDataRepositorys == null){
            INSTANCE.opcDataRepositorys = new ArrayMap<>();
        }
        return INSTANCE.opcDataRepositorys;
    }

    public static ArrayMap<String, BasePageDataRepository> getPageDataRepository() {
        if (INSTANCE.pageDataRepositorys == null){
            INSTANCE.pageDataRepositorys = new ArrayMap<>();
        }
        return INSTANCE.pageDataRepositorys;
    }

    public static BaseOpcDataRepository getBaseOpcDataRepository(String id){
        if (getOpcDataRepository().get(id)==null){
            getOpcDataRepository().put(id,new BaseOpcDataRepository());
        }
        return getOpcDataRepository().get(id);
    }

    public static BasePageDataRepository getBasePageDataRepository(String id){
        if (getPageDataRepository().get(id)==null){
            getPageDataRepository().put(id,new BasePageDataRepository());
        }
        return getPageDataRepository().get(id);
    }

    public static void destroy(){
        if (INSTANCE.opcDataRepositorys !=null){
            INSTANCE.opcDataRepositorys.forEach((s,d)->{
                d.destroy();
            });
        }

        if (INSTANCE.pageDataRepositorys !=null){
            INSTANCE.pageDataRepositorys.forEach((s,d)->{
                d.destroy();
            });
        }
    }


}
