package br.com.marquezzin.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.marquezzin.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);

        // Validação Time
        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(taskModel.getStartedAt()) || (currentDate.isAfter(taskModel.getEndAt()))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de início/data de término deve ser maior que a data atual");
        }
        if (taskModel.getStartedAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de início deve ser menor que a data de término");

        }
        var task = this.taskRepository.save(taskModel); // método da interface, salva a task criada
        return ResponseEntity.status(HttpStatus.OK).body(task);

    }

    @GetMapping("/")
    // listar as tasks de um usuario
    public List<TaskModel> list(HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID) idUser);
        return tasks;
    }

    // http://localhost:8080/tasks/313132
    @PutMapping("/{id}")
    public TaskModel update(@RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request) {

        var task = this.taskRepository.findById(id).orElse(null);
        // nao nulo do source é o que foi alterado
        // e ele vai pro target
        Utils.copyNonNullProperties(taskModel, task);

        return this.taskRepository.save(task);

    }

}
