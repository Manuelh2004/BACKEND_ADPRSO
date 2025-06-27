package backend.backend_adprso.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.backend_adprso.Entity.Usuario.UsuarioEntity;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    @Query(value = "SELECT u.* FROM usuario u WHERE u.tipus_id = :tipus_id", nativeQuery = true)
    List<UsuarioEntity> findByTipoUsuario_Tipus_id(@Param("tipus_id") Long tipus_id);

    @Query("SELECT u FROM UsuarioEntity u WHERE LOWER(TRIM(BOTH FROM u.usr_email)) = LOWER(TRIM(BOTH FROM :email))")
    Optional<UsuarioEntity> findByUsrEmail(String email);

    @Query("SELECT u FROM UsuarioEntity u WHERE LOWER(u.usr_nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) OR LOWER(u.usr_apellido) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<UsuarioEntity> buscarPorNombre(@org.springframework.data.repository.query.Param("nombre") String nombre);
}