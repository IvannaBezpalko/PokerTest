package com.test.poker.service;

import com.test.poker.model.response.UserStoryResponse;

public interface UserStoryService {

    UserStoryResponse create(String id, String description, long sessionId);

    void delete(long id);

    void startVoting(long id);

    void voteUserStory(long id, String option, long memberId);

    void finishVoting(long userStoryId);

    UserStoryResponse getUserStory(long id);
}
