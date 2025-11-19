package com.example.compliance.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

/**
 * Генерация человеко-читаемых номеров документов.
 */
@Component
public class DocumentNumberGenerator {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Формирует номер заявки вида CA-YYYY-NNNNNN.
     *
     * @return строка номера
     */
    @Transactional
    public String nextIssueNumber() {
        long seq = nextVal("seq_issues");
        return format("CA", seq);
    }

    /**
     * Формирует номер подарка вида GA-YYYY-NNNNNN.
     *
     * @return строка номера
     */
    @Transactional
    public String nextGiftNumber() {
        long seq = nextVal("seq_gifts");
        return format("GA", seq);
    }

    private String format(final String prefix, final long seq) {
        int year = LocalDate.now().getYear();
        return String.format("%s-%d-%06d", prefix, year, seq);
    }

    private long nextVal(final String sequence) {
        Object value = entityManager
            .createNativeQuery("SELECT nextval('" + sequence + "')")
            .getSingleResult();
        if (value instanceof Number number) {
            return number.longValue();
        }
        throw new IllegalStateException("Sequence did not return numeric value");
    }
}
