package com.example.todo.controller;

import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*")
public class TodoController {

    private final TodoRepository todoRepository;

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping
    public List<Todo> getAll() {
        return todoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getById(@PathVariable Long id) {
        return todoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Todo create(@RequestBody Todo todo) {
        todo.setId(null);
        return todoRepository.save(todo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> update(@PathVariable Long id, @RequestBody Todo updated) {
        return todoRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(updated.getTitle());
                    existing.setCompleted(updated.isCompleted());
                    return ResponseEntity.ok(todoRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!todoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        todoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

