package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private int id;
    private int clienteId;
    private LocalDateTime fecha;
    private String estado;
    private BigDecimal total;
    private List<LineaPedido> lineas;

    public Pedido() {
        this.lineas = new ArrayList<>();
        this.fecha = LocalDateTime.now();
        this.estado = "pendiente";
        this.total = BigDecimal.ZERO;
    }

    public Pedido(int id, int clienteId, LocalDateTime fecha,
            String estado, BigDecimal total) {
        this.id = id;
        this.clienteId = clienteId;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.lineas = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int c) {
        this.clienteId = c;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime f) {
        this.fecha = f;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String e) {
        this.estado = e;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal t) {
        this.total = t;
    }

    public List<LineaPedido> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaPedido> l) {
        this.lineas = l;
    }

    public void recalcularTotal() {
        total = lineas.stream()
                .map(l -> l.getPrecioUnit().multiply(BigDecimal.valueOf(l.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String toString() {
        return "Pedido #" + id + " | " + estado + " | " + total + "€";
    }
}
