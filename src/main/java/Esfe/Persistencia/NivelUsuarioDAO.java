package Esfe.Persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Esfe.Dominio.NivelUsuario;

public class NivelUsuarioDAO {
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;



    public NivelUsuarioDAO() {
        conn = ConnectionManager.getInstance();
    }

    public NivelUsuario create(NivelUsuario nivelUsuario) throws SQLException {
        NivelUsuario res = null;
        String sql = "INSERT INTO NivelUsuario (name, description, minPoint, maxPoint, status, idPrivilegio ) VALUES (?, ?, ?, ?, ?, ? )";

        try (var connection = conn.connect();
             var ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nivelUsuario.getName());
            ps.setString(2, nivelUsuario.getDescription());
            ps.setInt(3, nivelUsuario.getMinPoint());
            ps.setInt(4, nivelUsuario.getMaxPoint());
            ps.setInt(5, nivelUsuario.getStatus());
            ps.setInt(6, nivelUsuario.getIdPrivilegio());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating nivelUsuario failed, no rows affected.");
            }

            try (var generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    res = getById(idGenerado);
                } else {
                    throw new SQLException("Creating nivelUsuario failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al crear el nivelUsuario: " + ex.getMessage(), ex);
        } finally {
            conn.disconnect();
        }
        return res;
    }


    public boolean update(NivelUsuario nivelUsuario) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE NivelUsuario SET name = ?, description = ?, minPoint = ?, maxPoint = ?, status = ?, idPrivilegio = ? "
            );

            ps.setString(1, nivelUsuario.getName());
            ps.setString(2, nivelUsuario.getDescription());
            ps.setInt(3, nivelUsuario.getMinPoint());
            ps.setInt(4, nivelUsuario.getMaxPoint());
            ps.setInt(5, nivelUsuario.getStatus());
            ps.setInt(6, nivelUsuario.getIdPrivilegio());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el nivelUsuario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public boolean delete(NivelUsuario nivelUsuario) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "DELETE FROM NivelUsuario WHERE idNivel = ?"
            );
            ps.setInt(1, nivelUsuario.getIdNivel());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el nivelUsuario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public ArrayList<NivelUsuario> search(String name) throws SQLException {
        ArrayList<NivelUsuario> records = new ArrayList<>();

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT idNivelUsuario, name, description, minPoint, maxPoint, status, idPrivilegio FROM NivelUsuario WHERE name LIKE ?"
            );

            ps.setString(1, "%" + name + "%");

            rs = ps.executeQuery();

            while (rs.next()) {
                NivelUsuario nivelUsuario = new NivelUsuario();
                nivelUsuario.setIdNivel(rs.getInt("idNivel"));
                nivelUsuario.setName(rs.getString("name"));
                nivelUsuario.setDescription(rs.getString("description"));
                nivelUsuario.setMinPoint(rs.getInt("minPoint"));
                nivelUsuario.setMaxPoint(rs.getInt("maxPoint"));
                nivelUsuario.setStatus(rs.getInt("status"));
                nivelUsuario.setIdPrivilegio(rs.getInt("idPrivilegio"));

                records.add(nivelUsuario);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar nivelUsuarios: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }

        return records;
    }

    public NivelUsuario getById(int id) throws SQLException {
        NivelUsuario nivelUsuario = null;

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT idNivel, name, description, minPoint, maxPoint, status, idPrivilegio FROM NivelUsuario WHERE idNivel = ?"
            );

            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                nivelUsuario = new NivelUsuario();
                nivelUsuario.setIdNivel(rs.getInt("idNivel"));
                nivelUsuario.setName(rs.getString("name"));
                nivelUsuario.setDescription(rs.getString("description"));
                nivelUsuario.setMinPoint(rs.getInt("minPoint"));
                nivelUsuario.setMaxPoint(rs.getInt("maxPoint"));
                nivelUsuario.setStatus(rs.getInt("status"));
                nivelUsuario.setIdPrivilegio(rs.getInt("idPrivilegio"));
            }

            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener un nivelUsuario por id: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }

        return nivelUsuario;
    }
}

