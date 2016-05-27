package com.example.aromano.adm_proj_gestorescolar;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import java.util.ArrayList;

public class ContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new ClassScheduleFragment());
        fragments.add(new ExamScheduleFragment());
        fragments.add(new GradesFragment());
        String[] tabTitles = new String[]{"Classes", "Exams", "Grades"};

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new ContentPagerAdapter(getSupportFragmentManager(), ContentActivity.this, fragments, tabTitles));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }



}
