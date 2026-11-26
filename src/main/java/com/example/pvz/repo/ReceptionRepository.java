package com.example.pvz.repo;

import com.example.pvz.domain.Reception;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReceptionRepository {
    private final JdbcClient jdbc;

    public Optional<Reception> insertOpen(UUID pvzId) {
        return jdbc.sql("""
                INSERT INTO receptions(id, pvz_id, status)
                VALUES (:id, :pvz, 'in_progress')
                ON CONFLICT (pvz_id) WHERE status = 'in_progress' DO NOTHING
                RETURNING id, date_time, pvz_id, status""")
                .param("id", UUID.randomUUID())
                .param("pvz", pvzId)
                .query(Reception.class).optional();
    }

    public Optional<UUID> lockOpen(UUID pvzId) {
        return jdbc.sql("""
            SELECT id FROM receptions
            WHERE pvz_id = :pvz AND status = 'in_progress'
            FOR UPDATE""")
                .param("pvz", pvzId).query(UUID.class).optional();
    }

    public Optional<Reception> close(UUID pvzId) {
        return jdbc.sql("""
            UPDATE receptions SET status = 'close'
            WHERE pvz_id = :pvz AND status = 'in_progress'
            RETURNING id, date_time, pvz_id, status""")
                .param("pvz", pvzId).query(Reception.class).optional();
    }
}