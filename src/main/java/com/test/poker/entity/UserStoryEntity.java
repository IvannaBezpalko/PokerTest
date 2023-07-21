package com.test.poker.entity;

import com.test.poker.model.UserStoryStatus;
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
public class UserStoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "input_id")
    private String inputId;

    private String description;

    @ManyToOne
    @JoinColumn(name="id", nullable=false, insertable = false, updatable = false)
    private SessionEntity session;

    private UserStoryStatus status;

    private boolean isDeleted = false;

    private int emittedVotes = 0;

    @OneToMany(mappedBy= "userStory", cascade = { CascadeType.REMOVE, CascadeType.PERSIST })
    private List<VoteEntity> votes;
}
