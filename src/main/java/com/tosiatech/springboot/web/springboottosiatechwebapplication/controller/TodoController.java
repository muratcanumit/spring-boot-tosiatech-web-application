package com.tosiatech.springboot.web.springboottosiatechwebapplication.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tosiatech.springboot.web.springboottosiatechwebapplication.model.Todo;
import com.tosiatech.springboot.web.springboottosiatechwebapplication.service.TodoRepository;

@Controller
public class TodoController {

	/*@Autowired
	TodoService todoService;*/
	
	@Autowired
	TodoRepository repository;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
	
	private String getLoggedInUserName(ModelMap model) {
		Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if ( principle instanceof UserDetails) {
			return ((UserDetails) principle).getUsername();
		}
		
		return principle.toString();
	}
	
	@RequestMapping(value="/list-todos", method = RequestMethod.GET)
	public String showTodosList (ModelMap model) {
		
		String name = getLoggedInUserName(model);
		model.put("todos", repository.findByUser(name));
		//model.put("todos", todoService.retrieveTodos(name));
		return "list-todos";
	}

	
	@RequestMapping(value="/add-todo", method = RequestMethod.GET)
	public String showAddTodo (ModelMap model) {
		
		model.addAttribute("todo", new Todo(0, getLoggedInUserName(model), "Default Desc", new Date(), false));
		return "todo";
	}
	
	@RequestMapping(value="/delete-todo", method = RequestMethod.GET)
	public String deleteTodo (@RequestParam int id) {
				
		repository.deleteById(id);
		//todoService.deleteTodo(id);
		return "redirect:/list-todos";
	}
	
	@RequestMapping(value="/add-todo", method = RequestMethod.POST)
	public String addTodo (ModelMap model, @Valid Todo todo, BindingResult result) {
		
		if(result.hasErrors()) {
			return "todo";
		}
		
		todo.setUser(getLoggedInUserName(model));
		repository.save(todo);
		//todoService.addTodo(getLoggedInUserName(model), todo.getDesc(), todo.getTargetDate() , false);
		
		return "redirect:/list-todos";
	}
	
	@RequestMapping(value="/update-todo", method = RequestMethod.GET)
	public String showUpdateTodoPage (@RequestParam int id, ModelMap model) {
		
		Todo todo = repository.findById(id).get();
		//Todo todo = todoService.retrieveTodo(id);
		model.put("todo", todo);
		return "todo";
	}
	
	@RequestMapping(value="/update-todo", method = RequestMethod.POST)
	public String updateTodo (ModelMap model, @Valid Todo todo, BindingResult result) {

		if(result.hasErrors()) {
			return "todo";
		}
		todo.setUser(getLoggedInUserName(model));
		
		repository.save(todo);
		//todoService.updateTodo(todo);
		
		return "redirect:/list-todos";
	}
	
}
