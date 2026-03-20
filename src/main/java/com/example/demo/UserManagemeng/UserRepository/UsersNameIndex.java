package com.example.demo.UserManagemeng.UserRepository;

import com.example.demo.UnitModel.UserModel.NameIndex;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersNameIndex extends JpaRepository<NameIndex, String> {
}
