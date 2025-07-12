package com.minhalista.listatodo.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minhalista.listatodo.ultils.ultils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.var;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create (@RequestBody TaskModel taskModel, HttpServletRequest request) {
        System.out.println("Chegou no controller" + request.getAttribute("idUser"));
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID)idUser);

        var currentDate = LocalDateTime.now();

        if(currentDate.isAfter(taskModel.getStartAt()) || (currentDate.isAfter(taskModel.getEndAT()))){
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio deve ser maior do que atual");
        }

        if(taskModel.getStartAt().isAfter(taskModel.getEndAT())){
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio deve ser menor da data de termino");
        }

        var task = this.taskRepository.save(taskModel);
        return  ResponseEntity.status(HttpStatus.OK).body(task);

        
    }
    @ControllerAdvice
    public class ExceptionHandlerController {

        @ExceptionHandler({HttpMessageNotReadableException.class})
        public ResponseEntity handleDateParseError(Exception ex) {
            if (ex.getCause() instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Erro ao converter a data. Use o formato: yyyy-MM-dd'T'HH:mm:ss");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno no servidor.");
        }
    }


    @GetMapping("/")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
    public List<TaskModel> list(HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID)idUser);
        return tasks;
    }

    @PutMapping("/{id}")
    public String putMethodName(@PathVariable String id, @RequestBody String entity) {
        //TODO: process PUT request
        
        return entity;
    }
    public ResponseEntity update(@RequestBody TaskModel taskModel,@PathVariable UUID id,HttpServletRequest request){

        var task = this.taskRepository.findById(id).orElse(null);

        if(task == null){
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada");
        }

        var idUser = request.getAttribute("idUser");

        if(task.getIdUser().equals(idUser)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não tem permissão para alterar essa tarefa.");

        }

        ultils.copyNonNullProperties(taskModel, task);
        var taskUpdated = this.taskRepository.save(task);
       return  ResponseEntity.ok().body(this.taskRepository.save(taskUpdated));
  
    }

}
