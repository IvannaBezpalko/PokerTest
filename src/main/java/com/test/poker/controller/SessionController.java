package com.test.poker.controller;

import com.test.poker.model.response.SessionResponse;
import com.test.poker.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    /**
     * Create session. Session id param is an inner application params (key from database),
     * uuid param is a generated UUID random String, should be used in invite link in public cases.
     *
     * @param title Session title
     * @param deckType Allowed enum values: CLUBS, DIAMONDS, SPADES, HEARTS.
     * @return Session model
     */
    @PostMapping()
    public SessionResponse createSession(@RequestParam("title") String title,
                                         @RequestParam("deck_type") String deckType) {
        return sessionService.create(title, deckType);
    }

    /**
     * Get session model by uuid (string key for public).
     *
     * @param uuid can be changed to long id (database key) depends on business logic
     * @return Session model with user stories list.
     */
    @GetMapping("/{uuid}")
    public SessionResponse get(@PathVariable String uuid) {
        return sessionService.getSession(uuid);
    }

    /**
     * Method for entering existing session by new user.
     *
     * @param name New member nickname
     * @param uuid session public key, can be changed depends on business logic
     * @return session model
     */
    @PostMapping("/enter/{uuid}")
    public SessionResponse enterSession(@RequestParam("name") String name, @PathVariable("uuid") String uuid) {
        return sessionService.enter(name, uuid);
    }

    /**
     * Delete session and all session's data (cascade type). Should be available only for users with admin roles.
     *
     * @param id session inner key (long id database key)
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        sessionService.delete(id);
    }
}
