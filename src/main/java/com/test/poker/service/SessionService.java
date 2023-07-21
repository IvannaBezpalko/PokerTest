package com.test.poker.service;

import com.test.poker.model.response.SessionResponse;

public interface SessionService {

    SessionResponse create(String title, String deckType);

    SessionResponse getSession(String uuid);

    SessionResponse enter(String name, String uuid);

    void delete(long id);
}
