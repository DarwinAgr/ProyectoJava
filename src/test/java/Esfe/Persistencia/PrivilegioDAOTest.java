package Esfe.Persistencia;

import Esfe.Dominio.Privilegio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PrivilegioDAOTest {
    private PrivilegioDAO privilegioDAO;

    @BeforeEach
    void setUp(){privilegioDAO = new PrivilegioDAO();}
    
    private Privilegio create(Privilegio privilegio) throws SQLException {
        Privilegio res = privilegioDAO.create(privilegio);
        assertNotNull(res, "El privilegio creado no debería ser nulo.");
        assertEquals(privilegio.getName(), res.getName(), "El nombre del privilegio creado debe ser igual al original.");
        assertEquals(privilegio.getDescription(), res.getDescription(), "El nombre del privilegio creado debe ser igual al original.");
        assertEquals(privilegio.getStatus(), res.getStatus(), "El status del privilegio creado debe ser igual al original.");
        return res;
    }

    private void update(Privilegio privilegio) throws SQLException {
        privilegio.setName(privilegio.getName() + "_u");
        privilegio.setDescription("u" + privilegio.getDescription());
        privilegio.setStatus(1); // Activo

        boolean res = privilegioDAO.update(privilegio);
        assertTrue(res, "La actualización del privilegio debería ser exitosa.");

        getById(privilegio);
    }

    private void getById(Privilegio privilegio) throws SQLException {
        Privilegio res = privilegioDAO.getById(privilegio.getIdPrivilegio());
        assertNotNull(res, "El usuario obtenido por ID no debería ser nulo.");
        assertEquals(privilegio.getIdPrivilegio(), res.getIdPrivilegio(), "El ID del usuario obtenido debe ser igual al original.");
        assertEquals(privilegio.getName(), res.getName(), "El nombre del usuario obtenido debe ser igual al esperado.");
        assertEquals(privilegio.getDescription(), res.getDescription(), "El email del usuario obtenido debe ser igual al esperado.");
        assertEquals(privilegio.getStatus(), res.getStatus(), "El status del usuario obtenido debe ser igual al esperado.");
    }

    private void search(Privilegio privilegio) throws SQLException {
        ArrayList<Privilegio> privilegios = privilegioDAO.search(privilegio.getName());
        boolean find = false;
        for (Privilegio privilegioItem : privilegios) {
            if (privilegioItem.getName().contains(privilegio.getName())) {
                find = true;
            } else {
                find = false;
                break;
            }
        }
        assertTrue(find, "El nombre buscado no fue encontrado: " + privilegio.getName());
    }

    private void delete(Privilegio privilegio) throws SQLException {
        boolean res = privilegioDAO.delete(privilegio);
        assertTrue(res, "La eliminación del usuario debería ser exitosa.");

        Privilegio res2 = privilegioDAO.getById(privilegio.getIdPrivilegio());
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
        Privilegio privilegio = new Privilegio(0, "Test User", strDescription, 2);

        // Llama al método 'create' para persistir el usuario de prueba en la base de datos (simulada) y verifica su creación.
        Privilegio testPrivilegio = create(privilegio);

        // Llama al método 'update' para modificar los datos del usuario de prueba y verifica la actualización.
        update(testPrivilegio);

        // Llama al método 'search' para buscar usuarios por el nombre del usuario de prueba y verifica que se encuentre.
        search(testPrivilegio);

        // Llama al método 'delete' para eliminar el usuario de prueba de la base de datos y verifica la eliminación.
        delete(testPrivilegio);
    }
    @Test
    void createPrivilegio() throws SQLException {
        Privilegio privilegio = new Privilegio(0, "test", "testdePrivilegio",  1);
        Privilegio res = privilegioDAO.create(privilegio);
        assertNotEquals(res,null);
    }

}