package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class UserController {
    private UserService userService;
    

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public User createNewUser(
        @RequestBody User postmanUser 
        ) {
        User createUser = this.userService.saveUser(postmanUser);
        return createUser;
    }

    @DeleteMapping("/user/{id}")
    public String createNewUser(
        @PathVariable("id") Long id
    ){
        this.userService.handleDeleteUser(id);
        return "delete User";
    }

    @GetMapping("/user/{id}")
    public User readAUser(
        @PathVariable("id") Long id
    ){
        User getUser = this.userService.handleGetAUser(id);
        return getUser;
    }

    @GetMapping("/user")
    public List<User> readAllUser() {
        List<User> listAllUser = this.userService.handleGetAllUser();
        return listAllUser;
    }
    
    @PutMapping("/user")
    public String updateAUser(@RequestBody User updateUser) {
        this.userService.handleUpdateUser(updateUser);
        return "update User thành công";
        
    }
    
}
