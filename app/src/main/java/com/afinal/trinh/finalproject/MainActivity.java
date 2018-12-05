package com.afinal.trinh.finalproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.telecom.Call;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements DataCommunication {
    private static final String TAG = "MainActivity";

    private PageAdapter pageAdapter;
    private TelephonyManager mTelephonyManager;
    private ViewPager mViewPager;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    private HashMap<String, List<String>> numMessageMap;
    private String number;
    private String[] PERMISSIONS = {
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CONTACTS
    };

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
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_message_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_list_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_contacts_black_24dp);


        // Create a telephony manager.
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        checkForPhonePermission(this, PERMISSIONS);
//        if (isTelephonyEnabled()) {
//            Log.d(TAG, "Telephony is enabled");
//            checkForPhonePermission();
//        } else {
//            Toast.makeText(this,
//                    "TELEPHONY NOT ENABLED! ",
//                    Toast.LENGTH_LONG).show();
//            Log.d(TAG, "TELEPHONY NOT ENABLED! ");
//            // Disable the call button
//            disableCallButton();
//        }
    }




    private boolean isTelephonyEnabled() {
        if (mTelephonyManager != null) {
            if (mTelephonyManager.getSimState() ==
                    TelephonyManager.SIM_STATE_READY) {
                return true;
            }
        }
        return false;
    }



    public void checkForPhonePermission(Context context, String... permissions)  {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context,
                        permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "PERMISSION NOT GRANTED!");
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], int[] grantResults) {
        // Check if permission is granted or not for the request.
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (permissions.length > 0 && grantResults.length > 0) {
                    if (permissions[0].equalsIgnoreCase
                            (Manifest.permission.CALL_PHONE)
                            && grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED) {
                        // Permission was granted.
                    }
                } else {
                    // Permission denied.
                    Log.d(TAG, "Failure to obtain permission!");
                    Toast.makeText(this,
                            "Failure to obtain permission!",
                            Toast.LENGTH_LONG).show();
                }
            }
            break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }



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
        adapter.addFragment(new CallFragment(), "Messaging");
        adapter.addFragment(new Messages(), "Chats");
        adapter.addFragment(new ContactFragment(), "Contacts");
        viewPager.setAdapter(adapter);
    }
}
