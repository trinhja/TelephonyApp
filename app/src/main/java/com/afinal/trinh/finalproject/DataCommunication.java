package com.afinal.trinh.finalproject;

import java.util.HashMap;
import java.util.List;

public interface DataCommunication {

    public void setNumMessageMap(HashMap<String, List<String>> numMessageMap);

    public HashMap<String, List<String>> getNumMessageMap();

    public void setNumber(String number);

    public String getNumber();

}
