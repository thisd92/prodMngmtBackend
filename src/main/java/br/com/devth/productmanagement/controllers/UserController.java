package br.com.devth.productmanagement.controllers;

import br.com.devth.productmanagement.domain.user.User;
import br.com.devth.productmanagement.services.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    ResponseEntity<User> saveUser(@RequestBody User user){
        if(this.userService.findByUsername(user.getUsername()) != null || this.userService.findByEmail(user.getEmail()) != null) return ResponseEntity.badRequest().build();
        userService.saveUser(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll(){
        List<User> userList = userService.findAll();
        return ResponseEntity.ok().body(userList);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id){
        Optional<User> userOptional = userService.findById(id);
        return userOptional.map(user -> ResponseEntity.ok().body(user)).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user){
        userService.updateUser(id, user);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
