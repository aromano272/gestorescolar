package com.example.aromano.adm_proj_gestorescolar;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class ContentActivity extends AppCompatActivity {
    private Aluno aluno;
    long timer_start = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        // TODO get aluno from intent
        if(getIntent().getParcelableExtra("aluno") != null) {
            aluno = getIntent().getParcelableExtra("aluno");
        } else {
            finish();
        }


        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(ClassScheduleFragment.newInstance(aluno));
        fragments.add(EventScheduleFragment.newInstance(aluno));
        fragments.add(GradesFragment.newInstance(aluno));
        String[] tabTitles = new String[]{"Classes", "Exams", "Grades"};

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new ContentPagerAdapter(getSupportFragmentManager(), ContentActivity.this, fragments, tabTitles));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);





    }

    @Override
    public void onBackPressed() {
        int delay = 2000;
        long timer_now = System.currentTimeMillis();

        if (timer_now - timer_start < delay) {
            finish();
        } else {
            timer_start = timer_now;
            Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT).show();
        }
    }



}
