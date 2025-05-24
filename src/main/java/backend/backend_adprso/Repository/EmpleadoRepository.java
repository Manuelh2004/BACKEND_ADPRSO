package backend.backend_adprso.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import backend.backend_adprso.Entity.Usuario.EmpleadoEntity;

@Repository
public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity, Long>{
    
    @Query("SELECT e FROM EmpleadoEntity e WHERE e.usr_email = :email")
    EmpleadoEntity findByUsrEmail(@Param("email") String email);
    
}

