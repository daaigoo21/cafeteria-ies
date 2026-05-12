package model;

public class Cliente extends Usuario {

    private String curso;

    public Cliente() {
        super();
        setRol("cliente");
    }

    public Cliente(int id, String username, String password,
            String email, String nombre, String apellidos,
            String dni, String curso) {
        super(id, username, password, email, nombre, apellidos, dni, "cliente");
        this.curso = curso;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String c) {
        this.curso = c;
    }

    @Override
    public String toString() {
        return super.toString() + " | Curso: " + curso;
    }
}
