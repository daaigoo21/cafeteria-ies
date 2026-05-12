package model;

public class Empleado extends Usuario {

    private String turno;

    public Empleado() {
        super();
        setRol("empleado");
    }

    public Empleado(int id, String username, String password,
            String email, String nombre, String apellidos,
            String dni, String turno) {
        super(id, username, password, email, nombre, apellidos, dni, "empleado");
        this.turno = turno;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String t) {
        this.turno = t;
    }

    @Override
    public String toString() {
        return super.toString() + " | Turno: " + turno;
    }
}
