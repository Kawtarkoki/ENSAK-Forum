package com.forum.service;

import com.forum.model.User;
import com.forum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> getUserByUsername(String username) {
        Optional<User> opuser = userRepository.findByUsername(username);
        opuser.orElseThrow(()-> new UsernameNotFoundException("Not found"));
        return opuser;
    }
    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public List<User> listAll() {
        List<User> users  = userRepository.findAll();
        users.forEach(user -> user.setPassword(null));
        return users;
    }

    public void encodeAndSave(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void deleteByUsername(String username){
        userRepository.deleteByUsername(username);
    }
}
