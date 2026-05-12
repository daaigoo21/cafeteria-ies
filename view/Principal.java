package view;

import dao.PedidoDAOImpl;
import dao.ProductoDAOImpl;
import dao.UsuarioDAOImpl;
import dto.PedidoDetalleDTO;
import model.*;
import servicio.ServicioMeteo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class Principal extends JFrame {

    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();
    private final ProductoDAOImpl productoDAO = new ProductoDAOImpl();
    private final PedidoDAOImpl pedidoDAO = new PedidoDAOImpl();

    private final Usuario usuarioActivo;

    private final DefaultTableModel modeloTabla = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modeloTabla);
    private final JPanel panelForm = new JPanel(new GridBagLayout());
    private final JLabel lblModulo = new JLabel("Productos");

    private final JTextField fNombre = new JTextField(15);
    private final JTextField fDescripcion = new JTextField(15);
    private final JTextField fPrecio = new JTextField(15);
    private final JComboBox<String> fCategoria = new JComboBox<>(
            new String[] { "bebida", "comida", "bocadillo", "otro" });
    private final JTextField fStock = new JTextField(15);
    private final JTextField fUsername = new JTextField(15);
    private final JPasswordField fPassword = new JPasswordField(15);
    private final JTextField fEmail = new JTextField(15);
    private final JTextField fApellidos = new JTextField(15);
    private final JComboBox<String> fRol = new JComboBox<>(new String[] { "cliente", "empleado" });
    private final JComboBox<String> fEstado = new JComboBox<>(
            new String[] { "pendiente", "preparando", "listo", "entregado" });

    private final JLabel lblTemp = new JLabel("24°C");
    private final JLabel lblSensacion = new JLabel("22°C");
    private final JLabel lblDesc = new JLabel("Despejado");

    public Principal(Usuario usuario) {
        super("Cafeteria IES Francisco Ayala");
        this.usuarioActivo = usuario;
        construirUI();
        cargarModulo("PRODUCTOS");
        cargarTiempoAsync();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1050, 620);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void construirUI() {
        setLayout(new BorderLayout());
        add(crearMenuBar(), BorderLayout.NORTH);
        add(crearPanelLateral(), BorderLayout.WEST);
        add(crearPanelCentral(), BorderLayout.CENTER);
    }

    private JMenuBar crearMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu mSesion = new JMenu("Sesion");
        JMenuItem miInfo = new JMenuItem("Usuario: " + usuarioActivo.getNombre() + " (" + usuarioActivo.getRol() + ")");
        miInfo.setEnabled(false);
        JMenuItem miPass = new JMenuItem("Cambiar contrasena");
        JMenuItem miSalir = new JMenuItem("Cerrar sesion");
        miPass.addActionListener(e -> cambiarPassword());
        miSalir.addActionListener(e -> cerrarSesion());
        mSesion.add(miInfo);
        mSesion.addSeparator();
        mSesion.add(miPass);
        mSesion.add(miSalir);

        JMenu mTema = new JMenu("Tema");
        JMenuItem miClaro = new JMenuItem("Claro");
        JMenuItem miOscuro = new JMenuItem("Oscuro");
        miClaro.addActionListener(e -> aplicarTema("claro"));
        miOscuro.addActionListener(e -> aplicarTema("oscuro"));
        mTema.add(miClaro);
        mTema.add(miOscuro);

        bar.add(mSesion);
        bar.add(mTema);
        return bar;
    }

    private JPanel crearPanelLateral() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(80, 50, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        panel.setPreferredSize(new Dimension(160, 0));

        JLabel logo = new JLabel("Cafeteria IES", SwingConstants.CENTER);
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 14));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(logo);
        panel.add(Box.createVerticalStrut(20));

        for (String[] mod : new String[][] {
                { "PRODUCTOS", "Productos" },
                { "PEDIDOS", "Pedidos" },
                { "USUARIOS", "Usuarios" }
        }) {
            JButton btn = crearBotonNav(mod[1], mod[0]);
            panel.add(btn);
            panel.add(Box.createVerticalStrut(8));
        }

        panel.add(Box.createVerticalStrut(20));
        panel.add(crearPanelMeteo());

        return panel;
    }

    private JPanel crearPanelMeteo() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(150, 100, 60));
        sep.setMaximumSize(new Dimension(140, 2));
        panel.add(sep);
        panel.add(Box.createVerticalStrut(10));

        JLabel titulo = new JLabel("Granada", SwingConstants.CENTER);
        titulo.setForeground(new Color(220, 180, 120));
        titulo.setFont(new Font("SansSerif", Font.BOLD, 12));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(6));

        lblTemp.setForeground(Color.WHITE);
        lblTemp.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTemp.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTemp);

        lblDesc.setForeground(new Color(200, 200, 200));
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblDesc);
        panel.add(Box.createVerticalStrut(2));

        JLabel lblSensLabel = new JLabel("Sensacion:", SwingConstants.CENTER);
        lblSensLabel.setForeground(new Color(180, 180, 180));
        lblSensLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblSensLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblSensLabel);

        lblSensacion.setForeground(new Color(200, 200, 200));
        lblSensacion.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblSensacion.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblSensacion);
        panel.add(Box.createVerticalStrut(8));

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setMaximumSize(new Dimension(120, 26));
        btnActualizar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnActualizar.setBackground(new Color(120, 80, 40));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setBorderPainted(false);
        btnActualizar.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btnActualizar.addActionListener(e -> cargarTiempoAsync());
        panel.add(btnActualizar);

        return panel;
    }

    private void cargarTiempoAsync() {
        lblTemp.setText("...");
        lblDesc.setText("cargando");
        lblSensacion.setText("...");

        SwingWorker<String[], Void> worker = new SwingWorker<String[], Void>() {
            @Override
            protected String[] doInBackground() {
                return ServicioMeteo.obtenerTiempo();
            }

            @Override
            protected void done() {
                try {
                    String[] datos = get();
                    lblTemp.setText(datos[0]);
                    lblSensacion.setText(datos[1]);
                    lblDesc.setText(datos[2]);
                } catch (Exception e) {
                    lblTemp.setText("--");
                    lblDesc.setText("Error");
                    lblSensacion.setText("--");
                }
            }
        };
        worker.execute();
    }

    private JButton crearBotonNav(String texto, String modulo) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(140, 36));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(new Color(120, 80, 40));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.addActionListener(e -> cargarModulo(modulo));
        return btn;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblModulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblModulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        panel.add(lblModulo, BorderLayout.NORTH);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(24);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(620, 0));
        panel.add(scroll, BorderLayout.CENTER);

        panelForm.setBorder(BorderFactory.createTitledBorder("Operaciones"));
        panelForm.setPreferredSize(new Dimension(220, 0));
        panel.add(panelForm, BorderLayout.EAST);

        return panel;
    }

    private void cargarModulo(String modulo) {
        switch (modulo) {
            case "PRODUCTOS":
                lblModulo.setText("Productos");
                cargarProductos();
                break;
            case "PEDIDOS":
                lblModulo.setText("Pedidos");
                cargarPedidos();
                break;
            case "USUARIOS":
                lblModulo.setText("Usuarios");
                cargarUsuarios();
                break;
        }
    }

    private void cargarProductos() {
        modeloTabla
                .setColumnIdentifiers(new String[] { "ID", "Nombre", "Descripcion", "Precio", "Categoria", "Stock" });
        modeloTabla.setRowCount(0);
        for (Producto p : productoDAO.listarTodos()) {
            modeloTabla.addRow(new Object[] {
                    p.getId(), p.getNombre(), p.getDescripcion(),
                    p.getPrecio() + "EUR", p.getCategoria(), p.getStock()
            });
        }
        construirFormProducto();
    }

    private void construirFormProducto() {
        panelForm.removeAll();
        GridBagConstraints g = gbc();
        agregarCampoForm("Nombre:", fNombre, g, 0);
        agregarCampoForm("Descripcion:", fDescripcion, g, 1);
        agregarCampoForm("Precio:", fPrecio, g, 2);
        agregarCampoForm("Categoria:", fCategoria, g, 3);
        agregarCampoForm("Stock:", fStock, g, 4);

        JButton btnNuevo = boton("Nuevo", new Color(34, 139, 34));
        JButton btnGuardar = boton("Guardar", new Color(30, 100, 200));
        JButton btnEliminar = boton("Eliminar", new Color(180, 30, 30));

        tabla.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                int fila = tabla.getSelectedRow();
                fNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                fDescripcion.setText(modeloTabla.getValueAt(fila, 2).toString());
                fPrecio.setText(modeloTabla.getValueAt(fila, 3).toString().replace("EUR", ""));
                fCategoria.setSelectedItem(modeloTabla.getValueAt(fila, 4).toString());
                fStock.setText(modeloTabla.getValueAt(fila, 5).toString());
            }
        });

        btnNuevo.addActionListener(e -> limpiarFormProducto());
        btnGuardar.addActionListener(e -> guardarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());

        g.gridy = 5;
        g.gridx = 0;
        g.gridwidth = 2;
        panelForm.add(btnNuevo, g);
        g.gridy = 6;
        panelForm.add(btnGuardar, g);
        g.gridy = 7;
        panelForm.add(btnEliminar, g);

        panelForm.revalidate();
        panelForm.repaint();
    }

    private void limpiarFormProducto() {
        tabla.clearSelection();
        fNombre.setText("");
        fDescripcion.setText("");
        fPrecio.setText("");
        fStock.setText("");
        fCategoria.setSelectedIndex(0);
    }

    private void guardarProducto() {
        try {
            Producto p = new Producto();
            p.setNombre(fNombre.getText().trim());
            p.setDescripcion(fDescripcion.getText().trim());
            p.setPrecio(new BigDecimal(fPrecio.getText().trim()));
            p.setCategoria(fCategoria.getSelectedItem().toString());
            p.setStock(Integer.parseInt(fStock.getText().trim()));

            int fila = tabla.getSelectedRow();
            boolean ok;
            if (fila < 0) {
                ok = productoDAO.insertar(p);
            } else {
                p.setId((int) modeloTabla.getValueAt(fila, 0));
                ok = productoDAO.actualizar(p);
            }
            feedback(ok, "Producto guardado.", "Error al guardar el producto.");
            if (ok)
                cargarProductos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio y stock deben ser numericos.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        if (confirmar("Eliminar el producto seleccionado?")) {
            boolean ok = productoDAO.eliminar(id);
            feedback(ok, "Producto eliminado.", "Error al eliminar.");
            if (ok)
                cargarProductos();
        }
    }

    private void cargarPedidos() {
        modeloTabla.setColumnIdentifiers(new String[] { "ID", "Cliente", "Fecha", "Estado", "Total" });
        modeloTabla.setRowCount(0);
        List<PedidoDetalleDTO> lista = "cliente".equals(usuarioActivo.getRol())
                ? pedidoDAO.listarPorCliente(usuarioActivo.getId())
                : pedidoDAO.listarTodos();
        for (PedidoDetalleDTO d : lista) {
            modeloTabla.addRow(new Object[] {
                    d.getId(), d.getClienteCompleto(),
                    d.getFechaFormateada(), d.getEstado(), d.getTotal() + "EUR"
            });
        }
        construirFormPedido();
    }

    private void construirFormPedido() {
        panelForm.removeAll();
        GridBagConstraints g = gbc();

        JLabel lblSel = new JLabel("Selecciona un pedido");
        lblSel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 2;
        panelForm.add(lblSel, g);

        g.gridwidth = 1;
        agregarCampoForm("Estado:", fEstado, g, 1);

        JButton btnActualizar = boton("Actualizar estado", new Color(30, 100, 200));
        JButton btnEliminar = boton("Eliminar pedido", new Color(180, 30, 30));

        tabla.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                fEstado.setSelectedItem(modeloTabla.getValueAt(tabla.getSelectedRow(), 3).toString());
            }
        });

        btnActualizar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Selecciona un pedido.");
                return;
            }
            int id = (int) modeloTabla.getValueAt(fila, 0);
            boolean ok = pedidoDAO.actualizarEstado(id, fEstado.getSelectedItem().toString());
            feedback(ok, "Estado actualizado.", "Error al actualizar.");
            if (ok)
                cargarPedidos();
        });

        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Selecciona un pedido.");
                return;
            }
            int id = (int) modeloTabla.getValueAt(fila, 0);
            if (confirmar("Eliminar el pedido #" + id + "?")) {
                boolean ok = pedidoDAO.eliminar(id);
                feedback(ok, "Pedido eliminado.", "Error al eliminar.");
                if (ok)
                    cargarPedidos();
            }
        });

        g.gridy = 2;
        g.gridx = 0;
        g.gridwidth = 2;
        panelForm.add(btnActualizar, g);
        g.gridy = 3;
        panelForm.add(btnEliminar, g);

        if ("cliente".equals(usuarioActivo.getRol())) {
            JButton btnNuevo = boton("Nuevo pedido", new Color(34, 139, 34));
            btnNuevo.addActionListener(e -> abrirDialogoNuevoPedido());
            g.gridy = 4;
            panelForm.add(btnNuevo, g);
        }

        panelForm.revalidate();
        panelForm.repaint();
    }

    private void abrirDialogoNuevoPedido() {
        List<Producto> productos = productoDAO.listarTodos();
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos disponibles.");
            return;
        }

        JComboBox<Producto> cmbProducto = new JComboBox<>(productos.toArray(new Producto[0]));
        JSpinner spinCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));

        JPanel dlgPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        dlgPanel.add(new JLabel("Producto:"));
        dlgPanel.add(cmbProducto);
        dlgPanel.add(new JLabel("Cantidad:"));
        dlgPanel.add(spinCantidad);

        int res = JOptionPane.showConfirmDialog(this, dlgPanel, "Nuevo pedido", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION)
            return;

        Producto prod = (Producto) cmbProducto.getSelectedItem();
        int cantidad = (int) spinCantidad.getValue();

        LineaPedido linea = new LineaPedido(0, 0, prod.getId(), cantidad, prod.getPrecio());
        Pedido pedido = new Pedido();
        pedido.setClienteId(usuarioActivo.getId());
        pedido.getLineas().add(linea);
        pedido.recalcularTotal();

        int id = pedidoDAO.insertar(pedido);
        feedback(id > 0,
                "Pedido #" + id + " creado correctamente.",
                "Error: stock insuficiente o error interno.");
        cargarPedidos();
    }

    private void cargarUsuarios() {
        modeloTabla
                .setColumnIdentifiers(new String[] { "ID", "Username", "Nombre", "Apellidos", "Email", "DNI", "Rol" });
        modeloTabla.setRowCount(0);
        for (Usuario u : usuarioDAO.listarTodos()) {
            modeloTabla.addRow(new Object[] {
                    u.getId(), u.getUsername(), u.getNombre(),
                    u.getApellidos(), u.getEmail(), u.getDni(), u.getRol()
            });
        }
        construirFormUsuario();
    }

    private void construirFormUsuario() {
        panelForm.removeAll();
        GridBagConstraints g = gbc();
        agregarCampoForm("Username:", fUsername, g, 0);
        agregarCampoForm("Contrasena:", fPassword, g, 1);
        agregarCampoForm("Email:", fEmail, g, 2);
        agregarCampoForm("Apellidos:", fApellidos, g, 3);
        agregarCampoForm("Rol:", fRol, g, 4);

        JButton btnGuardar = boton("Guardar cambios", new Color(30, 100, 200));
        JButton btnEliminar = boton("Eliminar", new Color(180, 30, 30));

        tabla.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                int fila = tabla.getSelectedRow();
                fUsername.setText(modeloTabla.getValueAt(fila, 1).toString());
                fEmail.setText(modeloTabla.getValueAt(fila, 4).toString());
                fApellidos.setText(modeloTabla.getValueAt(fila, 3).toString());
                fRol.setSelectedItem(modeloTabla.getValueAt(fila, 6).toString());
                fPassword.setText("");
            }
        });

        btnGuardar.addActionListener(e -> guardarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());

        g.gridy = 5;
        g.gridx = 0;
        g.gridwidth = 2;
        panelForm.add(btnGuardar, g);
        g.gridy = 6;
        panelForm.add(btnEliminar, g);

        panelForm.revalidate();
        panelForm.repaint();
    }

    private void guardarUsuario() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario.");
            return;
        }
        Usuario u = new Usuario();
        u.setId((int) modeloTabla.getValueAt(fila, 0));
        u.setPassword(new String(fPassword.getPassword()));
        u.setEmail(fEmail.getText().trim());
        u.setNombre(modeloTabla.getValueAt(fila, 2).toString());
        u.setApellidos(fApellidos.getText().trim());
        boolean ok = usuarioDAO.actualizar(u);
        feedback(ok, "Usuario actualizado.", "Error al actualizar.");
        if (ok)
            cargarUsuarios();
    }

    private void eliminarUsuario() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        if (id == usuarioActivo.getId()) {
            JOptionPane.showMessageDialog(this, "No puedes eliminarte a ti mismo.");
            return;
        }
        if (confirmar("Eliminar el usuario seleccionado?")) {
            boolean ok = usuarioDAO.eliminar(id);
            feedback(ok, "Usuario eliminado.", "Error al eliminar.");
            if (ok)
                cargarUsuarios();
        }
    }

    private void cambiarPassword() {
        JPasswordField campo = new JPasswordField();
        int res = JOptionPane.showConfirmDialog(this, campo, "Nueva contrasena:", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION && campo.getPassword().length > 0) {
            usuarioActivo.setPassword(new String(campo.getPassword()));
            boolean ok = usuarioDAO.actualizar(usuarioActivo);
            feedback(ok, "Contrasena actualizada.", "Error al cambiar la contrasena.");
        }
    }

    private void cerrarSesion() {
        if (confirmar("Cerrar sesion?")) {
            dispose();
            new Login();
        }
    }

    private void aplicarTema(String tema) {
        try {
            String laf = "claro".equals(tema)
                    ? UIManager.getSystemLookAndFeelClassName()
                    : "javax.swing.plaf.nimbus.NimbusLookAndFeel";
            UIManager.setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private GridBagConstraints gbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;
        return g;
    }

    private void agregarCampoForm(String label, JComponent campo, GridBagConstraints g, int fila) {
        g.gridwidth = 1;
        g.gridy = fila;
        g.gridx = 0;
        panelForm.add(new JLabel(label), g);
        g.gridx = 1;
        panelForm.add(campo, g);
    }

    private JButton boton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return btn;
    }

    private boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje,
                "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private void feedback(boolean ok, String msgOk, String msgError) {
        if (ok) {
            JOptionPane.showMessageDialog(this, msgOk, "OK", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, msgError, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}