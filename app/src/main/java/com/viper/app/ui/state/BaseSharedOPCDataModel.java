package com.viper.app.ui.state;

import androidx.collection.ArrayMap;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.reflect.TypeToken;
import com.viper.app.data.bean.OPCNode;
import com.viper.app.util.U;
import com.viper.app.R;
import java.util.List;

public class BaseSharedOPCDataModel extends ViewModel {
    private MutableLiveData<ArrayMap<Integer, List<OPCNode>>> cache;


    public MutableLiveData<ArrayMap<Integer, List<OPCNode>>> getCache() {

        if (cache == null){
            cache = new MutableLiveData<>();
            U.getCacheThreadPool().execute(() -> {
                String content = U.readFile(U.getString(R.string.cache_file_name));
                if (!U.isEmpty(content)) {
                    ArrayMap<Integer, List<OPCNode>> map = U.fromJson(content,  new TypeToken<ArrayMap<Integer, List<OPCNode>>>() {
                    }.getType());
                    if (map != null) {
                        cache.postValue(map);
                    }
                }
            });
        }

        return cache;
    }

}
