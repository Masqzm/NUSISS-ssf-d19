package ssf.day19.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
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

        mav.setViewName("listing");
        mav.addObject("tasksList", tasksList);

        return mav;
    }

    // For filtering of tasks
    @PostMapping("/listing")
    public ModelAndView getListing(@RequestParam String qFilter) {
        ModelAndView mav = new ModelAndView();

        List<Task> tasksList = taskSvc.getFilteredStatusTasks(qFilter);

        mav.setViewName("listing");
        mav.addObject("tasksList", tasksList);

        return mav;
    }

    @GetMapping("/add")
    public ModelAndView getAddTaskForm() {
        ModelAndView mav = new ModelAndView();

        mav.setViewName("add");
        mav.addObject("task", new Task());

        return mav;
    }

    
    @PostMapping("/add")
    public ModelAndView addTask(@Valid Task task, BindingResult bindings) {
        ModelAndView mav = new ModelAndView();

        // Go back to original page if form has invalid inputs
        if(bindings.hasErrors())
        {
            mav.setViewName("add");

            // if need to clear form
            //mav.setViewName("redirect:/add");

            return mav;
        }

        taskSvc.addTask(task);

        mav.setViewName("redirect:/listing");
        //mav.addObject("tasksList", tasksList);

        return mav;
    }
}
