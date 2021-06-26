package com.hciot.dao;

import com.hciot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsername(String username);

    User findByUsernameAndPassword(String username,String password);

}
