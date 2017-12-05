package com.example.demo.resp;

import java.util.ArrayList;

public class DelRespEntity {
    ArrayList<Integer> ids;
    String token;

    public ArrayList<Integer> getIds() {
        return ids;
    }

    public void setIds(ArrayList<Integer> ids) {
        this.ids = ids;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "DelRespEntity{" +
                "ids=" + ids +
                ", token='" + token + '\'' +
                '}';
    }
}
