package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.service.error.IdInvalidException;



@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("users")
    public ResponseEntity<User> createNewUser(
        @RequestBody User postmanUser 
        ) {
            String encryptedPassword = passwordEncoder.encode(postmanUser.getPassword());
            postmanUser.setPassword(encryptedPassword);
        User createUser = this.userService.saveUser(postmanUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
    }

    
    @DeleteMapping("users/{id}")
    public ResponseEntity<String> createNewUser(
        @PathVariable("id") Long id
    ) throws IdInvalidException{
        if(id >= 1500){
            throw new IdInvalidException("Id không lớn hơn hoặc bằng 100");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete User Successfully");
    }

    @GetMapping("users/{id}")
    public ResponseEntity<User> readAUser(
        @PathVariable("id") Long id
    ){
        User getUser = this.userService.handleGetAUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(getUser);
    }

    @GetMapping("users")
    public ResponseEntity<List<User>> readAllUser() {
        List<User> listAllUser = this.userService.handleGetAllUser();
        return ResponseEntity.status(HttpStatus.OK).body(listAllUser);
    }
    
    @PutMapping("users")
    public ResponseEntity<String> updateAUser(@RequestBody User updateUser) {
        this.userService.handleUpdateUser(updateUser);
        return ResponseEntity.status(HttpStatus.OK).body("Update User successfully");
        
    }
    
}
