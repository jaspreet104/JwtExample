package com.example.springjwt.controller;

import com.example.springjwt.JwtUtil;
import com.example.springjwt.models.AuthenticateRequest;
import com.example.springjwt.models.AuthenticationResponse;
import com.example.springjwt.services.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/hello")
    public String hello(Model model) {
        return "Hello World";
    }

    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthToken(@RequestBody AuthenticateRequest authenticateRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticateRequest.getUserName(), authenticateRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new Exception("bad user/psw", e);
        }
        final UserDetails userDetails = myUserDetailService.loadUserByUsername(authenticateRequest.getUserName());
        final String jwtToken = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
    }
}
