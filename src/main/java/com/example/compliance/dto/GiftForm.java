package com.example.compliance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Форма регистрации подарка.
 */
public class GiftForm {

    @NotBlank
    @Size(min = 1, max = 255)
    private String counterparty;

    @NotNull
    @PositiveOrZero
    private BigDecimal estimatedValue;

    @NotBlank
    @Size(min = 3, max = 3)
    private String currency = "KZT";

    @NotBlank
    @Size(min = 1, max = 1000)
    private String justification;

    @NotNull
    private LocalDate eventDate;

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(final String counterparty) {
        this.counterparty = counterparty;
    }

    public BigDecimal getEstimatedValue() {
        return estimatedValue;
    }

    public void setEstimatedValue(final BigDecimal estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(final String justification) {
        this.justification = justification;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(final LocalDate eventDate) {
        this.eventDate = eventDate;
    }
}
