package dao;

import dto.PedidoDetalleDTO;
import model.Pedido;
import java.util.List;

public interface PedidoDAO {
    int insertar(Pedido pedido);

    List<PedidoDetalleDTO> listarTodos();

    List<PedidoDetalleDTO> listarPorCliente(int clienteId);

    boolean actualizarEstado(int pedidoId, String nuevoEstado);

    boolean eliminar(int pedidoId);
}
