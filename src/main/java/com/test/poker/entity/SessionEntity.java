package com.test.poker.entity;

import com.test.poker.model.DeckType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;

    private String title;

    private DeckType deckType;

    @OneToMany(mappedBy= "session", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<UserStoryEntity> userStories;

    @OneToMany(cascade = CascadeType.ALL)
    private List<MemberEntity> members;
}
