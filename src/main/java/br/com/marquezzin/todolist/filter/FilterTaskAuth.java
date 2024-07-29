package br.com.marquezzin.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.marquezzin.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component // spring gerencia
// OncePerRequestFilter : passa por esse filtro para cada requisição feita
public class FilterTaskAuth extends OncePerRequestFilter {
    @Autowired
    IUserRepository userRepository;

    @Override
    // request é o que ta recebendo e response o que deseja enviar (p/controller)
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();
        // Fazer esse processo so para rota desejada
        // startsWith para pegar o tasks/{id} do update
        if (servletPath.startsWith("/tasks/")) {
            // pegar a autenticação(usuario e senha)
            var authorization = request.getHeader("Authorization");

            var authEncoded = authorization.substring("Basic".length()).trim();// tira o basic dps os espaços
            // Decodifica em um array de bytes
            byte[] authDecode = Base64.getDecoder().decode(authEncoded);
            var authString = new String(authDecode); // array p/ string
            // marquezzin:arrascaeta2003
            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];
            System.out.println(username);
            System.out.println(password);
            // validar usuario
            var user = this.userRepository.findByUsername(username);
            if (user == null) {
                response.sendError(401);

            } else {
                // validar senha
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword().toCharArray());
                if (passwordVerify.verified) {
                    // segue viagem
                    request.setAttribute("idUser", user.getId()); // guarda o id do user(user.getId()) no atributo
                                                                  // idUser

                    filterChain.doFilter(request, response);
                    // é crucial para permitir que a requisição continue seu fluxo normal de
                    // processamento através da cadeia de filtros e, eventualmente, chegue ao
                    // recurso final que deve processá-la. Sem essa chamada, o processamento da
                    // requisição pararia no filtro atual.

                } else {
                    response.sendError(401);
                }
            }

        } else {
            filterChain.doFilter(request, response);
        }
    }
}
