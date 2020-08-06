package com.tosiatech.springboot.web.springboottosiatechwebapplication.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tosiatech.springboot.web.springboottosiatechwebapplication.model.Todo;

public interface TodoRepository extends JpaRepository<Todo, Integer> {
	
	List<Todo> findByUser(String user);
	
}
