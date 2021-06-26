package com.hciot.service;

import com.hciot.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    User saveUser(User user);

    User getUser(Long id);

    User getUserByUsername(String username);

    Page<User> listUser(Pageable pageable);

    List<User> listUser();

    User updateUser(Long id,User user);

    void deleteUser(Long id);

    User checkUser(String username, String password);
}
