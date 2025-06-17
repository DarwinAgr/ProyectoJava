package Esfe.Persistencia;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Esfe.Dominio.Privilegio;

public class PrivilegioDAO {
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;



    public PrivilegioDAO() {
        conn = ConnectionManager.getInstance();
    }

    public Privilegio create(Privilegio privilegio) throws SQLException {
        Privilegio res = null;
        String sql = "INSERT INTO Privilegio (name, description, status ) VALUES (?, ?, ? )";

        try (var connection = conn.connect();
             var ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, privilegio.getName());
            ps.setString(2, privilegio.getDescription());
            ps.setInt(3, privilegio.getStatus());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating privilegio failed, no rows affected.");
            }

            try (var generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    res = getById(idGenerado);
                } else {
                    throw new SQLException("Creating privilegio failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al crear el privilegio: " + ex.getMessage(), ex);
        } finally {
            conn.disconnect();
        }
        return res;
    }


    public boolean update(Privilegio privilegio) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE Privilegio SET name = ?, description = ?, status = ? WHERE idPrivilegio= ? "
            );

            ps.setString(1, privilegio.getName());
            ps.setString(2, privilegio.getDescription());
            ps.setInt(3, privilegio.getStatus());
            ps.setInt(4, privilegio.getIdPrivilegio());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el privilegio: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public boolean delete(Privilegio privilegio) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "DELETE FROM Privilegio WHERE idPrivilegio = ?"
            );
            ps.setInt(1, privilegio.getIdPrivilegio());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el privilegio: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public ArrayList<Privilegio> search(String name) throws SQLException {
        ArrayList<Privilegio> records = new ArrayList<>();

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT idPrivilegio, name, description, status FROM Privilegio WHERE name LIKE ?"
            );

            ps.setString(1, "%" + name + "%");

            rs = ps.executeQuery();

            while (rs.next()) {
                Privilegio privilegio = new Privilegio();
                privilegio.setIdPrivilegio(rs.getInt("idPrivilegio"));
                privilegio.setName(rs.getString("name"));
                privilegio.setDescription(rs.getString("description"));
                privilegio.setStatus(rs.getInt("status"));

                records.add(privilegio);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar privilegios: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }

        return records;
    }

    public Privilegio getById(int id) throws SQLException {
        Privilegio privilegio = null;

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT idPrivilegio, name, description, status FROM Privilegio WHERE idPrivilegio = ?"
            );

            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                privilegio = new Privilegio();
                privilegio.setIdPrivilegio(rs.getInt("idPrivilegio"));
                privilegio.setName(rs.getString("name"));
                privilegio.setDescription(rs.getString("description"));
                privilegio.setStatus(rs.getInt("status"));
            }

            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener un privilegio por id: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }

        return privilegio;
    }
}

