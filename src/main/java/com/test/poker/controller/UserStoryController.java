package com.test.poker.controller;

import com.test.poker.model.response.UserStoryResponse;
import com.test.poker.service.UserStoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-stories")
@RequiredArgsConstructor
public class UserStoryController {

    private final UserStoryService userStoryService;

    /**
     * Create user story in defined session (inner session id used). User story response model contains
     * database id key and entered by user inputId (can be changed depends on business logic).
     *
     * @param inputId entered by user id
     * @param description user story description provided by user
     * @param sessionId session id (inner database key)
     * @return user story model
     */
    @PostMapping
    public UserStoryResponse create(@RequestParam("id") String inputId, @RequestParam("description") String description,
                                    @RequestParam("session_id") long sessionId){
        return userStoryService.create(inputId, description, sessionId);
    }

    /**
     * Get user story model by inner id. All endpoints which return user story model
     * provide votes list depending on user story status (if status VOTING vote's field option is hidden).
     *
     * @param id inner user story id
     * @return user story model
     */
    @GetMapping("/{id}")
    public UserStoryResponse get(@PathVariable long id){
        return userStoryService.getUserStory(id);
    }

    /**
     * Marks defined user story as deleted (means we don't return this model in user stories list).
     *
     * @param id inner user story database key
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id){
        userStoryService.delete(id);
    }

    /**
     * Switch user story status from PENDING to VOTING.
     *
     * @param id inner user story database key
     */
    @PostMapping("/start-voting")
    public void startVoting(@RequestParam("id") long id){
        userStoryService.startVoting(id);
    }

    /**
     * Vote user story by defined user. Increment counter emitted_votes n user story model.
     *
     * @param userStoryId database user story id
     * @param option user vote, can be changed depends on business logic
     * @param memberId user who vote, must be joined to this session
     */
    @PostMapping("/vote")
    public void vote(@RequestParam("user_story_id") long userStoryId, @RequestParam("option") String option,
                     @RequestParam("member_id") long memberId){
        userStoryService.voteUserStory(userStoryId, option, memberId);
    }

    /**
     * Switch user story status from "VOTING" to "VOTED".
     *
     * @param userStoryId database user story key
     */
    @PostMapping("/finish-voting")
    public void finishVoting(@RequestParam("user_story_id") long userStoryId){
        userStoryService.finishVoting(userStoryId);
    }
}
