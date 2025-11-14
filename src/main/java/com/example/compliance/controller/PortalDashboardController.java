package com.example.compliance.controller;

import com.example.compliance.dto.GiftFilter;
import com.example.compliance.dto.IssueFilter;
import com.example.compliance.model.Gift;
import com.example.compliance.model.Issue;
import com.example.compliance.model.User;
import com.example.compliance.service.GiftService;
import com.example.compliance.service.IssueService;
import com.example.compliance.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Дашборд пользователя.
 */
@Controller
@RequestMapping("/portal")
public class PortalDashboardController {

    private final UserService userService;
    private final IssueService issueService;
    private final GiftService giftService;

    public PortalDashboardController(final UserService userService,
                                     final IssueService issueService,
                                     final GiftService giftService) {
        this.userService = userService;
        this.issueService = issueService;
        this.giftService = giftService;
    }

    @GetMapping("/dashboard")
    public String dashboard(final Authentication authentication, final Model model) {
        User user = userService.loadByUsername(authentication.getName());
        List<Issue> lastIssues = issueService.findUserIssues(user, new IssueFilter()).stream()
            .limit(5)
            .collect(Collectors.toList());
        List<Gift> lastGifts = giftService.findUserGifts(user, new GiftFilter()).stream()
            .limit(5)
            .collect(Collectors.toList());
        model.addAttribute("issues", lastIssues);
        model.addAttribute("gifts", lastGifts);
        model.addAttribute("user", user);
        return "portal/dashboard";
    }
}
