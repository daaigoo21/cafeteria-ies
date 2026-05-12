package dao;

import db.ConexionDB;
import model.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {

    @Override
    public boolean insertar(Producto producto) {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, categoria, stock) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setBigDecimal(3, producto.getPrecio());
            ps.setString(4, producto.getCategoria());
            ps.setInt(5, producto.getStock());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    producto.setId(keys.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean actualizar(Producto producto) {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, categoria = ?, stock = ? WHERE id = ?";
        try (Connection con = ConexionDB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setBigDecimal(3, producto.getPrecio());
            ps.setString(4, producto.getCategoria());
            ps.setInt(5, producto.getStock());
            ps.setInt(6, producto.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Producto> listarTodos() {
        String sql = "SELECT * FROM productos ORDER BY categoria, nombre";
        List<Producto> lista = new ArrayList<>();
        try (Connection con = ConexionDB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                lista.add(mapear(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Producto buscarPorId(int id) {
        String sql = "SELECT * FROM productos WHERE id = ?";
        try (Connection con = ConexionDB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return mapear(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        try (Connection con = ConexionDB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean reducirStock(int productoId, int cantidad, Connection con) throws SQLException {
        String sqlCheck = "SELECT stock FROM productos WHERE id = ? FOR UPDATE";
        String sqlUpdate = "UPDATE productos SET stock = stock - ? WHERE id = ?";
        try (PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
            psCheck.setInt(1, productoId);
            try (ResultSet rs = psCheck.executeQuery()) {
                if (!rs.next() || rs.getInt("stock") < cantidad)
                    return false;
            }
        }
        try (PreparedStatement psUpdate = con.prepareStatement(sqlUpdate)) {
            psUpdate.setInt(1, cantidad);
            psUpdate.setInt(2, productoId);
            return psUpdate.executeUpdate() > 0;
        }
    }

    private Producto mapear(ResultSet rs) throws SQLException {
        return new Producto(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getBigDecimal("precio"),
                rs.getString("categoria"),
                rs.getInt("stock"));
    }
}
