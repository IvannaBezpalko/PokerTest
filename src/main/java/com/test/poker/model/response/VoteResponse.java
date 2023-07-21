package com.test.poker.model.response;

import lombok.Builder;

@Builder
public record VoteResponse (long id, long userStoryId, long memberId, String option){
}
