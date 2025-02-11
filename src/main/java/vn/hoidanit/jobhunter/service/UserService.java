package vn.hoidanit.jobhunter.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {
    public UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Boolean isEmailExist(String email){
        return this.userRepository.existsByEmail(email);
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
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);
        
                // remove sensitive data
        List<ResUpdateUserDTO> listUser = pageUser.getContent()
                .stream().map(item -> new ResUpdateUserDTO(
                        item.getId(),
                        item.getEmail(),
                        item.getName(),
                        item.getGender(),
                        item.getAddress(),
                        item.getAge(),
                        item.getUpdatedAt(),
                        item.getCreatedAt()))
                .collect(Collectors.toList());
        rs.setResult(listUser);

        return rs;
    }

    public User handleUpdateUser(User userRequirement){
        User userUpdate = handleGetAUser(userRequirement.getId());
        if(userUpdate != null){
            userUpdate.setName(userRequirement.getName());
            userUpdate.setEmail(userRequirement.getEmail());
            userUpdate.setAddress(userRequirement.getAddress());
            userUpdate.setGender(userRequirement.getGender());

        }
        this.userRepository.save(userUpdate);
        return userUpdate;  
    }

    public User handleGetUserByUserName(String username) {
        return this.userRepository.findByEmail(username);
    } 
    
    public ResCreateUserDTO convertToResCreateUserDTO(User user){
        ResCreateUserDTO res = new ResCreateUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        return res;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user){
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        res.setUpdatedAt(user.getUpdatedAt());
        return res;
    }


    public void updateUserToken(String token, String email){
        User currentUser = this.handleGetUserByUserName(email);
        if(currentUser != null){
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email){
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }
}
