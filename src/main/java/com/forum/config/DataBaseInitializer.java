package com.forum.config;


import com.forum.model.role.UserRole;
import com.forum.model.User;
import com.forum.repository.UserRepository;
import com.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataBaseInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count() == 0){
            User admin = new User("ismail","Ismail","ARZIKI", "useruser", UserRole.ADMIN, true);
            User user = new User("kawtar","Kawtar","BAKOUR", "useruser", UserRole.USER, true);
            User user2 = new User("aniss","Aniss","Moumen", "useruser", UserRole.ENTREPRENEUR, true);

            userService.encodeAndSave(admin);
            userService.encodeAndSave(user);
            userService.encodeAndSave(user2);
        }
    }
}
