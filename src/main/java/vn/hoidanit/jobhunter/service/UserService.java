package vn.hoidanit.jobhunter.service;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
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

    public ResultPaginationDTO handleGetAllUser(Specification<User> spec, Pageable pageable){
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();
        mt.setPage(pageUser.getNumber() + 1);
        mt.setPageSize(pageUser.getSize());
        
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageUser.getContent());

        return rs;
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

    public User handleGetUserByUserName(String username) {
        return this.userRepository.findByEmail(username);
    } 
    

}
