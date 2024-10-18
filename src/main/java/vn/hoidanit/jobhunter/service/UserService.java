package vn.hoidanit.jobhunter.service;

import java.util.*;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {
    public UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user){
         return this.userRepository.save(user);
    }

    public void handleDeleteUser(Long id){
         this.userRepository.deleteById(id);
    }

    public User handleGetAUser(Long id){
        Optional<User> userOptional = this.userRepository.findById(id);
        if(userOptional.isPresent()){
            return userOptional.get();
        }
        return null;
    }   

    public List<User> handleGetAllUser(){
        return this.userRepository.findAll();
    }

    public User handleUpdateUser(User userRequirement){
        User userUpdate = handleGetAUser(userRequirement.getId());
        if(userUpdate != null){
            userUpdate.setName(userRequirement.getName());
            userUpdate.setEmail(userRequirement.getEmail());
            userUpdate.setPassword(userRequirement.getPassword());
            userUpdate = this.userRepository.save(userUpdate);
        }
        return userUpdate;  
    }

}
