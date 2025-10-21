package co.edu.uniandes.proyecto1.domain.model.user;

import java.util.Objects;

public abstract class Usuario {
    private final String id;
    private final String login;
    private String password;
    private final Role role;

    protected Usuario(String id, String login, String password, Role role) {
        this.id = Objects.requireNonNull(id, "id");
        this.login = Objects.requireNonNull(login, "login");
        this.password = Objects.requireNonNull(password, "password");
        this.role = Objects.requireNonNull(role, "role");
    }

    public String getId() { return id; }
    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }

    public void setPassword(String newPassword) {
        this.password = Objects.requireNonNull(newPassword, "newPassword");
    }
}


