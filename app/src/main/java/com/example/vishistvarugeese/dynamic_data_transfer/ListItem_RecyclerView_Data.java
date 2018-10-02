package com.example.vishistvarugeese.dynamic_data_transfer;

/**
 * Created by Vishist Varugeese on 27-03-2018.
 */

public class ListItem_RecyclerView_Data {

    private String title;
    private String value;

    public ListItem_RecyclerView_Data (String title, String value){
        this.title = title;
        this.value = value;
    }

    public String getTitle(){
        return title;
    }

    public String getValue(){
        return value;
    }
}
