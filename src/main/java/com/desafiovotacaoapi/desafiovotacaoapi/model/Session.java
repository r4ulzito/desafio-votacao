package com.desafiovotacaoapi.desafiovotacaoapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "tb_sessions")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Builder.Default
    private boolean isOpen = true;

    public void setOpen(boolean open) {
        this.isOpen = open;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return isOpen() == session.isOpen()
                && Objects.equals(getId(), session.getId())
                && Objects.equals(getTopic(), session.getTopic())
                && Objects.equals(getDataStart(), session.getDataStart())
                && Objects.equals(getDataEnd(), session.getDataEnd());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTopic(), getDataStart(), getDataEnd(), isOpen());
    }
}


