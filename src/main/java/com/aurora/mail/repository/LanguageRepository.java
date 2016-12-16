package com.aurora.mail.repository;

import org.springframework.stereotype.Repository;

/**
 * Created by Rasheed on 9/14/14.
 */
@Repository
public interface LanguageRepository {

    String findLanguageCodeById(Long languageId);
}
