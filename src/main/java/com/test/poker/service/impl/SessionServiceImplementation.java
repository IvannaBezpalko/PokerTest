package com.test.poker.service.impl;

import com.test.poker.entity.MemberEntity;
import com.test.poker.entity.SessionEntity;
import com.test.poker.entity.UserStoryEntity;
import com.test.poker.entity.VoteEntity;
import com.test.poker.exception.AppNotFoundException;
import com.test.poker.model.DeckType;
import com.test.poker.model.UserStoryStatus;
import com.test.poker.model.response.MemberResponse;
import com.test.poker.model.response.SessionResponse;
import com.test.poker.model.response.UserStoryResponse;
import com.test.poker.model.response.VoteResponse;
import com.test.poker.repository.SessionRepository;
import com.test.poker.service.SessionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionServiceImplementation implements SessionService {

    private final SessionRepository sessionRepository;

    @Override
    public SessionResponse create(String title, String deckType) {
        SessionEntity session = SessionEntity.builder()
                .title(title)
                .uuid(UUID.randomUUID().toString())
                .deckType(DeckType.getByUpperCaseName(deckType))
                .build();

        return fromEntity(sessionRepository.save(session));
    }

    @Override
    public SessionResponse getSession(String id) {
        SessionEntity entity = sessionRepository.findByUuid(id);
        if (entity == null) {
            throw new AppNotFoundException("Session with id " + id + " not found.");
        }
        return fromEntity(entity);
    }

    @Override
    public SessionResponse enter(String name, String uuid) {
        SessionEntity session = sessionRepository.findByUuid(uuid);
        if (session == null) {
            throw new AppNotFoundException("The session with id " + uuid + " not found");
        }
        MemberEntity member = MemberEntity.builder()
                .nickName(name)
                .build();
        session.getMembers().add(member);
        session = sessionRepository.save(session);
        return fromEntity(session);
    }

    @Override
    public void delete(long id) {
        sessionRepository.deleteById(id);
    }

    private SessionResponse fromEntity(SessionEntity entity) {
        return SessionResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .uuid(entity.getUuid())
                .deckType(entity.getDeckType())
                .members(fromEntityMembers(entity.getMembers()))
                .userStories(fromEntityUserStories(entity.getUserStories()))
                .inviteLink("/sessions/enter/" + entity.getUuid())
                .build();
    }

    private List<UserStoryResponse> fromEntityUserStories(List<UserStoryEntity> userStories) {
        if (userStories == null) return Collections.emptyList();
        return userStories.stream()
                .filter(u -> !u.isDeleted() && u.getStatus().equals(UserStoryStatus.VOTING))
                .map(u ->
                        UserStoryResponse.builder()
                                .sessionId(u.getSession().getId())
                                .description(u.getDescription())
                                .id(u.getId())
                                .inputId(u.getInputId())
                                .status(u.getStatus().toString())
                                .votes(votesFromEntity(u.getVotes(), u.getStatus().equals(UserStoryStatus.VOTED)))
                                .build()
                ).toList();
    }

    private List<VoteResponse> votesFromEntity(List<VoteEntity> votes, boolean isShowOption) {
        if (votes == null) return Collections.emptyList();

        return votes.stream().map(v ->
                VoteResponse.builder()
                        .memberId(v.getMember().getId())
                        .id(v.getId())
                        .option((isShowOption)?v.getOption():null)
                        .userStoryId(v.getUserStory().getId())
                .build()).toList();
    }

    private List<MemberResponse> fromEntityMembers(List<MemberEntity> members) {
        if (members == null) return Collections.emptyList();
        return members.stream().map(
                m -> MemberResponse.builder()
                        .id(m.getId())
                        .nickName(m.getNickName())
                        .build()
        ).toList();
    }
}
