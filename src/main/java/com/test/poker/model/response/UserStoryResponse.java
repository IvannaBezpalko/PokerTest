package com.test.poker.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public record UserStoryResponse(long id, String inputId, String description, long sessionId, String status, int emittedVotes,
                                List<VoteResponse> votes) {
}
