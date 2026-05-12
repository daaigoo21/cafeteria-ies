package dao;

import db.ConexionDB;
import dto.PedidoDetalleDTO;
import model.LineaPedido;
import model.Pedido;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {

    private final ProductoDAOImpl productoDAO = new ProductoDAOImpl();

    @Override
    public int insertar(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedidos (cliente_id, fecha, estado, total) VALUES (?, ?, ?, ?)";
        String sqlLinea = "INSERT INTO lineas_pedido (pedido_id, producto_id, cantidad, precio_unit) VALUES (?, ?, ?, ?)";
        Connection con = null;
        try {
            con = ConexionDB.getConnection();
            ConexionDB.iniciarTransaccion(con);
            int pedidoId;
            try (PreparedStatement ps = con.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, pedido.getClienteId());
                ps.setTimestamp(2, Timestamp.valueOf(pedido.getFecha()));
                ps.setString(3, pedido.getEstado());
                ps.setBigDecimal(4, pedido.getTotal());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next())
                        throw new SQLException("No se generó ID de pedido");
                    pedidoId = keys.getInt(1);
                }
            }
            try (PreparedStatement psL = con.prepareStatement(sqlLinea)) {
                for (LineaPedido linea : pedido.getLineas()) {
                    boolean ok = productoDAO.reducirStock(linea.getProductoId(), linea.getCantidad(), con);
                    if (!ok)
                        throw new SQLException("Stock insuficiente para producto id=" + linea.getProductoId());
                    psL.setInt(1, pedidoId);
                    psL.setInt(2, linea.getProductoId());
                    psL.setInt(3, linea.getCantidad());
                    psL.setBigDecimal(4, linea.getPrecioUnit());
                    psL.addBatch();
                }
                psL.executeBatch();
            }
            ConexionDB.confirmar(con);
            return pedidoId;
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
        return -1;
    }

    @Override
    public List<PedidoDetalleDTO> listarTodos() {
        String sql = "SELECT p.id, p.fecha, p.estado, p.total, u.nombre, u.apellidos, u.username " +
                "FROM pedidos p JOIN usuarios u ON u.id = p.cliente_id ORDER BY p.fecha DESC";
        return ejecutarConsultaDetalle(sql, -1);
    }

    @Override
    public List<PedidoDetalleDTO> listarPorCliente(int clienteId) {
        String sql = "SELECT p.id, p.fecha, p.estado, p.total, u.nombre, u.apellidos, u.username " +
                "FROM pedidos p JOIN usuarios u ON u.id = p.cliente_id WHERE p.cliente_id = ? ORDER BY p.fecha DESC";
        return ejecutarConsultaDetalle(sql, clienteId);
    }

    @Override
    public boolean actualizarEstado(int pedidoId, String nuevoEstado) {
        String sql = "UPDATE pedidos SET estado = ? WHERE id = ?";
        try (Connection con = ConexionDB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, pedidoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean eliminar(int pedidoId) {
        String sql = "DELETE FROM pedidos WHERE id = ?";
        try (Connection con = ConexionDB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, pedidoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private List<PedidoDetalleDTO> ejecutarConsultaDetalle(String sql, int clienteId) {
        List<PedidoDetalleDTO> lista = new ArrayList<>();
        try (Connection con = ConexionDB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            if (clienteId >= 0)
                ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new PedidoDetalleDTO(
                            rs.getInt("id"),
                            rs.getTimestamp("fecha").toLocalDateTime(),
                            rs.getString("estado"),
                            rs.getBigDecimal("total"),
                            rs.getString("nombre"),
                            rs.getString("apellidos"),
                            rs.getString("username")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
