package co.edu.uniandes.proyecto1.domain.repository;

import co.edu.uniandes.proyecto1.domain.model.user.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Optional<Usuario> findByLogin(String login);
    Optional<Usuario> findById(String id);
    List<Usuario> findAll();
    Usuario save(Usuario usuario);
    void update(Usuario usuario);
}


