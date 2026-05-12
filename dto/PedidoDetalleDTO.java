package dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PedidoDetalleDTO {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int id;
    private LocalDateTime fecha;
    private String estado;
    private BigDecimal total;
    private String clienteNombre;
    private String clienteApellidos;
    private String clienteUsername;

    public PedidoDetalleDTO(int id, LocalDateTime fecha, String estado,
            BigDecimal total, String clienteNombre,
            String clienteApellidos, String clienteUsername) {
        this.id = id;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.clienteNombre = clienteNombre;
        this.clienteApellidos = clienteApellidos;
        this.clienteUsername = clienteUsername;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getFechaFormateada() {
        return fecha.format(FMT);
    }

    public String getEstado() {
        return estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public String getClienteApellidos() {
        return clienteApellidos;
    }

    public String getClienteUsername() {
        return clienteUsername;
    }

    public String getClienteCompleto() {
        return clienteNombre + " " + clienteApellidos;
    }

    @Override
    public String toString() {
        return "Pedido #" + id + " | " + getClienteCompleto()
                + " | " + getFechaFormateada()
                + " | " + estado + " | " + total + "€";
    }
}
