package com.qrms.Pozo;

/**
 * Created by mspl-01 on 08/02/2020.
 */

public class User {

    int Id;
    int UserId;
    String Name;

    public int getId() {
        return Id;
    }

    public int getUserId() {
        return UserId;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public String getUserName() {
        return UserName;
    }

    public String getPassword() {
        return Password;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    String Email;
    String MobileNo;
    String UserName;
    String Password;
    String Designation;
}
