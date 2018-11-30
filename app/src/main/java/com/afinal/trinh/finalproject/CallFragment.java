package com.afinal.trinh.finalproject;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import static android.content.Context.TELEPHONY_SERVICE;


public class CallFragment extends Fragment {
    public static final String TAG = MainActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    private TelephonyManager mTelephonyManager;


    private ImageButton callButton;
    private EditText editText;
    private Button retryButton;
    private EditText smsEditText;
    private ImageButton smsButton;
 //   private static final String TAG = "Tab1Fragment";

//    private Button btnTEST

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.call_fragment,container,false);

        callButton = (ImageButton) view.findViewById(R.id.phone_icon);
        editText = (EditText) view.findViewById(R.id.editText_main);
        retryButton = (Button) view.findViewById(R.id.button_retry);
        smsEditText = (EditText) view.findViewById(R.id.sms_message);
        smsButton = (ImageButton) view.findViewById(R.id.message_icon);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callNumber(view);
            }
        });

        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smsSendMessage(view);
            }
        });

        // Create a telephony manager.
        mTelephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        checkForSmsPermission();
        if (isTelephonyEnabled()) {
            Log.d(TAG, "Telephony is enabled");
            // ToDo: Check for phone permission.
            checkForPhonePermission();
            // ToDo: Register the PhoneStateListener.
        } else {
            Toast.makeText(getActivity(),
                    "TELEPHONY NOT ENABLED! ",
                    Toast.LENGTH_LONG).show();
            Log.d(TAG, "TELEPHONY NOT ENABLED! ");
            // Disable the call button
            disableCallButton();
        }

        return view;
    }
/*
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //btnTEST = (Button) view.findViewById(R.id.btnTEST);

        /*btnTEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "TESTING BUTTON CLICK 1",Toast.LENGTH_SHORT).show();
            }
        });*/

    //}

    private void checkForPhonePermission() {
        if (getActivity().checkSelfPermission(
                Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "PERMISSION NOT GRANTED!");
            this.requestPermissions(
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            // Permission already granted. Enable the call button.
            enableCallButton();
        }
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

    private void disableCallButton() {
        Toast.makeText(getActivity(),
                "Phone calling disabled", Toast.LENGTH_LONG).show();
        //ImageButton callButton = (ImageButton) view.findViewById(R.id.phone_icon);
        callButton.setVisibility(View.INVISIBLE);
        if (isTelephonyEnabled()) {
            //Button retryButton = (Button) view.findViewById(R.id.button_retry);
            retryButton.setVisibility(View.VISIBLE);
        }
    }

    private void enableCallButton() {
        //ImageButton callButton = (ImageButton) view.findViewById(R.id.phone_icon);
        callButton.setVisibility(View.VISIBLE);
    }

    public void retryApp(View view) {
        enableCallButton();
        Intent intent = getActivity().getPackageManager()
                .getLaunchIntentForPackage(getActivity().getPackageName());
        startActivity(intent);
    }
/*
    public void dialNumber(View view) {
        TextView textView = (TextView) findViewById(R.id.number_to_call);
        // Use format with "tel:" and phone number to create phoneNumber.
        String phoneNumber = String.format("tel: %s",
                textView.getText().toString());
        // Create the intent.
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        // Set the data for the intent as the phone number.
        dialIntent.setData(Uri.parse(phoneNumber));
        // If package resolves to an app, send intent.
        if (dialIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(dialIntent);
        } else {
            Log.e(TAG, "Can't resolve app for ACTION_DIAL Intent.");
        }
    }*/

    public void smsSendMessage(View view) {
        //EditText editText = (EditText) findViewById(R.id.number_to_call);
        // Set the destination phone number to the string in editText.
        String destinationAddress = editText.getText().toString();
        // Find the sms_message view.
        //EditText smsEditText = (EditText) findViewById(R.id.sms_message);
        // Get the text of the SMS message.
        String smsMessage = smsEditText.getText().toString();
        // Set the service center address if needed, otherwise null.
        String scAddress = null;
        // Set pending intents to broadcast
        // when message sent and when delivered, or set to null.
        PendingIntent sentIntent = null, deliveryIntent = null;
        // Use SmsManager.
        checkForSmsPermission();
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage
                (destinationAddress, scAddress, smsMessage,
                        sentIntent, deliveryIntent);
    }

    private void enableSmsButton() {
        //ImageButton smsButton = (ImageButton) findViewById(R.id.message_icon);
        smsButton.setVisibility(View.VISIBLE);
    }

    public void callNumber(View view) {
        //EditText editText = (EditText) view.findViewById(R.id.editText_main);
        // Use format with "tel:" and phone number to create phoneNumber.
        String phoneNumber = String.format("tel: %s",
                editText.getText().toString());
        // Log the concatenated phone number for dialing.
        Log.d(TAG, getString(R.string.dial_number) + phoneNumber);
        Toast.makeText(getActivity(),
                getString(R.string.dial_number) + phoneNumber,
                Toast.LENGTH_LONG).show();
        // Create the intent.
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        // Set the data for the intent as the phone number.
        callIntent.setData(Uri.parse(phoneNumber));
        // If package resolves to an app, send intent.
        if (callIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            checkForPhonePermission();
            startActivity(callIntent);
        } else {
            Log.e(TAG, "Can't resolve app for ACTION_CALL Intent.");
        }
    }

    private void checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, getString(R.string.permission_not_granted));
            // Permission not yet granted. Use requestPermissions().
            // MY_PERMISSIONS_REQUEST_SEND_SMS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            // Permission already granted. Enable the SMS button.
            enableSmsButton();
        }
    }

    private void disableSmsButton() {
        Toast.makeText(getActivity(), "SMS usage disabled", Toast.LENGTH_LONG).show();
        //ImageButton smsButton = (ImageButton) findViewById(R.id.message_icon);
        smsButton.setVisibility(View.INVISIBLE);
        //Button retryButton = (Button) findViewById(R.id.button_retry);
        retryButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {


        // Check if permission is granted or not for the request.
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (permissions[0].equalsIgnoreCase
                        (Manifest.permission.CALL_PHONE)
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                } else {
                    // Permission denied.
                    Log.d(TAG, "Failure to obtain permission!");
                    Toast.makeText(getActivity(),
                            "Failure to obtain permission!",
                            Toast.LENGTH_LONG).show();
                    // Disable the call button
                    disableCallButton();
                }
            }

        }

        /*
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (permissions[0].equalsIgnoreCase
                        (Manifest.permission.SEND_SMS)
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                    enableSmsButton();
                } else {
                    // Permission denied.
                    Log.d(TAG, "Failure to obtain permission!");
                    Toast.makeText(this,
                            "Failure to obtain permission!",
                            Toast.LENGTH_LONG).show();
                    // Disable the call button
                    disableSmsButton();
                }
            }

        }
         */
    }
}
