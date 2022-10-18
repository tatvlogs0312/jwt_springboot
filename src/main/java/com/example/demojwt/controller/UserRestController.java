package com.example.demojwt.controller;

import com.example.demojwt.entities.User;
import com.example.demojwt.service.JwtService;
import com.example.demojwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class UserRestController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUser(){
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getUserByID(@PathVariable("id") int id){
        User user = userService.findById(id);
        if(user != null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<?> addUser(@RequestBody User user){
        if(userService.addUser(user)){
            return new ResponseEntity<>("Created !", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("User existed" , HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(HttpServletRequest request, @RequestBody User user){
        String result = "";
        HttpStatus httpStatus = null;

        try{
            if(userService.checkLogin(user)){
                result = jwtService.generateTokenLogin(user.getUsername());
                httpStatus = HttpStatus.OK;
            } else {
                result = "Wrong userId or password";
                httpStatus = HttpStatus.BAD_REQUEST;
            }
        } catch (Exception e){
            result = "Server Error";
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(result, httpStatus);
    }
}
