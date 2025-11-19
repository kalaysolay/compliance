package com.example.compliance.controller;

import com.example.compliance.dto.GiftFilter;
import com.example.compliance.dto.IssueFilter;
import com.example.compliance.model.Gift;
import com.example.compliance.model.Issue;
import com.example.compliance.model.IssueStatus;
import com.example.compliance.service.GiftService;
import com.example.compliance.service.IssueService;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Дашборд офицера комплаенс.
 */
@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final IssueService issueService;
    private final GiftService giftService;

    public AdminDashboardController(final IssueService issueService,
                                    final GiftService giftService) {
        this.issueService = issueService;
        this.giftService = giftService;
    }

    @GetMapping("/dashboard")
    public String dashboard(final Model model) {
        List<Issue> issues = issueService.findAllIssues(new IssueFilter());
        List<Gift> gifts = giftService.findAllGifts(new GiftFilter());
        Map<IssueStatus, Long> counters = new EnumMap<>(IssueStatus.class);
        for (IssueStatus status : IssueStatus.values()) {
            counters.put(status, 0L);
        }
        counters.putAll(issues.stream()
            .collect(Collectors.groupingBy(Issue::getStatus, () -> new EnumMap<>(IssueStatus.class), Collectors.counting())));
        model.addAttribute("issues", issues.stream().limit(5).collect(Collectors.toList()));
        model.addAttribute("gifts", gifts.stream().limit(5).collect(Collectors.toList()));
        model.addAttribute("statusCounters", counters);
        return "admin/dashboard";
    }
}
