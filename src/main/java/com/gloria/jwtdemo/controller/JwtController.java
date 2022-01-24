package com.gloria.jwtdemo.controller;

import com.gloria.jwtdemo.model.JwtRequest;
import com.gloria.jwtdemo.model.JwtResponse;
import com.gloria.jwtdemo.service.CustomUserDetailService;
import com.gloria.jwtdemo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class JwtController {

    //inject classes


    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/generateToken")//in the Config, this is the endpoint that is allowed without authentication
    public ResponseEntity<JwtResponse> generateToken(@RequestBody JwtRequest jwtRequest){

        //authenticate the user
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));

        //check user existence from CustomUserDetails
        UserDetails userDetails = customUserDetailService.loadUserByUsername(jwtRequest.getUsername());

        //generate token
        String jwtToken = jwtUtil.generateToken(userDetails);

        JwtResponse jwtResponse = new JwtResponse(jwtToken);
        //return ResponseEntity.ok(jwtResponse);
        //OR
        return new ResponseEntity<JwtResponse>(jwtResponse, HttpStatus.OK);
    }
}
