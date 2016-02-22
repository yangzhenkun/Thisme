package com.example.yasin.thisme.model;

/**
 * Created by Yasin on 2016/2/21.
 */
public class User {
    private boolean isOnline = false;
    private String id;
    static private User user;

    private User(){

    }

    public synchronized static User getInsstance(){
        if(user == null){
            user = new User();
        }
        return user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }
}
