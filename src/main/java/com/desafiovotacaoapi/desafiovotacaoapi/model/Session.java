package com.desafiovotacaoapi.desafiovotacaoapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "tb_sessions")
@Entity(name = "Session")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "topic_id", nullable = false)
    @ManyToOne
    private Topic topic;

    @Column(name = "data_start", nullable = false)
    private LocalDateTime dataStart;

    @Column(name = "data_end", nullable = false)
    private LocalDateTime dataEnd;

    @Column(name = "is_open", nullable = false)
    private boolean isOpen;

    public Session(LocalDateTime dataEnd, Topic topic) {
        this.dataStart = LocalDateTime.now();
        this.dataEnd = dataEnd;
        this.topic = topic;
        this.isOpen = true;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Session session = (Session) o;
        return id != null && Objects.equals(id, session.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}


