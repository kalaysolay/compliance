package com.example.compliance.repo;

import com.example.compliance.model.Gift;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Репозиторий подарков.
 */
public interface GiftRepository extends JpaRepository<Gift, UUID>, JpaSpecificationExecutor<Gift> {
}
