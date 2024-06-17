package br.com.marquezzin.todolist.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data // getter e setters dos atributos
@Entity(name = "tb_users") // entidade eh uma tabela no bd
public class UserModel {

    @Id
    @GeneratedValue(generator = "UUID") // jpa gera as chaves
    private UUID id;

    @Column(name = "usuario", unique = true) // atributo username mapeado como usuario no BD, nome unico
    private String username;
    private String name;
    private String password;

    @CreationTimestamp // informação do tempo em que o dado foi gerado
    private LocalDateTime createdAt;

}
