package com.example.vishistvarugeese.dynamic_data_transfer;

/**
 * Created by Vishist Varugeese on 11-01-2018.
 */

public class ListItem_RecyclerView_User {

    private String txtUserName;
    private String txtUserCpf;
    private String txtAccountType;
    private String txtCreatedDate;
    private String txtPhoneNumber;

    public ListItem_RecyclerView_User(String txtUserName, String txtUserCpf, String txtAccountType, String txtPhoneNumber, String txtCreatedDate) {
        this.txtUserName = txtUserName;
        this.txtUserCpf = txtUserCpf;
        this.txtAccountType = txtAccountType;
        this.txtCreatedDate = txtCreatedDate;
        this.txtPhoneNumber = txtPhoneNumber;
    }

    public String getTxtUserName() {
        return txtUserName;
    }

    public String getTxtUserCpf() {
        return txtUserCpf;
    }

    public String getTxtAccountType() {
        return txtAccountType;
    }

    public String getTxtCreatedDate() {
        return txtCreatedDate;
    }

    public String getTxtPhoneNumber() {
        return txtPhoneNumber;
    }
}
