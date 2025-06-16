package Esfe.Persistencia;

import Esfe.Dominio.NivelUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class NivelUsuarioDAOTest {
    private NivelUsuarioDAO nivelUsuarioDAO;

    @BeforeEach
    void setUp(){nivelUsuarioDAO = new NivelUsuarioDAO();}

    private NivelUsuario create(NivelUsuario nivel) throws SQLException {
        NivelUsuario res = nivelUsuarioDAO.create(nivel);
        assertNotNull(res, "El privilegio creado no debería ser nulo.");
        assertEquals(nivel.getName(), res.getName(), "El nombre del privilegio creado debe ser igual al original.");
        assertEquals(nivel.getDescription(), res.getDescription(), "El nombre del privilegio creado debe ser igual al original.");
        assertEquals(nivel.getMinPoint(), res.getMinPoint(), "El nombre del privilegio creado debe ser igual al original.");
        assertEquals(nivel.getMaxPoint(), res.getMaxPoint(), "El nombre del privilegio creado debe ser igual al original.");
        assertEquals(nivel.getStatus(), res.getStatus(), "El status del privilegio creado debe ser igual al original.");
        assertEquals(nivel.getIdPrivilegio(), res.getIdPrivilegio(), "El status del privilegio creado debe ser igual al original.");
        return res;
    }

    private void update(NivelUsuario nivel) throws SQLException {
        nivel.setName(nivel.getName() + "_u");
        nivel.setDescription("u" + nivel.getDescription());
        nivel.setStatus(1); // Activo

        boolean res = nivelUsuarioDAO.update(nivel);
        assertTrue(res, "La actualización del privilegio debería ser exitosa.");

        getById(nivel);
    }

    private void getById(NivelUsuario nivel) throws SQLException {
        NivelUsuario res = nivelUsuarioDAO.getById(nivel.getIdNivel());
        assertNotNull(res, "El usuario obtenido por ID no debería ser nulo.");
        assertEquals(nivel.getIdPrivilegio(), res.getIdPrivilegio(), "El ID del usuario obtenido debe ser igual al original.");
        assertEquals(nivel.getName(), res.getName(), "El nombre del usuario obtenido debe ser igual al esperado.");
        assertEquals(nivel.getDescription(), res.getDescription(), "El email del usuario obtenido debe ser igual al esperado.");
        assertEquals(nivel.getStatus(), res.getStatus(), "El status del usuario obtenido debe ser igual al esperado.");
    }

    private void search(NivelUsuario nivel) throws SQLException {
        ArrayList<NivelUsuario> niveles = nivelUsuarioDAO.search(nivel.getName());
        boolean find = false;
        for (NivelUsuario nivelItem : niveles) {
            if (nivelItem.getName().contains(nivel.getName())) {
                find = true;
            } else {
                find = false;
                break;
            }
        }
        assertTrue(find, "El nombre buscado no fue encontrado: " + nivel.getName());
    }

    private void delete(NivelUsuario nivel) throws SQLException {
        boolean res = nivelUsuarioDAO.delete(nivel);
        assertTrue(res, "La eliminación del usuario debería ser exitosa.");

        NivelUsuario res2 = nivelUsuarioDAO.getById(nivel.getIdPrivilegio());
        assertNull(res2, "El usuario debería haber sido eliminado y no encontrado por ID.");
    }

    @Test
    void testPrivilegioDAO() throws SQLException {
        // Crea una instancia de la clase Random para generar datos de prueba aleatorios.
        Random random = new Random();
        // Genera un número aleatorio entre 1 y 1000 para asegurar la unicidad del email en cada prueba.
        int num = random.nextInt(1000) + 1;
        // Define una cadena base para el email y le concatena el número aleatorio generado.
        String strDescription = "test" + num ;
        // Crea un nuevo objeto User con datos de prueba. El ID se establece en 0 ya que será generado por la base de datos.
        NivelUsuario nivel = new NivelUsuario(0, "Test Nivel", strDescription, 100, 300, 2, 1);

        // Llama al método 'create' para persistir el usuario de prueba en la base de datos (simulada) y verifica su creación.
        NivelUsuario testNivel = create(nivel);

        // Llama al método 'update' para modificar los datos del usuario de prueba y verifica la actualización.
        update(testNivel);

        // Llama al método 'search' para buscar usuarios por el nombre del usuario de prueba y verifica que se encuentre.
        search(testNivel);

        // Llama al método 'delete' para eliminar el usuario de prueba de la base de datos y verifica la eliminación.
        delete(testNivel);
    }
    @Test
    void createPrivilegio() throws SQLException {
        NivelUsuario nivel = new NivelUsuario(0, "test", "testdePrivilegio", 200, 500, 1, 1);
        NivelUsuario res = nivelUsuarioDAO.create(nivel);
        assertNotEquals(res,null);
    }

}