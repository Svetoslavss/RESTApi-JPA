package com.academy.project.SomeProject.service;

import com.academy.project.SomeProject.model.User;
import com.academy.project.SomeProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

// Service for CRUD operations and validation

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User add(User user){
        return repository.save(user);
    }

    public List<User> getByUsername(String username){
        return  repository.findByUsername(username);
    }

    public boolean deleteById(Long id){
       Optional<User> existingUser = repository.findById(id);
       if (existingUser.isPresent()){
           repository.deleteById(id);
           return true;
       } else {
           return false;
       }
    }

    public Optional<User> findById(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return repository.findById(id);
    }

    public List<User> findAll(){
        return repository.findAll();
    }

    public List<User> deleteAll() {
        List<User> user = repository.findAll();
        repository.deleteAll();
        return user;
    }
}
