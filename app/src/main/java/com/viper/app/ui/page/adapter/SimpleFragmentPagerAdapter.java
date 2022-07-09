package com.viper.app.ui.page.adapter;

import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.viper.app.ui.page.HomeFragment;
import com.viper.base.ui.page.BaseFragment;

/**
 * viewpager2 fragment 适配器 未使用
 */
public class SimpleFragmentPagerAdapter extends FragmentStateAdapter {

    private final SparseArray<BaseFragment> fragments = new SparseArray<>();
    private final int size=4;

    public SimpleFragmentPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public SimpleFragmentPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public SimpleFragmentPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = fragments.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                 //   fragment = new BaseTreeFragment(U.getFragmentTitle().get(0));
                    break;
                case 2:
                //    fragment = new BaseTreeFragment(U.getFragmentTitle().get(1));
                    break;
                case 3:
                 //   fragment = new BaseTreeFragment(U.getFragmentTitle().get(2));
                    break;
                default:
                   // fragment = new SearchFragment();
                    break;
            }
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return size;
    }
}
