package com.minhalista.listatodo.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.minhalista.listatodo.user.IUserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter{

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                    var servletPath = request.getServletPath();

                    if(servletPath.startsWith("/tasks/")){
                          //pegar o autenticação (usuário senha)
                var authorization = request.getHeader("Authorization");
                
                var autEncoded = authorization.substring("Basic".length()).trim();

                byte[] authDecoder = Base64.getDecoder().decode(autEncoded);

                var authString = new String(authDecoder);    

                System.out.println("Authorization");
                System.out.println(authString);
                
                String[] credentials = authString.split(":");
                String username = credentials [0];
                String password = credentials [1];

                //validar o usuário
                var user = this.userRepository.findByUsername(username);
                if(user == null){
                    response.sendError(401);
                } else {
                    var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                    if (passwordVerify.verified) {
                        request.setAttribute("idUser", user.getId());
                        filterChain.doFilter(request, response);
                    } else {
                        response.sendError(401);
                    }
                }
        }else {
            filterChain.doFilter(request, response);
        } 
                
              
    }

}
