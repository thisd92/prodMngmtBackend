package br.com.devth.productmanagement.services;

import br.com.devth.productmanagement.model.User;
import br.com.devth.productmanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user){
        user.encryptPass();
        return userRepository.save(user);
    }
}
