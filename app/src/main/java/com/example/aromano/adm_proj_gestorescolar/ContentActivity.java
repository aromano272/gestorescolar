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
    private Aluno ALUNO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        // TODO get aluno from intent
        if(getIntent().getParcelableExtra("aluno") != null) {
            ALUNO = getIntent().getParcelableExtra("aluno");
        } else {
            finish();
        }


        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(ClassScheduleFragment.newInstance(ALUNO));
        fragments.add(ExamScheduleFragment.newInstance(ALUNO));
        fragments.add(GradesFragment.newInstance(ALUNO));
        String[] tabTitles = new String[]{"Classes", "Exams", "Grades"};

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new ContentPagerAdapter(getSupportFragmentManager(), ContentActivity.this, fragments, tabTitles));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }



}
