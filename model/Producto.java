package model;

import java.math.BigDecimal;

public class Producto {

    private int id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String categoria;
    private int stock;

    public Producto() {
    }

    public Producto(int id, String nombre, String descripcion,
            BigDecimal precio, String categoria, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String n) {
        this.nombre = n;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String d) {
        this.descripcion = d;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal p) {
        this.precio = p;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String c) {
        this.categoria = c;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int s) {
        this.stock = s;
    }

    @Override
    public String toString() {
        return nombre + " — " + precio + "€ (stock: " + stock + ")";
    }
}
