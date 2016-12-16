package com.aurora.mail.util;

import com.aurora.mail.beans.UserBean;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Aamir on 15/09/2014.
 */
public class UserBeanStub {

    public static final String CUSTOM_MESSAGE="Hi, Sir.";
    public static final String CUSTOM_SUBJECT="email.activation.title";


    public static final Integer USER_ID=56;
    public static final String USER_FIRST_NAME="Aamir";
    public static final boolean USER_ACTIVE=true;
    public static final String USER_A_EMAIL = "aamir100711@gmail.com";
    public static final Integer USER_POWER_LEVEL = 6;

    public static final Integer USER_ID_2=67;
    public static final String USER_FIRST_NAME_2="Aziz";
    public static final boolean USER_ACTIVE_2=true;
    public static final String USER_A_EMAIL_2 = "aamir100711@gmail.com";
    public static final Integer USER_POWER_LEVEL_2 = 6;

    public static UserBean createUserBean() {
        UserBean userBean=new UserBean();
        userBean.setId(USER_ID);
        userBean.setFirstName(USER_FIRST_NAME);
        userBean.setActive(USER_ACTIVE);
        userBean.setEmail(USER_A_EMAIL);
        userBean.setPowerlevel(USER_POWER_LEVEL);
        return userBean;
    }

    public static Set<UserBean> createUserBeans(){
        Set<UserBean> userBeans	= new HashSet<UserBean>(0);
        UserBean userBean=new UserBean();
        userBean.setId(USER_ID_2);
        userBean.setFirstName(USER_FIRST_NAME_2);
        userBean.setActive(USER_ACTIVE_2);
        userBean.setEmail(USER_A_EMAIL_2);
        userBean.setPowerlevel(USER_POWER_LEVEL);
        UserBean userBean1=new UserBean();
        userBean1.setId(USER_ID);
        userBean1.setFirstName(USER_FIRST_NAME);
        userBean1.setActive(USER_ACTIVE);
        userBean1.setEmail(USER_A_EMAIL);
        userBean1.setPowerlevel(USER_POWER_LEVEL);
        userBeans.add(userBean);
        userBeans.add(userBean1);
        return userBeans;
    }

}
