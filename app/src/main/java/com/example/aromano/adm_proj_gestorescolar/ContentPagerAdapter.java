package com.example.aromano.adm_proj_gestorescolar;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by aRomano on 24/05/2016.
 */
public class ContentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[];
    private Context context;
    private ArrayList<Fragment> fragments;

    public ContentPagerAdapter(FragmentManager fm, Context context, ArrayList<Fragment> fragments, String[] tabTitles) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
        this.tabTitles = tabTitles;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    // TODO make android keep data on fragment change, maybe with onsavedinstance
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
