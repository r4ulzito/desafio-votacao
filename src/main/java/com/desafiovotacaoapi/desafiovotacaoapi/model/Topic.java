package com.desafiovotacaoapi.desafiovotacaoapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Table(name = "tb_topics")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return Objects.equals(getId(), topic.getId())
                && Objects.equals(getTitle(), topic.getTitle())
                && Objects.equals(getDescription(), topic.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription());
    }
}
