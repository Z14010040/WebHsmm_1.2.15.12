package com.sansec.hsm.bean.login;

import java.util.ArrayList;
import java.util.List;

public class UserList {
    private List<String> containter = new ArrayList<String>();

    //UserList算是一个容器,整个应用程序内只有一个用户列表类
    private static UserList instance = new UserList();

    //以private的方式调用构造函数,避免被外界产生新的instance
    private UserList() {
    }

    //供外界使用的instance
    public static UserList getInstance() {
        return instance;
    }

    //新增用户到用户列表
    public void addUser(String ip) {
        if (ip != null) {
            if (!checkExist(ip)) {
                containter.add(ip);
            }
        }
    }

    //列出所有在线的用户
    public String[] getAllUsers() {
        String[] users = new String[containter.size()];
        for (int i = 0; i < containter.size(); i++) {
            users[i] = containter.get(i);
        }
        return users;
    }

    //移除已离线的用户
    public void removeUser(String ip) {
        if (ip != null) {
            containter.remove(ip);
        }
    }

    public boolean checkExist(String ip) {
        return containter.contains(ip);
    }
}
