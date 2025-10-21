package co.edu.uniandes.proyecto1.application.service;

import co.edu.uniandes.proyecto1.domain.exception.BusinessException;
import co.edu.uniandes.proyecto1.domain.model.user.*;
import co.edu.uniandes.proyecto1.domain.repository.UsuarioRepository;
import co.edu.uniandes.proyecto1.domain.service.IdGenerator;

import java.math.BigDecimal;
import java.util.Optional;

public class AuthService {
    private final UsuarioRepository usuarios;

    public AuthService(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
        seedAdminIfMissing();
    }

    public Usuario login(String login, String password) {
        Optional<Usuario> u = usuarios.findByLogin(login);
        if (u.isEmpty() || !u.get().getPassword().equals(password)) {
            throw new BusinessException("Credenciales inválidas");
        }
        return u.get();
    }

    public ClienteComprador registrarCliente(String login, String password) {
        if (usuarios.findByLogin(login).isPresent()) {
            throw new BusinessException("Login ya existente");
        }
        ClienteComprador c = new ClienteComprador(IdGenerator.newId(), login, password, BigDecimal.ZERO);
        usuarios.save(c);
        return c;
    }

    public Organizador registrarOrganizador(String login, String password) {
        if (usuarios.findByLogin(login).isPresent()) {
            throw new BusinessException("Login ya existente");
        }
        Organizador o = new Organizador(IdGenerator.newId(), login, password, BigDecimal.ZERO);
        usuarios.save(o);
        return o;
    }

    private void seedAdminIfMissing() {
        boolean hasAdmin = usuarios.findAll().stream().anyMatch(u -> u.getRole() == Role.ADMIN);
        if (!hasAdmin) {
            Administrador admin = new Administrador(IdGenerator.newId(), "admin", "admin",
                    new BigDecimal("0.10"), new BigDecimal("1.00"));
            usuarios.save(admin);
        }
    }
}


