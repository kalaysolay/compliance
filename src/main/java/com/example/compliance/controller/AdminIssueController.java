package com.example.compliance.controller;

import com.example.compliance.dto.IssueFilter;
import com.example.compliance.model.Issue;
import com.example.compliance.model.IssueStatus;
import com.example.compliance.service.IssueService;
import jakarta.validation.constraints.NotNull;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

/**
 * Контроллер управления заявками в админке.
 */
@Controller
@RequestMapping("/admin/issues")
@Validated
public class AdminIssueController {

    private final IssueService issueService;

    public AdminIssueController(final IssueService issueService) {
        this.issueService = issueService;
    }

    @ModelAttribute("filter")
    public IssueFilter filter() {
        return new IssueFilter();
    }

    @ModelAttribute("statuses")
    public IssueStatus[] statuses() {
        return IssueStatus.values();
    }

    @GetMapping
    public String list(@ModelAttribute("filter") final IssueFilter filter,
                       final Model model) {
        List<Issue> issues = issueService.findAllIssues(filter);
        model.addAttribute("issues", issues);
        return "admin/issues/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable("id") final UUID id, final Model model) {
        try {
            Issue issue = issueService.getIssue(id);
            model.addAttribute("issue", issue);
            model.addAttribute("availableStatuses", EnumSet.allOf(IssueStatus.class));
            return "admin/issues/view";
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable("id") final UUID id,
                               @RequestParam("status") @NotNull final IssueStatus status) {
        try {
            Issue issue = issueService.getIssue(id);
            issueService.updateStatus(issue, status);
            return "redirect:/admin/issues/" + id;
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }
}
