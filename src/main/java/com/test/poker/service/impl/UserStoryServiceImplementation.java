package com.test.poker.service.impl;

import com.test.poker.entity.MemberEntity;
import com.test.poker.entity.SessionEntity;
import com.test.poker.entity.UserStoryEntity;
import com.test.poker.entity.VoteEntity;
import com.test.poker.exception.AppBadRequestException;
import com.test.poker.model.UserStoryStatus;
import com.test.poker.model.response.UserStoryResponse;
import com.test.poker.model.response.VoteResponse;
import com.test.poker.repository.MemberRepository;
import com.test.poker.repository.SessionRepository;
import com.test.poker.repository.UserStoryRepository;
import com.test.poker.repository.VoteRepository;
import com.test.poker.service.UserStoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserStoryServiceImplementation implements UserStoryService {

    private final UserStoryRepository userStoryRepository;
    private final SessionRepository sessionRepository;

    private final VoteRepository voteRepository;
    private final MemberRepository memberRepository;

    @Override
    public UserStoryResponse create(String id, String description, long sessionId) {
        SessionEntity session = sessionRepository.findById(sessionId).orElseThrow();
        UserStoryEntity entity = UserStoryEntity.builder()
                .description(description)
                .session(session)
                .inputId(id)
                .status(UserStoryStatus.PENDING)
                .build();
        return fromEntity(userStoryRepository.save(entity));
    }

    @Override
    public void delete(long id) {
        UserStoryEntity userStoryEntity = userStoryRepository.findById(id).orElseThrow();
        if(userStoryEntity.getStatus().equals(UserStoryStatus.PENDING)){
            userStoryEntity.setDeleted(true);
            userStoryRepository.save(userStoryEntity);
        } else {
            throw new AppBadRequestException("This user story has status " + userStoryEntity.getStatus().toString());
        }
    }

    @Override
    public void startVoting(long id) {
        UserStoryEntity entity = userStoryRepository.findById(id).orElseThrow();
        if(entity.getStatus().equals(UserStoryStatus.VOTING)){
            throw new AppBadRequestException("The user story has VOTING status.");
        } else {
            entity.setStatus(UserStoryStatus.VOTING);
            userStoryRepository.save(entity);
        }
    }

    @Override
    public void voteUserStory(long id, String option, long memberId) {
        UserStoryEntity userStory = userStoryRepository.findById(id).orElseThrow();
        MemberEntity member = memberRepository.findById(memberId).orElseThrow();
        if(userStory.getStatus().equals(UserStoryStatus.VOTING)){
            VoteEntity vote = VoteEntity.builder()
                    .userStory(userStory)
                    .option(option)
                    .member(member)
                    .build();
            voteRepository.save(vote);
            userStory.setEmittedVotes(userStory.getEmittedVotes() + 1);
            userStoryRepository.save(userStory);
        } else {
            throw new AppBadRequestException("The user story has not VOTING status.");
        }
    }

    @Override
    public void finishVoting(long userStoryId) {
        UserStoryEntity userStory = userStoryRepository.findById(userStoryId).orElseThrow();
        if(userStory.getStatus().equals(UserStoryStatus.VOTING)){
            userStory.setStatus(UserStoryStatus.VOTED);
            userStoryRepository.save(userStory);
        } else {
            throw new AppBadRequestException("The user story has not VOTING status.");
        }
    }

    @Override
    public UserStoryResponse getUserStory(long id) {
        UserStoryEntity entity = userStoryRepository.findById(id).orElseThrow();
        boolean isOptionShow = !entity.getStatus().equals(UserStoryStatus.VOTING);
        return UserStoryResponse.builder()
                .id(entity.getId())
                .inputId(entity.getInputId())
                .sessionId(entity.getSession().getId())
                .status(entity.getStatus().toString())
                .description(entity.getDescription())
                .emittedVotes(entity.getEmittedVotes())
                .votes(votesFromEntity(entity.getVotes(), isOptionShow))
                .build();
    }

    private UserStoryResponse fromEntity(UserStoryEntity entity) {
        if(entity == null){
            return null;
        }
        boolean isOptionShow = !entity.getStatus().equals(UserStoryStatus.VOTING);
        return UserStoryResponse.builder()
                .sessionId(entity.getSession().getId())
                .id(entity.getId())
                .inputId(entity.getInputId())
                .status(entity.getStatus().toString())
                .description(entity.getDescription())
                .emittedVotes(entity.getEmittedVotes())
                .votes(votesFromEntity(entity.getVotes(), isOptionShow))
                .build();
    }

    private List<VoteResponse> votesFromEntity(List<VoteEntity> votes, boolean isShowOption) {
        if (votes == null) return Collections.emptyList();
        return votes.stream().map(v ->
                VoteResponse.builder()
                        .memberId(v.getMember().getId())
                        .id(v.getId())
                        .option((isShowOption)?v.getOption():"")
                        .userStoryId(v.getUserStory().getId())
                        .build()).toList();
    }
}
