package com.example.compliance.controller;

import com.example.compliance.dto.GiftFilter;
import com.example.compliance.dto.GiftForm;
import com.example.compliance.model.Gift;
import com.example.compliance.model.User;
import com.example.compliance.service.GiftService;
import com.example.compliance.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
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

/**
 * Управление подарками пользователя.
 */
@Controller
@RequestMapping("/portal/gifts")
public class PortalGiftController {

    private final UserService userService;
    private final GiftService giftService;

    public PortalGiftController(final UserService userService,
                                final GiftService giftService) {
        this.userService = userService;
        this.giftService = giftService;
    }

    @ModelAttribute("filter")
    public GiftFilter filter() {
        return new GiftFilter();
    }

    @GetMapping
    public String list(final Authentication authentication,
                       @ModelAttribute("filter") final GiftFilter filter,
                       final Model model) {
        User user = userService.loadByUsername(authentication.getName());
        List<Gift> gifts = giftService.findUserGifts(user, filter);
        model.addAttribute("gifts", gifts);
        return "portal/gifts/list";
    }

    @GetMapping("/new")
    public String newGift(final Model model) {
        if (!model.containsAttribute("giftForm")) {
            model.addAttribute("giftForm", new GiftForm());
        }
        return "portal/gifts/new";
    }

    @PostMapping
    public String create(final Authentication authentication,
                         @Valid @ModelAttribute("giftForm") final GiftForm giftForm,
                         final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "portal/gifts/new";
        }
        User user = userService.loadByUsername(authentication.getName());
        Gift gift = giftService.createGift(user, giftForm);
        return "redirect:/portal/gifts/" + gift.getId();
    }

    @GetMapping("/{id}")
    public String view(final Authentication authentication,
                       @PathVariable("id") final UUID id,
                       final Model model) {
        User user = userService.loadByUsername(authentication.getName());
        try {
            Gift gift = giftService.getOwnedGift(user, id);
            model.addAttribute("gift", gift);
            return "portal/gifts/view";
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }
}
