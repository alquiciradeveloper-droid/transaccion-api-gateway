package aalquicira.api.transaccion.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import aalquicira.api.transaccion.Entity.Transaccion;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    
    @Transactional
    @Modifying
    @Query("UPDATE Transaccion t SET t.estatus = 'Cancelada' WHERE t.id = :id AND t.referencia = :referencia")
    int cancelarTransaccion(@Param("id") Long id, @Param("referencia") String referencia);

    // Consulta con Paginación JPA nativa de Spring Data
    Page<Transaccion> findAll(Pageable pageable);
}
