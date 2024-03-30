package br.com.devth.productmanagement.controllers;

import br.com.devth.productmanagement.domain.user.AuthDTO;
import br.com.devth.productmanagement.domain.user.LoginResponseDTO;
import br.com.devth.productmanagement.domain.user.User;
import br.com.devth.productmanagement.services.AuthService;
import br.com.devth.productmanagement.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/signin")
    public ResponseEntity authenticate(@RequestBody AuthDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

}
