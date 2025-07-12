package com.minhalista.listatodo.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;



public interface IUserRepository extends JpaRepository <userModel, UUID> {
    userModel findByUsername(String username);
    
}
