package backend.backend_adprso.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.backend_adprso.Entity.Evento.EventoInteresadoEntity;

@Repository
public interface EventoInteresadoRepository extends JpaRepository<EventoInteresadoEntity, Long>{
    
}
