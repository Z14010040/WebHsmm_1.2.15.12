package com.sansec.hsm.bean.login;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class UserTrace implements HttpSessionBindingListener{
	private String username;
    private UserList containter = UserList.getInstance();

    public String getUsername() {
		return username;
	}
	public void setUsername(String ip) {
		this.username = ip;
	}

    //当UserTrace被加入session对象时会调用此方法
    public void valueBound(HttpSessionBindingEvent event) {
        
    }

    
    //当UserTrace被移除session对象时会调用此方法
    public void valueUnbound(HttpSessionBindingEvent event) {
        containter.removeUser(username);
    }
    
    public void removeIP(String ip){
        containter.removeUser(ip);
    }
}
