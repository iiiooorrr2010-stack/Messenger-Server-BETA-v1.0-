package com.example.demo.UserManagemeng.UserRepository;

import com.example.demo.UnitModel.UserModel.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, String> {
}