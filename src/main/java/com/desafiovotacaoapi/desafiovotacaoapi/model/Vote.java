package com.desafiovotacaoapi.desafiovotacaoapi.model;

import com.desafiovotacaoapi.desafiovotacaoapi.model.enums.Answer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.util.Objects;

@Table(name = "tb_votes")
@Entity(name = "Vote")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "associate_id", nullable = false)
    @OneToOne
    private Associate associate;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Answer answer;

    public Vote(Answer answer, Associate associate, Topic topic) {

        this.answer = answer;
        this.associate = associate;
        this.topic = topic;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Vote vote = (Vote) o;
        return getId() != null && Objects.equals(getId(), vote.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
