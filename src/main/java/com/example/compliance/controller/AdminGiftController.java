package com.example.compliance.controller;

import com.example.compliance.dto.GiftFilter;
import com.example.compliance.model.Gift;
import com.example.compliance.service.GiftService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

/**
 * Контроллер просмотра подарков в админке.
 */
@Controller
@RequestMapping("/admin/gifts")
public class AdminGiftController {

    private final GiftService giftService;

    public AdminGiftController(final GiftService giftService) {
        this.giftService = giftService;
    }

    @ModelAttribute("filter")
    public GiftFilter filter() {
        return new GiftFilter();
    }

    @GetMapping
    public String list(@ModelAttribute("filter") final GiftFilter filter,
                       final Model model) {
        List<Gift> gifts = giftService.findAllGifts(filter);
        model.addAttribute("gifts", gifts);
        return "admin/gifts/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable("id") final UUID id, final Model model) {
        try {
            Gift gift = giftService.getGift(id);
            model.addAttribute("gift", gift);
            return "admin/gifts/view";
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }
}
