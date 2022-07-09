package com.viper.app.ui.base.binding_adapter;

import android.annotation.SuppressLint;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.viper.app.MainActivity;
import com.viper.app.R;
import com.viper.app.data.bean.OPCNode;

import java.util.List;

public class MyViewBinding {

    public MyViewBinding() {
    }

    @SuppressLint("NonConstantResourceId")
    @BindingAdapter(
        value = {"initPagerAndBottom"},
        requireAll = false
    )
    public static void setAdapter(ViewPager2 viewPager2, FragmentStateAdapter adapter) {
        /*viewPager2.setAdapter(adapter);
        BottomNavigationView bottomNavigationView = viewPager2.getRootView().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navigation_home:viewPager2.setCurrentItem(0);break;
                case R.id.navigation_opc1:viewPager2.setCurrentItem(1);break;
                case R.id.navigation_opc2:viewPager2.setCurrentItem(2);break;
                case R.id.navigation_opc3:viewPager2.setCurrentItem(3);break;
            }
            return true;
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:bottomNavigationView.setSelectedItemId(R.id.navigation_home);break;
                    case 1:bottomNavigationView.setSelectedItemId(R.id.navigation_opc1);break;
                    case 2:bottomNavigationView.setSelectedItemId(R.id.navigation_opc2);break;
                    case 3:bottomNavigationView.setSelectedItemId(R.id.navigation_opc3);break;
                }

            }
        });*/
    }

    @BindingAdapter(value = {"bottomNavInit"}, requireAll = false)
    public static void bottomNavInit(BottomNavigationView view, MainActivity activity){

        NavigationUI.setupWithNavController(view, Navigation.findNavController(activity, R.id.main_fragment_host));
    }







    @SuppressWarnings("deprecation")
    @BindingAdapter(value = {"scrollChangeListener"}, requireAll = false)
    public static void scrollChangeListener(RecyclerView recyclerView, List<OPCNode>  inShowList){

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                // if (newState == RecyclerView.SCROLL_STATE_IDLE){

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (manager != null){
                    //int lastCompletelyVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition();
                    addItemOnScroll(inShowList,manager.findLastVisibleItemPosition(),20);
                }
            }

        });
    }


    public static void addItemOnScroll(List<OPCNode>  inShowList, int lastItemPosition, int addSize){
        if (inShowList.isEmpty() || lastItemPosition < 1) return;
        try {
            OPCNode lastItem = inShowList.get(lastItemPosition);
            OPCNode parent = lastItem.getParent();

            if (parent!=null){
                List<OPCNode> broList = parent.getChildList();
                int sizeOfBro = broList.size();
                int lastItemPositionOfParent = broList.indexOf(lastItem);
                if (lastItemPositionOfParent<sizeOfBro){
                    for (int i=lastItemPositionOfParent;i<sizeOfBro;i++){
                        if (!inShowList.contains(broList.get(i))){
                            if (i-lastItemPositionOfParent<15){
                                //todo load more
                                int preItemPosition = inShowList.indexOf(broList.get(i-1));
                                inShowList.add(preItemPosition+1,broList.get(i));
                            }
                        }

                    }
                }

                // addChildItem(vm.plcNodes,lastItemPositonNode.getParent(),lastItemPosition,10);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void addItemOnScroll(MutableLiveData<List<OPCNode>> list, int lastItemPosition, int addSize){
        List<OPCNode> inShowList = list.getValue();
        if (inShowList!=null){
            OPCNode lastItem = inShowList.get(lastItemPosition);
            OPCNode parent = lastItem.getParent();

            if (parent!=null){
                List<OPCNode> broList = parent.getChildList();
                int sizeOfBro = broList.size();
                int lastItemPositionOfParent = broList.indexOf(lastItem);
                if (lastItemPositionOfParent<sizeOfBro){
                    for (int i=lastItemPositionOfParent+1;i<sizeOfBro;i++){
                        if (!inShowList.contains(broList.get(i))){
                            if (i-lastItemPositionOfParent<10){
                                //todo load more
                                inShowList.add(i,broList.get(i));
                            }
                        }

                    }
                }

                // addChildItem(vm.plcNodes,lastItemPositonNode.getParent(),lastItemPosition,10);
            }
        }
        list.setValue(inShowList);


    }


    public static void addOrRemoveItem(List<OPCNode> showList, OPCNode item, int positon, int size){
       /* if (item.isExpand()){
            addChildItem(showList,item,positon,size);
        }else {
            removeChildItem(showList,item,positon);
        }*/
    }

    public static void addOrRemoveItem(MutableLiveData<List<OPCNode>> list, OPCNode item, int positon, int size){
      /*  if (item.isExpand()){
            addChildItem(list,item,positon,size);
        }else {
            removeChildItem(list,item,positon);
        }*/
    }



    public static void addChildItem(MutableLiveData<List<OPCNode>> list, OPCNode item, int positon, int size){
        List<OPCNode> childList = item.getChildList();
        List<OPCNode> inShow = list.getValue();
        if (childList !=null && inShow!=null){
            int addChildCount = 0;
            for (OPCNode node : childList) {
                if (!inShow.contains(node)){
                    addChildCount +=1;
                    //node.setExpand(false);
                    inShow.add(positon+addChildCount,node);
                }

                if (addChildCount > size){
                    break;
                }
            }
            list.setValue(inShow);
        }
    }

    public static void removeChildItem(MutableLiveData<List<OPCNode>> list, OPCNode item, int positon){
        List<OPCNode> childList = item.getChildList();
        List<OPCNode> inShow = list.getValue();
        if (inShow!=null){
            for (OPCNode node : childList) {
                inShow.remove(node);
                removeChildItem(list,node,positon);
            }
        }

        list.setValue(inShow);
    }

    public static void addChildItem(List<OPCNode> showList, OPCNode item, int positon, int size){
        List<OPCNode> childList = item.getChildList();
        if (childList !=null){
            int addChildCount = 0;
            for (OPCNode node : childList) {
                if (!showList.contains(node)){
                    addChildCount +=1;
                 //   node.setExpand(false);
                    showList.add(positon+addChildCount,node);
                }
                if (addChildCount > size && size > 0){
                    return;
                }
            }
        }
    }


    public static void removeChildItem(List<OPCNode> showList, OPCNode item, int positon){
        List<OPCNode> childList = item.getChildList();
        for (OPCNode node : childList) {
            showList.remove(node);
            removeChildItem(showList,node,positon);
        }
    }

}
