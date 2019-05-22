package com.casc.pgkg.bean;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("user_id")
    private int userId;

    @SerializedName("role_id")
    private int roleId;

    public int getUserId() {
        return userId;
    }

    public int getRoleId() {
        return roleId;
    }
}
