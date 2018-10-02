package com.example.vishistvarugeese.dynamic_data_transfer;

/**
 * Created by Vishist Varugeese on 19-12-2017.
 */

public class ListItem_RecyclerView_Admin {

    private String recentLoginTime;
    private String recentLoginName;
    private String recentLoginCpf;
    private String recentLoginType;

    public ListItem_RecyclerView_Admin(String recentLoginTime, String recentLoginName, String recentLoginCpf, String recentLoginType) {
        this.recentLoginTime = recentLoginTime;
        this.recentLoginName = recentLoginName;
        this.recentLoginCpf = recentLoginCpf;
        this.recentLoginType = recentLoginType;
    }

    public String getRecentLoginTime() { return recentLoginTime; }

    public String getRecentLoginName() {
        return recentLoginName;
    }

    public String getRecentLoginCpf() {
        return recentLoginCpf;
    }

    public String getRecentLoginType() {
        return recentLoginType;
    }
}
