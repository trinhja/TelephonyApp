package com.afinal.trinh.finalproject;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.content.Context.TELEPHONY_SERVICE;


public class CallFragment extends Fragment {
    public static final String TAG = MainActivity.class.getSimpleName();

    String[] PERMISSIONS = {
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CONTACTS
    };


    public ImageButton callButton;
    private EditText editNumberText;
    private EditText smsEditText;
    private ImageButton smsButton;
    private ListView listMessage;
    private int MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10;
     HashMap<String, List<String>> numMessageMap = new HashMap<String, List<String>>();
    MainActivity main = new MainActivity();
    private String number;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.call_fragment,container,false);

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.RECEIVE_SMS},
                MY_PERMISSIONS_REQUEST_SMS_RECEIVE);


        callButton = (ImageButton) view.findViewById(R.id.phone_icon);
        editNumberText = (EditText) view.findViewById(R.id.editNumber);
        smsEditText = (EditText) view.findViewById(R.id.editMessage);
        smsButton = (ImageButton) view.findViewById(R.id.message_icon);
        listMessage = (ListView) view.findViewById(R.id.messages_list);


        getActivity().registerReceiver(broadcastReceiver, new IntentFilter( "broadcast"));


        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smsSendMessage(view);
            }
        });





        editNumberText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        smsEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).checkForPhonePermission(getContext(), PERMISSIONS);
                callNumber(view);
            }
        });

        return view;
    }

    DataCommunication mCallback;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (DataCommunication) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement TextClicked");
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    private BroadcastReceiver broadcastReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Bundle b = intent.getExtras();
            final String message = b.getString("message");
            final String number = b.getString("number");
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    putMessagesInMap(number, message);

                    updateMessage(number);

                }
            });
        }
    };


    public void updateMessage(String number)
    {
        List<String> messages = numMessageMap.get(number);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),  android.R.layout.simple_list_item_1, messages);

        listMessage.setAdapter(adapter);
    }

    public void hideKeyboard(View view) {
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }




    public void onDestroy() {
        super.onDestroy();

        getActivity().unregisterReceiver(broadcastReceiver);
    }

    public void smsSendMessage(View view) {

        if (editNumberText.getText().length() > 0 && smsEditText.getText().length() > 0 ) {
            // Set the destination phone number to the string in editText.
            String destinationAddress = editNumberText.getText().toString();
            // Find the sms_message view.
            // Get the text of the SMS message.
            String smsMessage = smsEditText.getText().toString();
            // Set the service center address if needed, otherwise null.
            String scAddress = null;
            // Set pending intents to broadcast
            // when message sent and when delivered, or set to null.
            PendingIntent sentIntent = null, deliveryIntent = null;
            // Use SmsManager.
            ((MainActivity)getActivity()).checkForPhonePermission(getContext(), PERMISSIONS);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage
                    (destinationAddress, scAddress, smsMessage,
                            sentIntent, deliveryIntent);

            String number = editNumberText.getText().toString();

            putMessagesInMap(number, smsMessage);


            updateMessage(number);
            smsEditText.setText("");
        } else {
            Toast.makeText(getActivity(), "Phone number & text field cannot be empty.", Toast.LENGTH_LONG).show();
        }
    }

    private void putMessagesInMap(String number, String smsMessages) {
        List<String> messages = new ArrayList<>();

        // get message from the map and update it and store back into message
        if (numMessageMap.get(number) != null) {
            messages = numMessageMap.get(number);

        }
        messages.add(smsMessages);
        numMessageMap.put(number, messages);

        ((MainActivity)getActivity()).setNumMessageMap(numMessageMap);

    }


    public void callNumber(View view) {
        if (editNumberText.getText().length() != 0) {
            // Use format with "tel:" and phone number to create phoneNumber.
            String phoneNumber = String.format("tel: %s",
                    editNumberText.getText().toString());
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
                ((MainActivity) getActivity()).checkForPhonePermission(getContext(), PERMISSIONS);
                startActivity(callIntent);
            } else {
                Log.e(TAG, "Can't resolve app for ACTION_CALL Intent.");
            }
        } else {
            ((MainActivity)getActivity()).checkForPhonePermission(getContext(), PERMISSIONS);
            Toast.makeText(getActivity(), "Phone number field cannot be empty.", Toast.LENGTH_LONG).show();
        }
    }


    private boolean myIsVisibleToUser;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUserVisibleHint(myIsVisibleToUser);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        myIsVisibleToUser=isVisibleToUser;
        if (isVisibleToUser && getActivity()!=null) {
            if (isVisibleToUser) {

                if (((MainActivity) getActivity()).getNumber() != null) {
                    number = ((MainActivity) getActivity()).getNumber();

                    editNumberText.setText(number);

                    updateMessage(number);
                }
            }
        }
    }
}
