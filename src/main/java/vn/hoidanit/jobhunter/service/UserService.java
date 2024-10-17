package vn.hoidanit.jobhunter.service;

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

    public User handleGetAllUser(Long id){
        return this.userRepository.findById(id).get();
    }
}
