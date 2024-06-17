package br.com.marquezzin.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        System.out.println("Chegou na controller ");
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);

        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(taskModel.getStartedAt()) || (currentDate.isAfter(taskModel.getEndAt()))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de início/data de término deve ser maior que a data atual");
        }
        if (taskModel.getStartedAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de início deve ser menor que a data de término");

        }
        var task = this.taskRepository.save(taskModel); // método da interface
        return ResponseEntity.status(HttpStatus.OK).body(task);

    }
}
