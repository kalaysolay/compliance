package com.example.compliance.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Параметры фильтрации подарков.
 */
public class GiftFilter {
    private String counterparty;
    private LocalDate fromDate;
    private LocalDate toDate;
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private String creatorUsername;

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(final String counterparty) {
        this.counterparty = counterparty;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(final LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(final LocalDate toDate) {
        this.toDate = toDate;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public void setMinValue(final BigDecimal minValue) {
        this.minValue = minValue;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(final BigDecimal maxValue) {
        this.maxValue = maxValue;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(final String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }
}
