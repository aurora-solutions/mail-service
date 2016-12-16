package com.aurora.mail.repository;

import com.aurora.mail.beans.CustomerOrder;
import org.springframework.stereotype.Repository;

/**
 * Created by Rasheed on 9/14/14.
 */
@Repository
public class OrderRepositoryStub implements OrderRepository {
    @Override
    public CustomerOrder findById(Long orderId) {
        return null;
    }
}
