package com.example.compliance.controller;

import com.example.compliance.model.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Корневой контроллер перенаправляет пользователя в нужный раздел.
 */
@Controller
public class RootController {

    @GetMapping("/")
    public String root(final Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_" + UserRole.OFFICER.name())) {
                return "redirect:/admin/dashboard";
            }
        }
        return "redirect:/portal/dashboard";
    }
}
