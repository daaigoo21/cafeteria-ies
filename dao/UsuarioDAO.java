package dao;

import model.Usuario;
import java.util.List;

public interface UsuarioDAO {

    Usuario validar(String username, String password);

    int registrar(Usuario usuario);

    List<Usuario> listarTodos();

    boolean actualizar(Usuario usuario);

    boolean eliminar(int id);
}
