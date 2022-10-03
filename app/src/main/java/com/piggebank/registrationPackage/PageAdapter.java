package com.piggebank.registrationPackage;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;


public class PageAdapter extends FragmentPagerAdapter {

    static final SparseArray<Fragment> registeredFragments = new SparseArray();
    private Context context;
    int numberOfPages = 4;

    public PageAdapter(FragmentManager fm, Context context){

        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position){
        switch(position) {
            case 0:
                return InformationFragment.newInstance();

            case 1:
                return IncomeFragment.newInstance();
            case 2:
                return GoalFragment.newInstance();
            case 3:
                return ProfilePictureFragment.newInstance();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return getNumberOfFragments();
    }

    public int getNumberOfFragments(){

        return numberOfPages;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container,
                position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }




}
