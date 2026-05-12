package model;

import java.math.BigDecimal;

public class LineaPedido {

    private int id;
    private int pedidoId;
    private int productoId;
    private int cantidad;
    private BigDecimal precioUnit;

    public LineaPedido() {
    }

    public LineaPedido(int id, int pedidoId, int productoId,
            int cantidad, BigDecimal precioUnit) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnit = precioUnit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int p) {
        this.pedidoId = p;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int p) {
        this.productoId = p;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int c) {
        this.cantidad = c;
    }

    public BigDecimal getPrecioUnit() {
        return precioUnit;
    }

    public void setPrecioUnit(BigDecimal p) {
        this.precioUnit = p;
    }

    public BigDecimal getSubtotal() {
        return precioUnit.multiply(BigDecimal.valueOf(cantidad));
    }

    @Override
    public String toString() {
        return "Producto #" + productoId + " x" + cantidad + " = " + getSubtotal() + "€";
    }
}
