package com.aurora.mail.repository;

import com.aurora.mail.beans.ReceiptBean;
import org.springframework.stereotype.Repository;

/**
 * Created by Aamir on 17/09/2014.
 */
@Repository
public interface ReceiptRepository {
    ReceiptBean findById(Long receiptId);
}
