package com.example.demo.UserManagement.UserRepository;

import com.example.demo.UnitModel.UserModel.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, String> {
    User findByNickname(String name);
}