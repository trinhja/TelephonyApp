package com.afinal.trinh.finalproject;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import static android.content.Context.TELEPHONY_SERVICE;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private PageAdapter pageAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Starting.");

        pageAdapter = new PageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        Intent intent = getIntent();

        HashMap<String, List<String>> numMessageMap = (HashMap<String, List<String>>)intent.getSerializableExtra("numMessageMap");
        Bundle bundle = new Bundle();
        bundle.putSerializable("numMessageMap", numMessageMap);
        //set Fragmentclass Arguments
        Messages messages = new Messages();
        messages.setArguments(bundle);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }




    private void setupViewPager(ViewPager viewPager) {
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Messages(), "Messages");
        adapter.addFragment(new CallFragment(), "Messaging");
        adapter.addFragment(new ContactFragment(), "Contacts");
        //adapter.addFragment(new Tab3Fragment(), "TAB3");
        viewPager.setAdapter(adapter);
    }
}
