package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;


@RestController
public class UserController {
    private UserService userService;
    

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/createUser")
    public User createNewUser(
        @RequestBody User postmanUser 
        ) {
        User createUser = this.userService.saveUser(postmanUser);
        return createUser;
    }
    
}
