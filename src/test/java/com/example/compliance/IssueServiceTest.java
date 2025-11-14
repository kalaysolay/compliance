package com.example.compliance;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.compliance.dto.IssueForm;
import com.example.compliance.model.Issue;
import com.example.compliance.model.IssueType;
import com.example.compliance.model.User;
import com.example.compliance.service.IssueService;
import com.example.compliance.service.UserService;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Тесты сервисного слоя заявок.
 */
@SpringBootTest
@Transactional
class IssueServiceTest {

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    @Test
    void shouldCreateIssueWithGeneratedNumber() {
        User user = userService.loadByUsername("user");
        IssueForm form = new IssueForm();
        form.setIssueType(IssueType.OTHER);
        form.setDescription("Тестовая заявка");
        form.setEventDate(LocalDate.now());

        Issue created = issueService.createIssue(user, form);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getDocumentNumber()).startsWith("CA-");
        assertThat(created.getCreator().getId()).isEqualTo(user.getId());
    }
}
