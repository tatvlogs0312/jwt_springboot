package com.example.demojwt.service;

import com.example.demojwt.entities.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    public static List<User> listUser = new ArrayList<User>();

    static {
        User userKai = new User(1, "kai", "123456");
        userKai.setRoles(List.of("ROLE_ADMIN","ROLE_USER"));

        User userSena = new User(2, "sena", "123456");
        userSena.setRoles(List.of("ROLE_USER"));

        listUser.add(userKai);
        listUser.add(userSena);
    }

    public List<User> findAll() {
        return listUser;
    }

    public User findById(int id){
        for (User user:listUser){
            if(user.getId() == id){
                return user;
            }
        }

        return null;
    }

    public boolean addUser(User user){
        for (User userExist : listUser){
            if(user.getId() == userExist.getId() || StringUtils.equals(user.getUsername(), userExist.getUsername())){
                return false;
            }
        }

        user.setRoles(List.of("ROLE_USER"));
        listUser.add(user);
        return true;
    }

    public void deleteUser(int id){
        listUser.removeIf(user -> user.getId() == id);
    }

    public User loadUserByUsername(String username){
        for(User user:listUser){
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

    public boolean checkLogin(User user){
        for (User userExist : listUser){
            if(StringUtils.equals(user.getUsername(),userExist.getUsername())
                    && StringUtils.equals(user.getPassword(),userExist.getPassword())){
                return true;
            }
        }

        return false;
    }
}
