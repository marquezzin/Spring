package br.com.marquezzin.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;
//ID
//Usuario(ID_Usuario)
//Descricao
//Titulo
//Data de inicio
//Data de termino
//Prioridade

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_tasks")
public class TaskModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String description;

    @Column(length = 50) // limitando a 50 caracteres
    private String title;
    private LocalDateTime startedAt;
    private LocalDateTime endAt;
    private String priority;
    private UUID idUser;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
