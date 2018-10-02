package com.example.vishistvarugeese.dynamic_data_transfer;

/**
 * Created by Vishist Varugeese on 28-03-2018.
 */

public class ListItem_RecyclerView_Data_User {

    private String titleUsers;
    private String valueUsers;

    public ListItem_RecyclerView_Data_User (String titleUsers, String valueUsers){
        this.titleUsers = titleUsers;
        this.valueUsers = valueUsers;
    }

    public String getTitleUsers(){
        return titleUsers;
    }

    public String getValueUsers(){
        return valueUsers;
    }
}
