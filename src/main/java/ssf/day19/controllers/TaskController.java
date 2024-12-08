package ssf.day19.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ssf.day19.models.Task;
import ssf.day19.services.TaskService;

@Controller
@RequestMapping
public class TaskController {
    @Autowired
    TaskService taskSvc;

    // todo: accept query params for status filter, if null, status filter = all
    @GetMapping("/listing")
    public ModelAndView getListing() {
        ModelAndView mav = new ModelAndView();

        List<Task> tasksList = taskSvc.getAllTasks();

        mav.addObject("tasksList", tasksList);

        return mav;
    }

    // For filtering of tasks
    @PostMapping("/listing")
    public ModelAndView getListing(@RequestParam String qFilter) {
        ModelAndView mav = new ModelAndView();

        List<Task> tasksList = taskSvc.getFilteredStatusTasks(qFilter);

        mav.addObject("tasksList", tasksList);

        return mav;
    }
}
