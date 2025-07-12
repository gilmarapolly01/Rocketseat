package com.minhalista.listatodo.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
//Banco de dado usando entidade
@Entity(name = "tb_users")
public class userModel {

   @Id
   @GeneratedValue(generator = "UUID")
   private UUID id;

   @Column(unique = true)
   private String username;
   private String name;
   private String password;

   @CreationTimestamp
   private LocalDateTime createdArt;

  
   //metodo gatters (Buscar informações ) setters (Atualizar um valor de um class)
}
