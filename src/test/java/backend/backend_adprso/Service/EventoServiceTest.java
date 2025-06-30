package backend.backend_adprso.Service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import backend.backend_adprso.Entity.Evento.EventoEntity;
import backend.backend_adprso.Repository.EventoRepository;
import backend.backend_adprso.Repository.EventoUsuarioRepository;
import backend.backend_adprso.Service.Cloudinary.CloudinaryService;
import backend.backend_adprso.Service.Evento.EventoService;
import backend.backend_adprso.Service.Usuario.UsuarioService;

@ExtendWith(MockitoExtension.class)
class EventoServiceTest {
    @InjectMocks
    private EventoService eventoService;
    @Mock
    private EventoRepository eventoRepository;
    @Mock
    private EventoUsuarioRepository eventoUsuarioRepository;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private CloudinaryService cloudinaryService;
    private EventoEntity evento;

    @BeforeEach
    void setUp() {
        evento = new EventoEntity();
        evento.setEven_id(1L);
        evento.setEven_nombre("Taller de Java");
        evento.setEven_estado(1);
    }

    @Test
    void testListarEventos() {
        List<EventoEntity> mockEventos = List.of(evento);
        Mockito.when(eventoRepository.findAll()).thenReturn(mockEventos);

        List<EventoEntity> resultado = eventoService.ListarEventos();

        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals("Taller de Java", resultado.get(0).getEven_nombre());
    }

    @Test
    void testObtenerEventoPorId_Existe() {
        Mockito.when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));

        Optional<EventoEntity> resultado = eventoService.ObtenerEventoPorId(1L);

        Assertions.assertTrue(resultado.isPresent());
        Assertions.assertEquals("Taller de Java", resultado.get().getEven_nombre());
    }

    @Test
    void testObtenerEventoPorId_NoExiste() {
        Mockito.when(eventoRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<EventoEntity> resultado = eventoService.ObtenerEventoPorId(2L);

        Assertions.assertFalse(resultado.isPresent());
    }

    @Test
    void testCambiarEstadoEvento_Existe() {
        Mockito.when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        Mockito.when(eventoRepository.save(Mockito.any(EventoEntity.class))).thenReturn(evento);

        EventoEntity resultado = eventoService.cambiarEstadoEvento(1L, 0);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(0, resultado.getEven_estado());
    }

    @Test
    void testCambiarEstadoEvento_NoExiste() {
        Mockito.when(eventoRepository.findById(100L)).thenReturn(Optional.empty());

        EventoEntity resultado = eventoService.cambiarEstadoEvento(100L, 0);

        Assertions.assertNull(resultado);
    }

    @Test
    void testRegistrarEvento() {
        evento.setEven_estado(null);
        Mockito.when(eventoRepository.save(Mockito.any(EventoEntity.class))).thenReturn(evento);

        EventoEntity resultado = eventoService.RegistrarEvento(evento);

        Assertions.assertEquals(1, resultado.getEven_estado());
        Mockito.verify(eventoRepository).save(evento);
    }

    @Test
    void testListarEventosActivos() {
        Mockito.when(eventoRepository.findByEvenEstado(1)).thenReturn(List.of(evento));

        List<EventoEntity> resultado = eventoService.ListarEventosActivos();

        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals(1, resultado.get(0).getEven_estado());
    }

    @Test
    void testBuscarPorNombre() {
        Mockito.when(eventoRepository.buscarPorNombre("java")).thenReturn(List.of(evento));

        List<EventoEntity> resultado = eventoService.buscarPorNombre("java");

        Assertions.assertFalse(resultado.isEmpty());
    }

    @Test
    void testListarEventosInactivos() {
        EventoEntity inactivo = new EventoEntity();
        inactivo.setEven_id(2L);
        inactivo.setEven_estado(0);

        Mockito.when(eventoRepository.findByEvenEstado(0)).thenReturn(List.of(inactivo));

        List<EventoEntity> resultado = eventoService.listarEventosInactivos();

        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals(0, resultado.get(0).getEven_estado());
    }
}
