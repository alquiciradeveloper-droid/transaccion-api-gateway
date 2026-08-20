package aalquicira.api.transaccion.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import aalquicira.api.transaccion.Entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsuario(String usuario);

}
