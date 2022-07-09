package com.viper.app.domain.message;

import androidx.lifecycle.ViewModel;

import com.viper.base.livedata.ProtectedUnPeekLiveData;
import com.viper.base.livedata.UnPeekLiveData;

public class SharedViewModel extends ViewModel {

    //TODO tip 2：此处演示通过 UnPeekLiveData 配合 SharedViewModel 来发送 生命周期安全的、
    // 确保消息同步一致性和可靠性的 "跨页面" 通知。

    //TODO tip 3：并且，在 "页面通信" 的场景下，使用全局 ViewModel，是因为它被封装在 base 页面中，
    // 避免页面之外的组件拿到，从而造成不可预期的推送。
    // 而且尽可能使用单例或 ViewModel 托管 liveData，这样能利用好 LiveData "读写分离" 的特性
    // 来实现 "唯一可信源" 单向数据流的决策和分发，从而避免只读数据被篡改 导致的其他页面拿到脏数据。

    // 如果这么说还不理解的话，
    // 详见 https://xiaozhuanlan.com/topic/0168753249 和 https://xiaozhuanlan.com/topic/6257931840

    private final UnPeekLiveData<Boolean> toCloseSlidePanelIfExpanded = new UnPeekLiveData<>();


    private final UnPeekLiveData<Boolean> toCloseActivityIfAllowed = new UnPeekLiveData<>();

    private final UnPeekLiveData<Boolean> toOpenOrCloseDrawer = new UnPeekLiveData<>();
    private final UnPeekLiveData<Boolean> toOpenOrCloseEndDrawer = new UnPeekLiveData<>();

    //TODO tip 4：可以通过构造器的方式来配置 UnPeekLiveData

    // 具体存在有缘和使用方式可详见《LiveData 数据倒灌 背景缘由全貌 独家解析》
    // https://xiaozhuanlan.com/topic/6719328450

    private final UnPeekLiveData<Boolean> toAddSlideListener =
        new UnPeekLiveData.Builder<Boolean>().setAllowNullValue(false).create();

    public ProtectedUnPeekLiveData<Boolean> isToAddSlideListener() {
        return toAddSlideListener;
    }

    public ProtectedUnPeekLiveData<Boolean> isToCloseSlidePanelIfExpanded() {
        return toCloseSlidePanelIfExpanded;
    }

    public ProtectedUnPeekLiveData<Boolean> isToCloseActivityIfAllowed() {
        return toCloseActivityIfAllowed;
    }

    public ProtectedUnPeekLiveData<Boolean> isToOpenOrCloseDrawer() {
        return toOpenOrCloseDrawer;
    }

    public ProtectedUnPeekLiveData<Boolean> isToOpenOrCloseEndDrawer() {
        return toOpenOrCloseEndDrawer;
    }



    public void requestToCloseActivityIfAllowed(boolean allow) {
        toCloseActivityIfAllowed.setValue(allow);
    }



    public void requestToOpenOrCloseDrawer(boolean open) {
        toOpenOrCloseDrawer.setValue(open);
    }

    public void requestToOpenOrCloseEndDrawer(boolean open) {
        toOpenOrCloseEndDrawer.setValue(open);
    }

    public void requestToCloseSlidePanelIfExpanded(boolean close) {
        toCloseSlidePanelIfExpanded.setValue(close);
    }

    public void requestToAddSlideListener(boolean add) {
        toAddSlideListener.setValue(add);
    }
}
