package dao;

import db.ConexionDB;
import model.Cliente;
import model.Empleado;
import model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public Usuario validar(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
        try (Connection con = ConexionDB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return mapearUsuario(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int registrar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (username, password, email, nombre, apellidos, dni, rol) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getPassword());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getNombre());
            ps.setString(5, usuario.getApellidos());
            ps.setString(6, usuario.getDni());
            ps.setString(7, usuario.getRol());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next())
                    return keys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM usuarios ORDER BY apellidos, nombre";
        List<Usuario> lista = new ArrayList<>();
        try (Connection con = ConexionDB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                lista.add(mapearUsuario(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET password = ?, email = ?, nombre = ?, apellidos = ? WHERE id = ?";
        try (Connection con = ConexionDB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getPassword());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getNombre());
            ps.setString(4, usuario.getApellidos());
            ps.setInt(5, usuario.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection con = ConexionDB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean registrarCliente(Cliente cliente) {
        String sqlU = "INSERT INTO usuarios (username, password, email, nombre, apellidos, dni, rol) VALUES (?, ?, ?, ?, ?, ?, 'cliente')";
        String sqlC = "INSERT INTO clientes (usuario_id, curso) VALUES (?, ?)";
        Connection con = null;
        try {
            con = ConexionDB.getConnection();
            ConexionDB.iniciarTransaccion(con);
            int userId;
            try (PreparedStatement psU = con.prepareStatement(sqlU, Statement.RETURN_GENERATED_KEYS)) {
                psU.setString(1, cliente.getUsername());
                psU.setString(2, cliente.getPassword());
                psU.setString(3, cliente.getEmail());
                psU.setString(4, cliente.getNombre());
                psU.setString(5, cliente.getApellidos());
                psU.setString(6, cliente.getDni());
                psU.executeUpdate();
                try (ResultSet keys = psU.getGeneratedKeys()) {
                    if (!keys.next())
                        throw new SQLException("No se generó ID");
                    userId = keys.getInt(1);
                }
            }
            try (PreparedStatement psC = con.prepareStatement(sqlC)) {
                psC.setInt(1, userId);
                psC.setString(2, cliente.getCurso());
                psC.executeUpdate();
            }
            ConexionDB.confirmar(con);
            return true;
        } catch (SQLException e) {
            ConexionDB.revertir(con);
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.close();
                } catch (SQLException ignored) {
                }
        }
        return false;
    }

    public boolean registrarEmpleado(Empleado empleado) {
        String sqlU = "INSERT INTO usuarios (username, password, email, nombre, apellidos, dni, rol) VALUES (?, ?, ?, ?, ?, ?, 'empleado')";
        String sqlE = "INSERT INTO empleados (usuario_id, turno) VALUES (?, ?)";
        Connection con = null;
        try {
            con = ConexionDB.getConnection();
            ConexionDB.iniciarTransaccion(con);
            int userId;
            try (PreparedStatement psU = con.prepareStatement(sqlU, Statement.RETURN_GENERATED_KEYS)) {
                psU.setString(1, empleado.getUsername());
                psU.setString(2, empleado.getPassword());
                psU.setString(3, empleado.getEmail());
                psU.setString(4, empleado.getNombre());
                psU.setString(5, empleado.getApellidos());
                psU.setString(6, empleado.getDni());
                psU.executeUpdate();
                try (ResultSet keys = psU.getGeneratedKeys()) {
                    if (!keys.next())
                        throw new SQLException("No se generó ID");
                    userId = keys.getInt(1);
                }
            }
            try (PreparedStatement psE = con.prepareStatement(sqlE)) {
                psE.setInt(1, userId);
                psE.setString(2, empleado.getTurno());
                psE.executeUpdate();
            }
            ConexionDB.confirmar(con);
            return true;
        } catch (SQLException e) {
            ConexionDB.revertir(con);
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.close();
                } catch (SQLException ignored) {
                }
        }
        return false;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email"),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                rs.getString("dni"),
                rs.getString("rol"));
    }
}
