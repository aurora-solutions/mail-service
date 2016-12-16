package com.aurora.mail.repository;

import com.aurora.mail.beans.UserBean;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by rasheed on 9/14/14.
 */
@Repository
public class UserRepositoryStub implements UserRepository {
    @Override
    public UserBean findById(Integer userId) {
        return null;
    }

    @Override
    public Set<UserBean> findByPowerLevel(Integer powerLevel) {
        return null;
    }
}
