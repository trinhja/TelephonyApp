package com.afinal.trinh.finalproject;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;


import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements DataCommunication {
    private static final String TAG = "MainActivity";

    private PageAdapter pageAdapter;

    private ViewPager mViewPager;


    private HashMap<String, List<String>> numMessageMap;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Starting.");

        pageAdapter = new PageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void setNumMessageMap(HashMap<String, List<String>> numMessageMap){
       this.numMessageMap = numMessageMap;
    }

    @Override
    public HashMap<String, List<String>> getNumMessageMap(){
        return numMessageMap;
    }

    @Override
    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String getNumber (){
        return number;
    }

    public void selectTab(int position) {
        mViewPager.setCurrentItem(position);
    }

    private void setupViewPager(ViewPager viewPager) {
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Messages(), "Chats");
        adapter.addFragment(new CallFragment(), "Messaging");
        adapter.addFragment(new ContactFragment(), "Contacts");
        viewPager.setAdapter(adapter);
    }
}
