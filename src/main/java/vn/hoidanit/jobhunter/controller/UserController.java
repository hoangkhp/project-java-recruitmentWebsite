package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import java.util.*;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;




@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<ResCreateUserDTO> createNewUser(
        @Valid @RequestBody User postmanUser        
        ) throws IdInvalidException {
            boolean isEmailExist = this.userService.isEmailExist(postmanUser.getEmail());
            if(isEmailExist){
                throw new IdInvalidException(
                    "Email " +postmanUser.getEmail() + "is already exist, please using another email!!!"
                );
            }
            String encryptedPassword = passwordEncoder.encode(postmanUser.getPassword());
            postmanUser.setPassword(encryptedPassword);
        User createUser = this.userService.saveUser(postmanUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(createUser));
    }

    
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteAUser(
        @PathVariable("id") Long id
    ) throws IdInvalidException{
        User currentUser = this.userService.handleGetAUser(id);
        if(currentUser == null){
            throw new IdInvalidException("Don't have User have id:" + id);
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete User Successfully");
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ResUpdateUserDTO> readAUser(
        @PathVariable("id") Long id
    )throws IdInvalidException{
        User currentUser = this.userService.handleGetAUser(id);
        if(currentUser == null){
            throw new IdInvalidException("Don't have User have id:" + id);
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUpdateUserDTO(currentUser));
    }

    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> readAllUser(
        @Filter Specification<User> spec,
        Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleGetAllUser(spec, pageable));
    }
    
    @PutMapping("/users")
    public ResponseEntity<ResUpdateUserDTO> updateAUser(@RequestBody User updateUser) 
    throws IdInvalidException {
        User currentUser = this.userService.handleUpdateUser(updateUser);
        if(currentUser == null){
            throw new IdInvalidException("Don't have User have id:" + updateUser.getId());
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUpdateUserDTO(currentUser));
    }
    
}
