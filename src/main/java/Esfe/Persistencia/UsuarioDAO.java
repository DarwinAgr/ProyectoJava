package Esfe.Persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Esfe.Dominio.Usuario;
import Esfe.Utils.PasswordHasher;

public class UsuarioDAO {
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;



    public UsuarioDAO() {
        conn = ConnectionManager.getInstance();
    }

    public Usuario create(Usuario usuario) throws SQLException {
        Usuario res = null;
        String sql = "INSERT INTO Usuario (name, passwordHash, email, status, idNivelUsuario) VALUES (?, ?, ?, ?, ?)";

        try (var connection = conn.connect();
             var ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getName());
            ps.setString(2, PasswordHasher.hashPassword(usuario.getPasswordHash()));
            ps.setString(3, usuario.getEmail());
            ps.setInt(4, usuario.getStatus());
            ps.setInt(5, usuario.getIdNivelUsuario());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating usuario failed, no rows affected.");
            }

            try (var generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    res = getById(idGenerado);
                } else {
                    throw new SQLException("Creating usuario failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al crear el usuario: " + ex.getMessage(), ex);
        } finally {
            conn.disconnect();
        }
        return res;
    }


    public boolean update(Usuario usuario) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE Usuario SET name = ?, email = ?, status = ?, idNivelUsuario = ? WHERE idUsuario = ?"
            );

            ps.setString(1, usuario.getName());
            ps.setString(2, usuario.getEmail());
            ps.setInt(3, usuario.getStatus());
            ps.setInt(4, usuario.getIdNivelUsuario());
            ps.setInt(5, usuario.getIdUsuario());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el usuario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public boolean delete(Usuario usuario) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "DELETE FROM Usuario WHERE idUsuario = ?"
            );
            ps.setInt(1, usuario.getIdUsuario());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el usuario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public ArrayList<Usuario> search(String name) throws SQLException {
        ArrayList<Usuario> records = new ArrayList<>();

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT u.idUsuario, u.name, u.passwordHash, u.email, u.status,\n" +
                            "       u.idNivelUsuario, n.name AS nivelUsuarioName\n" +
                            "FROM Usuario u\n" +
                            "JOIN NivelUsuario n ON u.idNivelUsuario = n.idNivel\n" +
                            "WHERE u.name LIKE ?\n"
            );

            ps.setString(1, "%" + name + "%");

            rs = ps.executeQuery();

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setName(rs.getString("name"));
                usuario.setPasswordHash(rs.getString("passwordHash"));
                usuario.setEmail(rs.getString("email"));
                usuario.setStatus(rs.getInt("status"));
                usuario.setIdNivelUsuario(rs.getInt("idNivelUsuario"));
                usuario.setNivelUsuarioName(rs.getString("nivelUsuarioName"));

                records.add(usuario);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar usuarios: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }

        return records;
    }

    public Usuario getById(int id) throws SQLException {
        Usuario usuario = null;

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT idUsuario, name, passwordHash, email, status, idNivelUsuario FROM Usuario WHERE idUsuario = ?"
            );

            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setName(rs.getString("name"));
                usuario.setPasswordHash(rs.getString("passwordHash"));
                usuario.setEmail(rs.getString("email"));
                usuario.setStatus(rs.getInt("status"));
                usuario.setIdNivelUsuario(rs.getInt("idNivelUsuario"));
            }

            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener un usuario por id: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }

        return usuario;
    }

    public Usuario authenticate(Usuario usuario) throws SQLException {
        Usuario usuarioAutenticado = null;

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT idUsuario, name, passwordHash, email, status, idNivelUsuario " +
                            "FROM Usuario WHERE email = ? AND passwordHash = ? AND status = 1"
            );

            ps.setString(1, usuario.getEmail());
            ps.setString(2, PasswordHasher.hashPassword(usuario.getPasswordHash()));

            rs = ps.executeQuery();

            if (rs.next()) {
                usuarioAutenticado = new Usuario();
                usuarioAutenticado.setIdUsuario(rs.getInt("idUsuario"));
                usuarioAutenticado.setName(rs.getString("name"));
                usuarioAutenticado.setPasswordHash(rs.getString("passwordHash"));
                usuarioAutenticado.setEmail(rs.getString("email"));
                usuarioAutenticado.setStatus(rs.getInt("status"));
                usuarioAutenticado.setIdNivelUsuario(rs.getInt("idNivelUsuario"));
            }

            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al autenticar un usuario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }

        return usuarioAutenticado;
    }

    public boolean updatePassword(Usuario usuario) throws SQLException {
        boolean res = false;

        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE Usuario SET passwordHash = ? WHERE idUsuario = ?"
            );

            ps.setString(1, PasswordHasher.hashPassword(usuario.getPasswordHash()));
            ps.setInt(2, usuario.getIdUsuario());

            if (ps.executeUpdate() > 0) {
                res = true;
            }

            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el password del usuario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }

        return res;
    }
}
