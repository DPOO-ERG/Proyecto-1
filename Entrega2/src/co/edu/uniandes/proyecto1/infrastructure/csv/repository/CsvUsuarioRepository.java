package co.edu.uniandes.proyecto1.infrastructure.csv.repository;

import co.edu.uniandes.proyecto1.domain.model.user.Usuario;
import co.edu.uniandes.proyecto1.domain.repository.UsuarioRepository;
import co.edu.uniandes.proyecto1.infrastructure.csv.CsvPaths;
import co.edu.uniandes.proyecto1.infrastructure.csv.mappers.UsuarioCsvMapper;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class CsvUsuarioRepository extends AbstractCsvRepository<Usuario> implements UsuarioRepository {
    public CsvUsuarioRepository() {
        super(resolvePath(), new UsuarioCsvMapper(),
                "id,role,login,password,saldoVirtual,porcentajeCargoServicio,cobroFijoEmision",
                Usuario::getId);
    }

    private static Path resolvePath() {
        return CsvPaths.resolve("usuarios.csv");
    }

    @Override
    public Optional<Usuario> findByLogin(String login) {
        return findAll().stream().filter(u -> u.getLogin().equals(login)).findFirst();
    }

    @Override
    public List<Usuario> findAll() { return super.findAll(); }
}


