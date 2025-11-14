package com.example.compliance.controller;

import com.example.compliance.dto.IssueFilter;
import com.example.compliance.dto.IssueForm;
import com.example.compliance.model.Issue;
import com.example.compliance.model.User;
import com.example.compliance.service.IssueService;
import com.example.compliance.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

/**
 * Управление заявками пользователя.
 */
@Controller
@RequestMapping("/portal/issues")
public class PortalIssueController {

    private final UserService userService;
    private final IssueService issueService;

    public PortalIssueController(final UserService userService,
                                 final IssueService issueService) {
        this.userService = userService;
        this.issueService = issueService;
    }

    @ModelAttribute("filter")
    public IssueFilter filter() {
        return new IssueFilter();
    }

    @GetMapping
    public String list(final Authentication authentication,
                       @ModelAttribute("filter") final IssueFilter filter,
                       final Model model) {
        User user = userService.loadByUsername(authentication.getName());
        List<Issue> issues = issueService.findUserIssues(user, filter);
        model.addAttribute("issues", issues);
        return "portal/issues/list";
    }

    @GetMapping("/new")
    public String newIssue(final Model model) {
        if (!model.containsAttribute("issueForm")) {
            model.addAttribute("issueForm", new IssueForm());
        }
        return "portal/issues/new";
    }

    @PostMapping
    public String create(final Authentication authentication,
                         @Valid @ModelAttribute("issueForm") final IssueForm issueForm,
                         final BindingResult bindingResult,
                         final Model model) {
        if (bindingResult.hasErrors()) {
            return "portal/issues/new";
        }
        User user = userService.loadByUsername(authentication.getName());
        Issue issue = issueService.createIssue(user, issueForm);
        return "redirect:/portal/issues/" + issue.getId();
    }

    @GetMapping("/{id}")
    public String view(final Authentication authentication,
                       @PathVariable("id") final UUID id,
                       final Model model) {
        User user = userService.loadByUsername(authentication.getName());
        try {
            Issue issue = issueService.getOwnedIssue(user, id);
            model.addAttribute("issue", issue);
            return "portal/issues/view";
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }
}
