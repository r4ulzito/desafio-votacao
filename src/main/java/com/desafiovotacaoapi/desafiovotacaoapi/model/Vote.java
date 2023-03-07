package com.desafiovotacaoapi.desafiovotacaoapi.model;

import com.desafiovotacaoapi.desafiovotacaoapi.model.enums.Answer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Table(name = "tb_votes")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return Objects.equals(getId(), vote.getId())
                && Objects.equals(getAssociate(), vote.getAssociate())
                && Objects.equals(getTopic(), vote.getTopic())
                && getAnswer() == vote.getAnswer();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAssociate(), getTopic(), getAnswer());
    }
}
