package com.test.poker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id", nullable=false, insertable = false, updatable = false)
    private UserStoryEntity userStory;

    @ManyToOne
    @JoinColumn(name="id", nullable=false, insertable = false, updatable = false)
    private MemberEntity member;

    private String option;
}
