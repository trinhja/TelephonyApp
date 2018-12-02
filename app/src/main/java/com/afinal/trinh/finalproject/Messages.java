package com.afinal.trinh.finalproject;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class Messages extends Fragment {
    private HashMap<String, List<String>> numMessageMap;
    private List<String> numberList = new ArrayList<>();
    private boolean myIsVisibleToUser;

    private ListView listMessage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        listMessage = (ListView) getActivity().findViewById(R.id.messages_list);

        // Inflate the layout for this fragment
//        if (getArguments() != null) {
//            numMessageMap = (HashMap<String, List<String>>) getArguments().getSerializable("numMessageMap");
//
//            for (String key : numMessageMap.keySet()) {
//                numberList.add(key);
//            }
//
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, numberList);
//
//            listMessage.setAdapter(adapter);
//        } else {
//            Toast.makeText(getActivity(), "Sorry, you have no messages!", Toast.LENGTH_LONG).show();
//        }

        return inflater.inflate(R.layout.messages_fragment, container, false);
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
                if (getArguments() != null) {
                    numMessageMap = (HashMap<String, List<String>>) getArguments().getSerializable("numMessageMap");

                    for (String key : numMessageMap.keySet()) {
                        numberList.add(key);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, numberList);

                    listMessage.setAdapter(adapter);
                } else {
//                numberList.add("Sorry, you have no messages!");
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, numberList);
//                listMessage.setAdapter(adapter);
                    Toast.makeText(getActivity(), "Sorry, you have no messages!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
