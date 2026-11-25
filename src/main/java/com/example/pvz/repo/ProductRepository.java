package com.example.pvz.repo;

import com.example.pvz.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
    private final JdbcClient jdbc;

    public Product insert(UUID receptionId, String type) {
        return jdbc.sql("""
                INSERT INTO products(id, type, reception_id) VALUES (:id, :type, :r)
                RETURNING id, date_time, type, reception_id""")
                .param("id", UUID.randomUUID()).param("type", type).param("r", receptionId)
                .query(Product.class).single();
    }

    public Optional<UUID> deleteLast(UUID receptionId) {
        return jdbc.sql("""
                DELETE FROM products
                WHERE id = (SELECT id FROM products WHERE reception_id = :r
                            ORDER BY seq DESC LIMIT 1)
                RETURNING id""")
                .param("r", receptionId).query(UUID.class).optional();
    }
}