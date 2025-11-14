package com.example.compliance.dto;

import com.example.compliance.model.IssueStatus;
import com.example.compliance.model.IssueType;
import java.time.LocalDate;

/**
 * Параметры фильтрации заявок.
 */
public class IssueFilter {
    private IssueType issueType;
    private IssueStatus status;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String query;
    private String creatorUsername;

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(final IssueType issueType) {
        this.issueType = issueType;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(final IssueStatus status) {
        this.status = status;
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

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(final String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }
}
