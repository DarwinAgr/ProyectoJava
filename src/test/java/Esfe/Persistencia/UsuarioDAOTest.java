package Esfe.Persistencia;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Esfe.Dominio.Usuario;

import java.util.ArrayList;
import java.util.Random;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDAOTest {
    private UsuarioDAO usuarioDAO;

    @BeforeEach
    void setUp() {
        usuarioDAO = new UsuarioDAO();
    }

    private Usuario create(Usuario usuario) throws SQLException {
        Usuario res = usuarioDAO.create(usuario);
        assertNotNull(res, "El usuario creado no debería ser nulo.");
        assertEquals(usuario.getName(), res.getName(), "El nombre del usuario creado debe ser igual al original.");
        assertEquals(usuario.getEmail(), res.getEmail(), "El email del usuario creado debe ser igual al original.");
        assertEquals(usuario.getStatus(), res.getStatus(), "El status del usuario creado debe ser igual al original.");
        assertEquals(usuario.getIdNivelUsuario(), res.getIdNivelUsuario(), "El nivel del usuario debe ser igual al original.");
        return res;
    }

    private void update(Usuario usuario) throws SQLException {
        usuario.setName(usuario.getName() + "_u");
        usuario.setEmail("u" + usuario.getEmail());
        usuario.setStatus(1); // Activo
        usuario.setIdNivelUsuario(1); // Asumiendo cambio de nivel de usuario

        boolean res = usuarioDAO.update(usuario);
        assertTrue(res, "La actualización del usuario debería ser exitosa.");

        getById(usuario);
    }

    private void getById(Usuario usuario) throws SQLException {
        Usuario res = usuarioDAO.getById(usuario.getIdUsuario());
        assertNotNull(res, "El usuario obtenido por ID no debería ser nulo.");
        assertEquals(usuario.getIdUsuario(), res.getIdUsuario(), "El ID del usuario obtenido debe ser igual al original.");
        assertEquals(usuario.getName(), res.getName(), "El nombre del usuario obtenido debe ser igual al esperado.");
        assertEquals(usuario.getEmail(), res.getEmail(), "El email del usuario obtenido debe ser igual al esperado.");
        assertEquals(usuario.getStatus(), res.getStatus(), "El status del usuario obtenido debe ser igual al esperado.");
        assertEquals(usuario.getIdNivelUsuario(), res.getIdNivelUsuario(), "El nivel del usuario obtenido debe ser igual al esperado.");
    }

    private void search(Usuario usuario) throws SQLException {
        ArrayList<Usuario> usuarios = usuarioDAO.search(usuario.getName());
        boolean find = false;
        for (Usuario usuarioItem : usuarios) {
            if (usuarioItem.getName().contains(usuario.getName())) {
                find = true;
            } else {
                find = false;
                break;
            }
        }
        assertTrue(find, "El nombre buscado no fue encontrado: " + usuario.getName());
    }

    private void delete(Usuario usuario) throws SQLException {
        boolean res = usuarioDAO.delete(usuario);
        assertTrue(res, "La eliminación del usuario debería ser exitosa.");

        Usuario res2 = usuarioDAO.getById(usuario.getIdUsuario());
        assertNull(res2, "El usuario debería haber sido eliminado y no encontrado por ID.");
    }

    private void authenticate(Usuario usuario) throws SQLException {
        Usuario res = usuarioDAO.authenticate(usuario);
        assertNotNull(res, "La autenticación debería retornar un usuario no nulo si es exitosa.");
        assertEquals(res.getEmail(), usuario.getEmail(), "El email del usuario autenticado debe coincidir con el email proporcionado.");
        assertEquals(res.getStatus(), 1, "El status del usuario autenticado debe ser 1 (activo).");
    }

    private void authenticationFails(Usuario usuario) throws SQLException {
        Usuario res = usuarioDAO.authenticate(usuario);
        assertNull(res, "La autenticación debería fallar y retornar null para credenciales inválidas.");
    }

    private void updatePassword(Usuario usuario) throws SQLException {
        boolean res = usuarioDAO.updatePassword(usuario);
        assertTrue(res, "La actualización de la contraseña debería ser exitosa.");
        authenticate(usuario);
    }

    @Test
    void testUsuarioDAO() throws SQLException {
        Random random = new Random();
        int num = random.nextInt(1000) + 1;
        String strEmail = "test" + num + "@example.com";

        // Crear un usuario con idUsuario = 0 (aún no asignado por BD)
        Usuario usuario = new Usuario(0, "Test User", "password", strEmail, 2, 1);

        Usuario testUsuario = create(usuario);
        update(testUsuario);
        search(testUsuario);

        // Restaurar contraseña original para autenticación exitosa
        testUsuario.setPasswordHash(usuario.getPasswordHash());
        authenticate(testUsuario);

        // Autenticación con contraseña errónea
        testUsuario.setPasswordHash("12345");
        authenticationFails(testUsuario);

        // Actualizar contraseña
        testUsuario.setPasswordHash("new_password");
        updatePassword(testUsuario);
        testUsuario.setPasswordHash("new_password");
        authenticate(testUsuario);

        delete(testUsuario);
    }

    @Test
    void createUsuario() throws SQLException {
        Usuario usuario = new Usuario(0, "admin", "12345", "admin@gmail.com", 1, 1);
        Usuario res = usuarioDAO.create(usuario);
        assertNotNull(res);
    }
}
