package com.example.pvz.repo;

import com.example.pvz.domain.PvzEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PvzRepository extends JpaRepository<PvzEntity, UUID> {}
