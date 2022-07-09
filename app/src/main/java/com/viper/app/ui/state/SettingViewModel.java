package com.viper.app.ui.state;

import android.util.Log;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.gson.reflect.TypeToken;
import com.viper.app.R;
import com.viper.app.data.bean.OPCNode;
import com.viper.app.data.bean.OPCSetting;
import com.viper.app.data.bean.PageSetting;
import com.viper.app.util.U;
import com.viper.base.utils.SPUtils;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class SettingViewModel extends ViewModel {
   // public final MutableLiveData<Boolean> init = new MutableLiveData<>(false);
    public final ObservableArrayList<Long> opcSettingIds = new ObservableArrayList<>();
    public final ObservableArrayList<Long> pageSettingIds = new ObservableArrayList<>();
    public final   ObservableArrayList<OPCSetting> opcSettings = new ObservableArrayList<>();
    public final ObservableArrayList<PageSetting> pageSettings = new ObservableArrayList<>();
    private MutableLiveData<OPCNode> copyNode;

    //public final MutableLiveData<Setting> setting = new MutableLiveData<>();

    public void resetOpcSetting(){
        long opcSettingId = System.currentTimeMillis();
        opcSettingIds.clear();
        opcSettingIds.add(opcSettingId);
        /*if (opcSettingIds.getValue() ==null){
            List<Long> list = new ArrayList<>();
            list.add(opcSettingId);
            opcSettingIds.postValue(list);
        }else {
            opcSettingIds.getValue().add(opcSettingId);
        }*/
       /* opcSettingIds.clear();
        opcSettingIds.add(opcSettingId);*/
        OPCSetting opcSetting = new OPCSetting(opcSettingId);
       /* List<OPCSetting> opcSettingList = new ArrayList<>();
        opcSettingList.add(opcSetting);
        opcSettings.postValue(opcSettingList);*/
       this.opcSettings.clear();
        this.opcSettings.add(opcSetting);
        saveOpcSettingIdsToLocal();
        saveOpcSettingToLocal(opcSetting);
    }

    public void resetPageSetting(){
        long pageSettingId = System.currentTimeMillis();
        pageSettingIds.clear();
        pageSettingIds.add(pageSettingId);
      /*  List<Long> list = new ArrayList<>();
        list.add(pageSettingId);
        pageSettingIds.postValue(list);*/
       /* if (pageSettingIds.getValue() ==null){
            List<Long> list = new ArrayList<>();
            list.add(pageSettingId);
            pageSettingIds.postValue(list);
        }else {
            pageSettingIds.getValue().add(pageSettingId);
        }*/

       /* pageSettingIds.clear();
        pageSettingIds.add(pageSettingId);*/
        PageSetting pageSetting = new PageSetting(pageSettingId);
       /* List<PageSetting> pageSettingList = new ArrayList<>();
        pageSettingList.add(pageSetting);
        this.pageSettings.postValue(pageSettingList);*/
        this.pageSettings.clear();
        this.pageSettings.add(pageSetting);
        savePageSettingIdsToLocal();
        savePageSettingToLocal(pageSetting);
    }


    public void resetAll(){
        SPUtils.getInstance(U.getString(R.string.setting)).clear();
        resetPageSetting();
        resetOpcSetting();
        SPUtils.getInstance(U.getString(R.string.setting)).put(U.getString(R.string.setting_init), true);
    }


    public void addPage(){
        long pageId = System.currentTimeMillis();
        pageSettingIds.add(pageId);
        /*if (pageSettingIds.getValue() ==null){
            List<Long> list = new ArrayList<>();
            list.add(pageId);
            pageSettingIds.postValue(list);
        }else {
            pageSettingIds.getValue().add(pageId);
        }*/
      //  pageSettingIds.add(pageId);
        PageSetting pageSetting = new PageSetting(pageId);
        this.pageSettings.add(pageSetting);
        /*if (this.pageSettings.getValue() == null){
            List<PageSetting> pageSettingList = new ArrayList<>();
            pageSettingList.add(pageSetting);
            this.pageSettings.postValue(pageSettingList);
        }else {
            this.pageSettings.getValue().add(pageSetting);
        }*/

        savePageSettingIdsToLocal();
        savePageSettingToLocal(pageSetting);
    }

    public void addOpc(){
        long opcId = System.currentTimeMillis();
        opcSettingIds.add(opcId);
       /* if (opcSettingIds.getValue() ==null){
            List<Long> list = new ArrayList<>();
            list.add(opcId);
            opcSettingIds.postValue(list);
        }else {
            opcSettingIds.getValue().add(opcId);
        }
*/
        //opcSettingIds.add(opcId);
        OPCSetting opcSetting = new OPCSetting(opcId);
       /* if ( this.opcSettings.getValue() == null){
            List<OPCSetting> opcSettingList = new ArrayList<>();
            opcSettingList.add(opcSetting);
            this.opcSettings.postValue(opcSettingList);
        }else {
            this.opcSettings.getValue().add(opcSetting);
        }*/

        this.opcSettings.add(opcSetting);
        saveOpcSettingIdsToLocal();
        saveOpcSettingToLocal(opcSetting);
    }

    public void saveOpcSettingIdsToLocal(){
        saveIdsToLocal(U.getString(R.string.opc_setting_ids),opcSettingIds);
       // saveIdsToLocal(U.getString(R.string.opc_setting_ids),opcSettingIds);
        /*SPUtils.getInstance(U.getString(R.string.setting)).put(U.getString(R.string.opc_setting_ids),
            U.toJson(opcSettingIds,ObservableArrayList.class));*/
    }

    public void savePageSettingIdsToLocal(){
        saveIdsToLocal(U.getString(R.string.page_setting_ids),pageSettingIds);

       // saveIdsToLocal(U.getString(R.string.page_setting_ids),pageSettingIds);
       /* SPUtils.getInstance(U.getString(R.string.setting)).put(U.getString(R.string.page_setting_ids),
            U.toJson(pageSettingIds,ObservableArrayList.class));*/
    }

    public void saveIdsToLocal(String key,List<Long> ids){

        Type type = new TypeToken<List<Long>>(){}.getType();
        SPUtils.getInstance(U.getString(R.string.setting)).put(key,
            U.toJson(ids,type));
       // Log.e( "saveIdsToLocal: ", String.valueOf(ids.get(0)));
    }

    public void savePageSettingToLocal(PageSetting item){
       // Log.e("savePageSettingToLocalId: ", String.valueOf(item.getId()));
        SPUtils.getInstance(U.getString(R.string.setting)).put(String.valueOf(item.getId()),
            U.toJson(item,PageSetting.class));
    }

    public void saveOpcSettingToLocal(OPCSetting item){
       // Log.e("saveOpcSettingToLocalid: ", String.valueOf(item.getId()));
        SPUtils.getInstance(U.getString(R.string.setting)).put(String.valueOf(item.getId()),
            U.toJson(item,OPCSetting.class));
    }

    public void deleteOpcSetting(OPCSetting item){
        /*if ( opcSettings.getValue()!=null){
            opcSettings.getValue().remove(item);
        }*/

        opcSettings.remove(item);
        opcSettingIds.remove(item.getId());
     /*   if (opcSettingIds.getValue()!=null){
            opcSettingIds.getValue().remove(item.getId());
        }*/

        //opcSettingIds.remove(item.getId());
        deleteOpcSettingFromLocal(item);
        saveOpcSettingIdsToLocal();

    }
    public void deletePageSetting(PageSetting item){
        pageSettings.remove(item);
       /* if (pageSettings.getValue()!=null){
            pageSettings.getValue().remove(item);
        }*/
        pageSettingIds.remove(item.getId());
       /* if ( pageSettingIds.getValue()!=null){
            pageSettingIds.getValue().remove(item.getId());
        }*/

        deletePageSettingFromLocal(item);

      //  pageSettingIds.remove(item.getId());
        savePageSettingIdsToLocal();

    }

    public void exportAll(){
        //todo export all setting to sdcard
        //todo 1.export setting ids(page and opc) export settings instances

        //todo 2.find page file by ids copy file to sdcard
    }

    public void importAll(){
        //todo import all setting from sdcard
        //todo 1.import ids import settings instances
        //todo 2.import page file form sdcard
    }

    private void deleteOpcSettingFromLocal(OPCSetting item){
        SPUtils.getInstance(U.getString(R.string.setting)).remove(String.valueOf(item.getId()));
    }

    private void deletePageSettingFromLocal(PageSetting item){
        SPUtils.getInstance(U.getString(R.string.setting)).remove(String.valueOf(item.getId()));
    }

    public MutableLiveData<OPCNode> getCopyNode() {
        if (copyNode == null){
            copyNode = new MutableLiveData<>();
        }
        return copyNode;
    }
}
