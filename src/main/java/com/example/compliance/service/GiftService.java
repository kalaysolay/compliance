package com.example.compliance.service;

import com.example.compliance.dto.GiftFilter;
import com.example.compliance.dto.GiftForm;
import com.example.compliance.model.Gift;
import com.example.compliance.model.User;
import com.example.compliance.repo.GiftRepository;
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
 * Сервис учёта подарков.
 */
@Service
public class GiftService {

    private final GiftRepository giftRepository;
    private final DocumentNumberGenerator documentNumberGenerator;

    public GiftService(final GiftRepository giftRepository,
                       final DocumentNumberGenerator documentNumberGenerator) {
        this.giftRepository = giftRepository;
        this.documentNumberGenerator = documentNumberGenerator;
    }

    /**
     * Создаёт запись о подарке.
     *
     * @param creator пользователь
     * @param form данные формы
     * @return созданная сущность
     */
    @Transactional
    public Gift createGift(final User creator, final GiftForm form) {
        Gift gift = new Gift();
        gift.setId(UUID.randomUUID());
        gift.setDocumentNumber(documentNumberGenerator.nextGiftNumber());
        gift.setCreator(creator);
        gift.setCounterparty(form.getCounterparty());
        gift.setEstimatedValue(form.getEstimatedValue());
        gift.setCurrency(form.getCurrency());
        gift.setJustification(form.getJustification());
        gift.setEventDate(form.getEventDate());
        return giftRepository.save(gift);
    }

    /**
     * Возвращает подарок, принадлежащий пользователю.
     *
     * @param user пользователь
     * @param id идентификатор
     * @return найденная запись
     */
    @Transactional(readOnly = true)
    public Gift getOwnedGift(final User user, final UUID id) {
        Gift gift = getGift(id);
        if (!gift.getCreator().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Gift not found for user");
        }
        return gift;
    }

    /**
     * Возвращает подарок по идентификатору.
     *
     * @param id идентификатор
     * @return сущность
     */
    @Transactional(readOnly = true)
    public Gift getGift(final UUID id) {
        return giftRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Gift not found: " + id));
    }

    /**
     * Возвращает подарки пользователя по фильтрам.
     */
    @Transactional(readOnly = true)
    public List<Gift> findUserGifts(final User user, final GiftFilter filter) {
        Specification<Gift> spec = Specification.where(byCreator(user.getId()));
        spec = spec.and(applyFilter(filter));
        return giftRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    /**
     * Возвращает подарки для офицера.
     */
    @Transactional(readOnly = true)
    public List<Gift> findAllGifts(final GiftFilter filter) {
        return giftRepository.findAll(applyFilter(filter), Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    private Specification<Gift> applyFilter(final GiftFilter filter) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter != null) {
                if (filter.getCounterparty() != null && !filter.getCounterparty().isBlank()) {
                    predicates.add(builder.like(builder.lower(root.get("counterparty")),
                        "%" + filter.getCounterparty().toLowerCase() + "%"));
                }
                if (filter.getFromDate() != null) {
                    predicates.add(builder.greaterThanOrEqualTo(root.get("eventDate"), filter.getFromDate()));
                }
                if (filter.getToDate() != null) {
                    predicates.add(builder.lessThanOrEqualTo(root.get("eventDate"), filter.getToDate()));
                }
                if (filter.getMinValue() != null) {
                    predicates.add(builder.greaterThanOrEqualTo(root.get("estimatedValue"), filter.getMinValue()));
                }
                if (filter.getMaxValue() != null) {
                    predicates.add(builder.lessThanOrEqualTo(root.get("estimatedValue"), filter.getMaxValue()));
                }
                if (filter.getCreatorUsername() != null && !filter.getCreatorUsername().isBlank()) {
                    predicates.add(builder.like(builder.lower(root.get("creator").get("username")),
                        "%" + filter.getCreatorUsername().toLowerCase() + "%"));
                }
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Specification<Gift> byCreator(final UUID userId) {
        return (root, query, builder) -> builder.equal(root.get("creator").get("id"), userId);
    }
}
