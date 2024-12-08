package ssf.day19.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import ssf.day19.config.Constants;
import ssf.day19.models.Task;
import ssf.day19.services.TaskService;

@Controller
@RequestMapping
public class TaskController {
    @Autowired
    TaskService taskSvc;

    // todo: accept query params for status filter, if null, status filter = all
    @GetMapping("/listing")
    public ModelAndView getListing(HttpSession sess) {
        ModelAndView mav = new ModelAndView();

        // If user not logged in
        if(sess.getAttribute(Constants.SESS_ATTR_USER) == null) {
            mav.setViewName("refused");

            return mav;
        }

        // Reset task to be updated
        sess.setAttribute(Constants.SESS_ATTR_TASK_UPDATE, null);

        List<Task> tasksList = taskSvc.getAllTasks();

        mav.setViewName("listing");
        mav.addObject("tasksList", tasksList);

        return mav;
    }

    // For filtering of tasks
    @PostMapping("/listing")
    public ModelAndView postListingQuery(@RequestParam String qFilter) {
        ModelAndView mav = new ModelAndView();

        List<Task> tasksList = taskSvc.getFilteredStatusTasks(qFilter);

        mav.setViewName("listing");
        mav.addObject("tasksList", tasksList);

        return mav;
    }

    @GetMapping("/add")
    public ModelAndView getAddTaskForm() {
        ModelAndView mav = new ModelAndView();

        System.out.println("add/update page");

        mav.setViewName("add");
        mav.addObject("task", new Task());        

        return mav;
    }
    
    @PostMapping("/update")
    public ModelAndView postUpdateTaskForm(@RequestParam String taskStr, HttpSession sess) {
        ModelAndView mav = new ModelAndView();

        Task taskUpdate = Task.jsonToTask(taskStr);

        mav.setViewName("add");
        mav.addObject("task", taskUpdate);

        // Store task to be updated into sess
        sess.setAttribute(Constants.SESS_ATTR_TASK_UPDATE, taskUpdate);

        return mav;
    }
    
    @PostMapping("/delete")
    public ModelAndView deleteTask(@RequestParam String id) {

        taskSvc.deleteTask(id);
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/listing");

        return mav;
    }

    // Best to separate add and update with PUT ?
    @PostMapping("/add")
    public ModelAndView addTask(@Valid Task task, BindingResult bindings, HttpSession sess) {
        ModelAndView mav = new ModelAndView();

        // For task update
        Task taskBeforeUpdate = (Task) sess.getAttribute(Constants.SESS_ATTR_TASK_UPDATE);

        // Set hidden task attributes (others modified/set thru form/model )
        if(taskBeforeUpdate != null) {
            task.setId(taskBeforeUpdate.getId());
            task.setCreatedAt(taskBeforeUpdate.getCreatedAt());
        }

        // Go back to original page if form has invalid inputs
        if(bindings.hasErrors())
        {
            // Check if its an update (bypasses valid checks when updating) 
            // - by right need to set proper logic here
            // - NOTE: can use bindings.hasFieldErrors("dueDate") to check each field error
            if(task.getId() == null)
            {
                mav.setViewName("add");

                // NOTE: if need to clear form use redirect
                //mav.setViewName("redirect:/add");

                return mav;
            }
        }

        taskSvc.addTask(task);

        mav.setViewName("redirect:/listing");

        return mav;
    }
}
