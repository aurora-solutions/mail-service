package com.aurora.mail.repository;

import org.springframework.stereotype.Repository;

/**
 * Created by rasheed on 9/14/14.
 */
@Repository
public class LanguageRepositoryStub implements LanguageRepository {
    @Override
    public String findLanguageCodeById(Long languageId) {
        return null;
    }
}
