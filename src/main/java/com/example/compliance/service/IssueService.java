package com.example.compliance.service;

import com.example.compliance.dto.IssueFilter;
import com.example.compliance.dto.IssueForm;
import com.example.compliance.model.Issue;
import com.example.compliance.model.IssueStatus;
import com.example.compliance.model.User;
import com.example.compliance.repo.IssueRepository;
import com.example.compliance.util.DocumentNumberGenerator;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис управления заявками.
 */
@Service
public class IssueService {

    private final IssueRepository issueRepository;
    private final DocumentNumberGenerator documentNumberGenerator;

    public IssueService(final IssueRepository issueRepository,
                        final DocumentNumberGenerator documentNumberGenerator) {
        this.issueRepository = issueRepository;
        this.documentNumberGenerator = documentNumberGenerator;
    }

    /**
     * Создаёт новую заявку от имени пользователя.
     *
     * @param creator автор заявки
     * @param form данные формы
     * @return созданная сущность
     */
    @Transactional
    public Issue createIssue(final User creator, final IssueForm form) {
        Issue issue = new Issue();
        issue.setId(UUID.randomUUID());
        issue.setDocumentNumber(documentNumberGenerator.nextIssueNumber());
        issue.setCreator(creator);
        issue.setIssueType(form.getIssueType());
        issue.setDescription(form.getDescription());
        issue.setEventDate(form.getEventDate());
        issue.setApproxDamage(form.getApproxDamage());
        issue.setStatus(IssueStatus.NEW);
        return issueRepository.save(issue);
    }

    /**
     * Находит заявку по идентификатору, доступную пользователю.
     *
     * @param user пользователь
     * @param id идентификатор заявки
     * @return найденная заявка или исключение
     */
    @Transactional(readOnly = true)
    public Issue getOwnedIssue(final User user, final UUID id) {
        Issue issue = getIssue(id);
        if (!issue.getCreator().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Issue not found for user");
        }
        return issue;
    }

    /**
     * Возвращает заявку по идентификатору.
     *
     * @param id идентификатор
     * @return сущность
     */
    @Transactional(readOnly = true)
    public Issue getIssue(final UUID id) {
        return issueRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Issue not found: " + id));
    }

    /**
     * Возвращает список заявок пользователя с учётом фильтров.
     *
     * @param user пользователь
     * @param filter фильтры
     * @return список сущностей
     */
    @Transactional(readOnly = true)
    public List<Issue> findUserIssues(final User user, final IssueFilter filter) {
        Specification<Issue> spec = Specification.where(byCreator(user.getId()));
        spec = spec.and(applyFilter(filter));
        return issueRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    /**
     * Возвращает список всех заявок для офицера.
     *
     * @param filter фильтр
     * @return список
     */
    @Transactional(readOnly = true)
    public List<Issue> findAllIssues(final IssueFilter filter) {
        Specification<Issue> spec = applyFilter(filter);
        return issueRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    /**
     * Обновляет статус заявки.
     *
     * @param issue заявка
     * @param status статус
     * @return обновлённая сущность
     */
    @Transactional
    public Issue updateStatus(final Issue issue, final IssueStatus status) {
        issue.setStatus(status);
        return issueRepository.save(issue);
    }

    private Specification<Issue> applyFilter(final IssueFilter filter) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter != null) {
                if (filter.getIssueType() != null) {
                    predicates.add(builder.equal(root.get("issueType"), filter.getIssueType()));
                }
                if (filter.getStatus() != null) {
                    predicates.add(builder.equal(root.get("status"), filter.getStatus()));
                }
                if (filter.getFromDate() != null) {
                    predicates.add(builder.greaterThanOrEqualTo(root.get("eventDate"), filter.getFromDate()));
                }
                if (filter.getToDate() != null) {
                    predicates.add(builder.lessThanOrEqualTo(root.get("eventDate"), filter.getToDate()));
                }
                if (filter.getQuery() != null && !filter.getQuery().isBlank()) {
                    predicates.add(builder.like(builder.lower(root.get("description")),
                        "%" + filter.getQuery().toLowerCase() + "%"));
                }
                if (filter.getCreatorUsername() != null && !filter.getCreatorUsername().isBlank()) {
                    predicates.add(builder.like(builder.lower(root.get("creator").get("username")),
                        "%" + filter.getCreatorUsername().toLowerCase() + "%"));
                }
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Specification<Issue> byCreator(final UUID userId) {
        return (root, query, builder) -> builder.equal(root.get("creator").get("id"), userId);
    }
}
