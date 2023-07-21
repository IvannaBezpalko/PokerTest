package com.test.poker.model.response;

import com.test.poker.model.DeckType;
import lombok.Builder;

import java.util.List;

@Builder
public record SessionResponse(
        long id,
        String title,
        String uuid,
        DeckType deckType,
        List<UserStoryResponse> userStories,
        List<MemberResponse> members,
        String inviteLink) {
}
