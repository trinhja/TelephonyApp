package com.afinal.trinh.finalproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class Messages extends Fragment {
    private HashMap<String, List<String>> numMessageMap;
    private boolean myIsVisibleToUser;
    private ListView listMessages;
    List<String> numberList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.messages_fragment,container,false);
        listMessages = (ListView) view.findViewById(R.id.chats_list);


        listMessages.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {


                ((MainActivity)getActivity()).setNumber(numberList.get(i));
                ((MainActivity)getActivity()).selectTab(1);

            }
        });


        return view;
    }

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

                if (((MainActivity) getActivity()).getNumMessageMap() != null) {
                    numMessageMap = ((MainActivity) getActivity()).getNumMessageMap();

                    numberList = new ArrayList<String>();
                    if (numMessageMap != null) {
                        for (String key : numMessageMap.keySet()) {
                            numberList.add(key);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, numberList);
                        listMessages.setAdapter(adapter);
                    }
                }

                if (numMessageMap == null || numMessageMap.isEmpty()) {

                    Toast.makeText(getActivity(), "You have no messages. :(", Toast.LENGTH_LONG).show();
                }
            }
        }
    }



}
