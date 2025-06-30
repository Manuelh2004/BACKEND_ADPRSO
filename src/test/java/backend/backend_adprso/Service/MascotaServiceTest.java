package backend.backend_adprso.Service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import backend.backend_adprso.Entity.Items.GustoEntity;
import backend.backend_adprso.Entity.Mascota.MascotaEntity;
import backend.backend_adprso.Repository.GustoMascotaRepository;
import backend.backend_adprso.Repository.GustoRepository;
import backend.backend_adprso.Repository.ImagenRepository;
import backend.backend_adprso.Repository.MascotaRepository;
import backend.backend_adprso.Service.Cloudinary.CloudinaryService;
import backend.backend_adprso.Service.Mascota.MascotaService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MascotaServiceTest {

    @Mock
    private MascotaRepository mascotaRepository;
    @Mock
    private GustoRepository gustoRepository;
    @Mock
    private GustoMascotaRepository gustoMascotaRepository;
    @Mock
    private ImagenRepository imagenRepository;
    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private MascotaService mascotaService;

    @Test
    void testListarMascotas() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setMasc_nombre("Firulais");

        Mockito.when(mascotaRepository.findAll()).thenReturn(List.of(mascota));

        List<MascotaEntity> resultado = mascotaService.listarMascotas();
        assertEquals(1, resultado.size());
        assertEquals("Firulais", resultado.get(0).getMasc_nombre());
    }

    @Test
    void testObtenerMascotaPorId_Existe() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setMasc_id(1L);
        mascota.setMasc_nombre("Luna");
        mascota.setGustoMascotaList(List.of());
        mascota.setImagenes(List.of());

        Mockito.when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        Mockito.when(imagenRepository.findByMascotaId(1L)).thenReturn(List.of());

        Optional<MascotaEntity> resultado = mascotaService.ObtenerMascotaPorId(1L);
        assertTrue(resultado.isPresent());
        assertEquals("Luna", resultado.get().getMasc_nombre());
    }

    @Test
    void testObtenerMascotaPorId_NoExiste() {
        Mockito.when(mascotaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<MascotaEntity> resultado = mascotaService.ObtenerMascotaPorId(99L);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testRegistrarMascota() throws IOException {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setMasc_nombre("Rocky");

        List<Long> gustosIds = List.of(1L);
        GustoEntity gusto = new GustoEntity();
        gusto.setGust_id(1L);

        Mockito.when(mascotaRepository.save(Mockito.any())).thenAnswer(i -> i.getArgument(0));
        Mockito.when(gustoRepository.findAllById(gustosIds)).thenReturn(List.of(gusto));

        MascotaEntity resultado = mascotaService.RegistrarMascota(mascota, gustosIds, new ArrayList<>());

        assertEquals("Rocky", resultado.getMasc_nombre());
        assertEquals(1, resultado.getMasc_estado());
        assertNotNull(resultado.getMasc_fecha_registro());
    }

    @Test
    void testCambiarEstadoMascota() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setMasc_id(5L);
        mascota.setMasc_estado(1);

        Mockito.when(mascotaRepository.findById(5L)).thenReturn(Optional.of(mascota));
        Mockito.when(mascotaRepository.save(Mockito.any())).thenAnswer(i -> i.getArgument(0));

        MascotaEntity resultado = mascotaService.cambiarEstadoMascota(5L, 0);

        assertEquals(0, resultado.getMasc_estado());
    }

    @Test
    void testBuscarPorNombre() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setMasc_nombre("Toby");

        Mockito.when(mascotaRepository.buscarPorNombre("toby")).thenReturn(List.of(mascota));

        List<MascotaEntity> resultado = mascotaService.buscarPorNombre("toby");
        assertEquals(1, resultado.size());
        assertEquals("Toby", resultado.get(0).getMasc_nombre());
    }

    @Test
    void testGenerarExcelMascotas_NoExcepciones() throws IOException {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setMasc_nombre("Fido");

        Mockito.when(mascotaRepository.findAll()).thenReturn(List.of(mascota));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> mascotaService.generarExcelMascotas(out));
    }
}