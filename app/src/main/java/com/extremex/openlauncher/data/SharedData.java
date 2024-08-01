package com.extremex.openlauncher.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SharedData {
    private static final MutableLiveData<String> timeData = new MutableLiveData<>();

    public static void setTime(String time) {
        timeData.postValue(time);
    }

    public static LiveData<String> getTime() {
        return timeData;
    }
}
