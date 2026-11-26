package com.example.pvz.repo;

import com.example.pvz.domain.PvzDto;
import com.example.pvz.domain.PvzView;
import com.example.pvz.domain.Product;
import com.example.pvz.domain.Reception;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository @RequiredArgsConstructor
public class PvzQueryRepository {
    private final JdbcClient jdbc;

    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public List<PvzView> list(OffsetDateTime from, OffsetDateTime to, int page, int limit) {
        var cond = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        if (from != null) { cond.append(" AND r.date_time >= :from"); params.put("from", from); }
        if (to != null)   { cond.append(" AND r.date_time <= :to");   params.put("to", to); }
        params.put("limit", limit);
        params.put("offset", (page - 1) * limit);

        String where = cond.isEmpty() ? ""
                : "WHERE EXISTS (SELECT 1 FROM receptions r WHERE r.pvz_id = p.id" + cond + ")";

        List<PvzDto> pvzs = jdbc.sql("SELECT p.id, p.registration_date, p.city FROM pvz p " + where
                        + " ORDER BY p.registration_date DESC, p.id LIMIT :limit OFFSET :offset")
                .params(params).query(PvzDto.class).list();
        if (pvzs.isEmpty()) return List.of();

        Map<String, Object> rp = new HashMap<>(params);
        rp.put("ids", pvzs.stream().map(PvzDto::id).toList());
        List<Reception> recs = jdbc.sql("SELECT r.id, r.date_time, r.pvz_id, r.status FROM receptions r "
                        + "WHERE r.pvz_id IN (:ids)" + cond + " ORDER BY r.pvz_id, r.date_time")
                .params(rp).query(Reception.class).list();

        Map<UUID, List<Product>> prodByRec = recs.isEmpty() ? Map.of()
                : jdbc.sql("SELECT id, date_time, type, reception_id FROM products "
                        + "WHERE reception_id IN (:rids) ORDER BY reception_id, seq")
                .param("rids", recs.stream().map(Reception::id).toList())
                .query(Product.class).list().stream()
                .collect(Collectors.groupingBy(Product::receptionId));

        Map<UUID, List<Reception>> recByPvz = recs.stream()
                .collect(Collectors.groupingBy(Reception::pvzId));

        return pvzs.stream().map(p -> new PvzView(p,
                recByPvz.getOrDefault(p.id(), List.of()).stream()
                        .map(r -> new PvzView.ReceptionView(r, prodByRec.getOrDefault(r.id(), List.of())))
                        .toList())).toList();
    }
}