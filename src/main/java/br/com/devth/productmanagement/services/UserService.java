package br.com.devth.productmanagement.services;

import br.com.devth.productmanagement.domain.user.User;
import br.com.devth.productmanagement.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User saveUser(User user){
        encryptPass(user);
        return userRepository.save(user);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public UserDetails findByUsername(String username){ return userRepository.findByUsername(username); }

    public UserDetails findByEmail(String email){ return userRepository.findByEmail(email); }

    public User updateUser(Long id, User user) {
        User entity = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        updateData(entity, user);
        return userRepository.save(entity);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private void encryptPass(User user) {
        String encryptedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPass);
    }

    public void updateData(User entity,  User user) {
        if (user.getName() != null) entity.setName(user.getName());
        if (user.getEmail() != null) entity.setEmail(user.getEmail());
    }
}
