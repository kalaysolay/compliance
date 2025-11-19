package com.example.compliance.dto;

import com.example.compliance.model.IssueType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Форма создания заявки пользователя.
 */
public class IssueForm {

    @NotNull
    private IssueType issueType;

    @NotBlank
    @Size(min = 1, max = 2000)
    private String description;

    @NotNull
    private LocalDate eventDate;

    @PositiveOrZero
    private BigDecimal approxDamage;

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(final IssueType issueType) {
        this.issueType = issueType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(final LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public BigDecimal getApproxDamage() {
        return approxDamage;
    }

    public void setApproxDamage(final BigDecimal approxDamage) {
        this.approxDamage = approxDamage;
    }
}
