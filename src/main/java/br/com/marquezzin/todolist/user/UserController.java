package br.com.marquezzin.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")

public class UserController {
    @Autowired // spring instaciando o obj userRepository da classe IUserRepository
    private IUserRepository userRepository;

    @PostMapping("/") // metodo do tipo post
    public ResponseEntity create(@RequestBody UserModel userModel) { // @RequestBody: dados do corpo da requisição devem
                                                                     // ser mapeados para um objeto Java.
        var user = this.userRepository.findByUsername(userModel.getUsername());
        if (user != null) {
            // Mensagem de erro
            // Status Code
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe");
        }

        var passwordHashred = BCrypt.withDefaults()
                .hashToString(12, userModel.getPassword().toCharArray());// metodo recebe um array

        userModel.setPassword(passwordHashred);

        var userCreated = this.userRepository.save(userModel); // save é um metodo do da extensão JPA que a interface //
                                                               // // // pegou
        return ResponseEntity.status(HttpStatus.OK).body(userCreated);
    }

}
