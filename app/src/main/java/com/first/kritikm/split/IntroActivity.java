package com.first.kritikm.split;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IntroActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button skip, next;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getSharedPreferences("com.first.kritikm.split_tabbed", MODE_PRIVATE);
        if(!settings.getBoolean("firstRun", true))
        {
            String username = SaveSharedPreference.getUsername(getApplicationContext());
            if(username.length() != 0)
            {
                Intent send_username = new Intent(this, HomeActivity.class);
                send_username.putExtra(MainActivity.extra_message, username);
                startActivity(send_username);
                finish();
            }
            else {
                Intent mainAct = new Intent(this, MainActivity.class);
                startActivity(mainAct);
                finish();
            }
        }

//        if(Build.VERSION.SDK_INT >= 21)
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_intro);

        viewPager = (ViewPager)findViewById(R.id.intro_pager);
        dotsLayout = (LinearLayout)findViewById(R.id.dots);
        skip = (Button) findViewById(R.id.skip_button);
        next = (Button)findViewById(R.id.next_button);

        layouts = new int[]
                {
                        R.layout.food_layout,
                        R.layout.movies_layout,
                        R.layout.travels_layout,
                        R.layout.all_layout
                };

        addBottomDots(0);

        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.edit().putBoolean("firstRun", false).apply();
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(1);
                if(current < layouts.length)
                    viewPager.setCurrentItem(current);
                else
                {
                    settings.edit().putBoolean("firstRun", false).apply();
                    startActivity(new Intent(IntroActivity.this, MainActivity.class));
                    finish();
                }
            }
        });

    }

    private void addBottomDots(int position)
    {
        int colorActive, colorInactive;
        dots = new TextView[layouts.length];

        if(Build.VERSION.SDK_INT < 23) {
            colorActive = getResources().getColor(R.color.colorPrimary);
            colorInactive = getResources().getColor(R.color.colorPrimaryDark);
        }
        else {
            colorActive = ContextCompat.getColor(this, R.color.colorPrimary);
            colorInactive = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        }
        dotsLayout.removeAllViews();
        for(int i = 0; i < dots.length; i++)
        {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInactive);
            dotsLayout.addView(dots[i]);
        }
        if(dots.length > 0)
            dots[position].setTextColor(colorActive);

    }

    private int getItem(int i)
    {
        return viewPager.getCurrentItem() + i;
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            if(position == layouts.length - 1) {
                next.setText(getString(R.string.start));
                skip.setVisibility(View.GONE);
            }
            else
            {
                next.setText(getString(R.string.next));
                skip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public class ViewPagerAdapter extends PagerAdapter
    {
        private LayoutInflater layoutInflater;

        public ViewPagerAdapter() {}

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount()
        {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
