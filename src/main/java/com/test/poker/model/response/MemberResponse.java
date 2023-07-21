package com.test.poker.model.response;

import lombok.Builder;

@Builder
public record MemberResponse(long id, String nickName) {
}
