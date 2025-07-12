package com.minhalista.listatodo.user;
/**
 * Modificador 
 * public - todos pode ter acesso
 * private - tem retrições de acesso
 * protected - esta na estrutura do  pacote 
*/

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.var;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * definindo 
 * class
 * interface
 * enum
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;



     
            
        @PostMapping("/")
        public ResponseEntity create(@RequestBody userModel userModel){

          var user = this.userRepository.findByUsername(userModel.getUsername());

          if (user != null) {
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário Já existe");
          }

          var passwordHashred = BCrypt.withDefaults().hashToString(12,userModel.getPassword().toCharArray());

          userModel.setPassword(passwordHashred);

          var userCreated = this.userRepository.save(userModel);
          return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);


        }

    
}

/***
     * Método de acesso do HTTP
     * Get - Buscar uma informação
     * post - adicionar um dado / informação
     * put - alterar um dado/info
     * delete - Remover um dado
     * patch - Alterar  somente uma parte da infor/dado
     * 
     */
    /**
     * String (texto)
     * integer (int) Numeros inteiro
     * Double (double) numero 0.0000
     * flout (flout) numero 0.000
     * char (AC*)
     * date (data)
     * void quando não quer retorno
     ** */

     /*
      * no create o @RequestBody para mostra que a info será do Body
       //para print a tela
            System.out.println(userModel.getUsername());
      */
       
