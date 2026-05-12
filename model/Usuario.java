package model;

public class Usuario {

    private int id;
    private String username;
    private String password;
    private String email;
    private String nombre;
    private String apellidos;
    private String dni;
    private String rol;

    public Usuario() {
    }

    public Usuario(int id, String username, String password,
            String email, String nombre, String apellidos,
            String dni, String rol) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String u) {
        this.username = u;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String p) {
        this.password = p;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String e) {
        this.email = e;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String n) {
        this.nombre = n;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String a) {
        this.apellidos = a;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String d) {
        this.dni = d;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String r) {
        this.rol = r;
    }

    @Override
    public String toString() {
        return nombre + " " + apellidos + " (" + username + ")";
    }
}
