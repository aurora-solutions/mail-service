package com.aurora.mail.repository;

import com.aurora.mail.beans.CustomerAccessBean;
import org.springframework.stereotype.Repository;

/**
 * Created by Rasheed on 9/14/14.
 */
@Repository
public class CustomerRepositoryStub implements CustomerRepository {

    @Override
    public CustomerAccessBean findById(Long customerId) {
        return null;
    }
}
