package com.hciot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "t_user")
public class User {

    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "姓名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    //@NotBlank(message = "身份不能为空")
    private Integer type;
    @NotBlank(message = "邮箱地址不能为空")
    private String email;
    @NotBlank(message = "联系电话不能为空")
    private String phone;


    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade={CascadeType.ALL})
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade={CascadeType.ALL})
    private List<Process> processList = new ArrayList<>();

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonBackReference
    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    @JsonBackReference
    public List<Process> getProcessList() {
        return processList;
    }

    public void setProcessList(List<Process> processList) {
        this.processList = processList;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", type=" + type +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
