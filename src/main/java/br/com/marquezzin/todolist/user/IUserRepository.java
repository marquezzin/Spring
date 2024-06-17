package br.com.marquezzin.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

//contrato, métodos(mas nao sua implementação)
public interface IUserRepository extends JpaRepository<UserModel, UUID> {
    // pega os metodos do JPA repository
    UserModel findByUsername(String username);
}
