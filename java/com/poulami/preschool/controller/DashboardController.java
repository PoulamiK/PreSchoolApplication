package com.poulami.preschool.controller;

import com.poulami.preschool.model.Person;
import com.poulami.preschool.repository.PersonRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class DashboardController {

    @Autowired
    PersonRepository personRepository;

    @Value("${preschool.pageSize}")
    private int defaultPageSize;

    @Value("${preschool.contact.successMsg}")
    private String message;

    @Autowired
    Environment environment;

    @RequestMapping("/dashboard")
    public String displayDashboard(Model model,Authentication authentication, HttpSession session) {
        Person person = personRepository.readByEmail(authentication.getName());
        model.addAttribute("username", person.getName());
        model.addAttribute("roles", authentication.getAuthorities().toString());
        if(null != person.getPreClass() && null != person.getPreClass().getName()){
            model.addAttribute("enrolledClass",person.getPreClass().getName());
        }
        session.setAttribute("loggedInPerson", person);
        return "dashboard.html";
    }

    private void logMessages(){
        log.error("Error message from the Dashboard page");
        log.warn("Error message from the Dashboard page");
        log.info("Error message from the Dashboard page");
        log.debug("Error message from the Dashboard page");
        log.trace("Error message from the Dashboard page");

        log.error("defaultPageSize value with @Value annotation is: " +defaultPageSize);
        log.error("successMsg value with @Value annotation is: " +message);

        log.error("defaultPageSize value with Environment is : "+environment.getProperty("preschool.pageSize"));
        log.error("successMsg value with Environment is : "+environment.getProperty("preschool.contact.successMsg"));
        log.error("Java Home environment variable using Environment is : "+environment.getProperty("JAVA_HOME"));

    }

}

