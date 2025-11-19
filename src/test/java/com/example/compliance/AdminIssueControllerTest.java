package com.example.compliance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.compliance.dto.IssueForm;
import com.example.compliance.model.Issue;
import com.example.compliance.model.IssueStatus;
import com.example.compliance.model.IssueType;
import com.example.compliance.model.User;
import com.example.compliance.service.IssueService;
import com.example.compliance.service.UserService;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Интеграционные тесты админского контроллера заявок.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AdminIssueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    private UUID issueId;

    @BeforeEach
    void setUp() {
        User user = userService.loadByUsername("user");
        IssueForm form = new IssueForm();
        form.setIssueType(IssueType.GIFT);
        form.setDescription("Админ тест");
        form.setEventDate(LocalDate.now());
        Issue issue = issueService.createIssue(user, form);
        issueId = issue.getId();
    }
/*
    @Test
    @WithMockUser(username = "officer", roles = "OFFICER")
    void officerShouldAccessIssueList() throws Exception {
        mockMvc.perform(get("/admin/issues"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "officer", roles = "OFFICER")
    void officerCanUpdateStatus() throws Exception {
        mockMvc.perform(post("/admin/issues/" + issueId + "/status")
                .param("status", IssueStatus.DONE.name())
                .with(csrf()))
            .andExpect(status().is3xxRedirection());

        Issue updated = issueService.getIssue(issueId);
        assertThat(updated.getStatus()).isEqualTo(IssueStatus.DONE);
    }*/
}
