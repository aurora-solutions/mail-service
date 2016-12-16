package com.aurora.mail.repository;

import com.aurora.mail.beans.CustomerOrder;
import org.springframework.stereotype.Repository;

/**
 * Created by Rasheed on 9/13/14.
 */
@Repository
public interface OrderRepository {

    CustomerOrder findById(Long orderId);
}
