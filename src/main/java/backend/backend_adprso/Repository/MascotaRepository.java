package backend.backend_adprso.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.backend_adprso.Entity.Mascota.MascotaEntity;

@Repository
public interface MascotaRepository extends JpaRepository<MascotaEntity, Long> {

    @Query("SELECT m FROM MascotaEntity m WHERE m.masc_estado = :estado")
    List<MascotaEntity> findByMascEstado(@Param("estado") Integer estado);

     // Filtrar por sexo (sex_id)
    @Query("SELECT m FROM MascotaEntity m WHERE m.sexo.sex_id = :sexId")
    List<MascotaEntity> findBySexo(@Param("sexId") Long sexId);

    // Filtrar por tamaño (tam_id)
    @Query("SELECT m FROM MascotaEntity m WHERE m.tamanio.tam_id = :tamId")
    List<MascotaEntity> findByTamanio(@Param("tamId") Long tamId);

    // Filtrar por nivel de energía (nien_id)
    @Query("SELECT m FROM MascotaEntity m WHERE m.nivel_energia.nien_id = :nienId")
    List<MascotaEntity> findByNivelEnergia(@Param("nienId") Long nienId);

    // Filtrar por tipo de mascota (tipma_id)
    @Query("SELECT m FROM MascotaEntity m WHERE m.tipo_mascota.tipma_id = :tipmaId")
    List<MascotaEntity> findByTipoMascota(@Param("tipmaId") Long tipmaId);

    // Filtrar por múltiples criterios (opcional, si quieres filtrar por todos juntos)
    @Query("SELECT m FROM MascotaEntity m WHERE " +
            "(:sexId IS NULL OR m.sexo.sex_id = :sexId) AND " +
            "(:tamId IS NULL OR m.tamanio.tam_id = :tamId) AND " +
            "(:nienId IS NULL OR m.nivel_energia.nien_id = :nienId) AND " +
            "(:tipmaId IS NULL OR m.tipo_mascota.tipma_id = :tipmaId) AND " +
            "m.masc_estado = 1")
    List<MascotaEntity> findByFilters(@Param("sexId") Long sexId,
                                      @Param("tamId") Long tamId,
                                      @Param("nienId") Long nienId,
                                      @Param("tipmaId") Long tipmaId);

       @Query("SELECT m FROM MascotaEntity m " +
           "LEFT JOIN FETCH m.gustoMascotaList gm " +
           "LEFT JOIN FETCH gm.gust_id g")
    List<MascotaEntity> findAllWithGustos();

   @Query("SELECT m FROM MascotaEntity m LEFT JOIN m.imagenes i WHERE m.masc_id = :mascotaId")
    List<MascotaEntity> findMascotaWithImagenes(@Param("mascotaId") Long mascotaId);   
    
    @Query("SELECT m FROM MascotaEntity m WHERE LOWER(m.masc_nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<MascotaEntity> buscarPorNombre(@Param("nombre") String nombre);
}
