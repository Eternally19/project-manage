package com.hciot.service;

import com.hciot.NotFoundException;
import com.hciot.dao.UserRepository;
import com.hciot.entity.User;
import com.hciot.util.MD5Utills;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User getUser(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    @Override
    public Page<User> listUser(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public List<User> listUser() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public User updateUser(Long id, User user) {
        User u = userRepository.getOne(id);
        if (u == null){
            throw new NotFoundException("不存在此用户");
        }
        BeanUtils.copyProperties(user,u);
        return userRepository.save(u);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, password);
        return user;
    }
}
