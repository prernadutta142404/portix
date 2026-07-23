package com.portix.portix.service;

import com.portix.portix.entity.User;

public interface UserService {

    User registerUser(User user);

    User loginUser(String email, String password);
    
    User findById(Long id);
    
    

}
