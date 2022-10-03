package com.piggebank.paybackPackage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int nrOfTabs;

    public PagerAdapter(FragmentManager fm, int numberOfTabs){
        super(fm);
        this.nrOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                tab3 tab3 = new tab3();
                return tab3;

            case 1:
                tab2 tab2 = new tab2();
                return tab2;

            case 2:
                tab1 tab1 = new tab1();
                return tab1;

            case 3:
                tab4 tab4 = new tab4();
                return tab4;

            default:
            return null;
        }
    }

    @Override
    public int getCount() {
        return nrOfTabs;
    }
}
