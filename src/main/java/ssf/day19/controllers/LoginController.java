package ssf.day19.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import ssf.day19.config.Constants;
import ssf.day19.models.User;

@Controller
@RequestMapping
public class LoginController {
    @GetMapping("/login")
    public ModelAndView getLogin(HttpSession sess) {
        ModelAndView mav = new ModelAndView();

        // Reset session for new login
        sess.invalidate();

        mav.setViewName("login");

        return mav;
    }
    
    @PostMapping("/login")
    public ModelAndView postLogin(@ModelAttribute User user, HttpSession sess) {
        ModelAndView mav = new ModelAndView();

        sess.setAttribute(Constants.SESS_ATTR_USER, user);

        mav.setViewName("redirect:/listing");

        return mav;
    }
}