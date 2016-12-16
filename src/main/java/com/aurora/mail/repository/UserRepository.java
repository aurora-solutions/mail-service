package com.aurora.mail.repository;

import com.aurora.mail.beans.UserBean;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by Rasheed on 9/14/14.
 */
@Repository
public interface UserRepository {

    UserBean findById(Integer userId);

    Set<UserBean> findByPowerLevel(Integer powerLevel);
}
