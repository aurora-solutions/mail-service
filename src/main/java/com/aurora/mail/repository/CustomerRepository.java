package com.aurora.mail.repository;

import com.aurora.mail.beans.CustomerAccessBean;
import org.springframework.stereotype.Repository;

/**
 * Created by Rasheed on 9/13/14.
 */
@Repository
public interface CustomerRepository {

    CustomerAccessBean findById(Long customerId);
}
