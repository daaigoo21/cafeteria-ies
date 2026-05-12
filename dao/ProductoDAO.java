package dao;

import model.Producto;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ProductoDAO {

    boolean insertar(Producto producto);

    boolean actualizar(Producto producto);

    List<Producto> listarTodos();

    Producto buscarPorId(int id);

    boolean eliminar(int id);

    boolean reducirStock(int productoId, int cantidad, Connection con) throws SQLException;
}
