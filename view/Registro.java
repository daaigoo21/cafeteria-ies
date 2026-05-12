package view;

import dao.UsuarioDAOImpl;
import model.Cliente;
import model.Empleado;

import javax.swing.*;
import java.awt.*;

public class Registro extends JFrame {

    private final JTextField txtUsername = new JTextField(18);
    private final JPasswordField txtPassword = new JPasswordField(18);
    private final JTextField txtEmail = new JTextField(18);
    private final JTextField txtNombre = new JTextField(18);
    private final JTextField txtApellidos = new JTextField(18);
    private final JTextField txtDni = new JTextField(18);
    private final JComboBox<String> cmbRol = new JComboBox<>(new String[] { "cliente", "empleado" });
    private final JTextField txtCurso = new JTextField(18);
    private final JComboBox<String> cmbTurno = new JComboBox<>(new String[] { "mañana", "tarde" });
    private final JLabel lblCurso = new JLabel("Curso:");
    private final JLabel lblTurno = new JLabel("Turno:");
    private final JButton btnRegistrar = new JButton("Registrarse");
    private final JLabel lblMsg = new JLabel(" ");
    private final JPanel panelRol = new JPanel(new GridBagLayout());

    private final UsuarioDAOImpl dao = new UsuarioDAOImpl();

    public Registro() {
        super("Cafetería IES — Registro");
        construirUI();
        configurarEventos();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void construirUI() {
        JPanel fondo = new JPanel(new GridBagLayout());
        fondo.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        fondo.setBackground(new Color(245, 245, 245));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        JLabel titulo = new JLabel("Nuevo usuario", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setForeground(new Color(80, 50, 20));
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 2;
        fondo.add(titulo, g);

        g.gridwidth = 1;
        int fila = 1;
        fila = agregarFila(fondo, g, fila, "Username:", txtUsername);
        fila = agregarFila(fondo, g, fila, "Contraseña:", txtPassword);
        fila = agregarFila(fondo, g, fila, "Email:", txtEmail);
        fila = agregarFila(fondo, g, fila, "Nombre:", txtNombre);
        fila = agregarFila(fondo, g, fila, "Apellidos:", txtApellidos);
        fila = agregarFila(fondo, g, fila, "DNI:", txtDni);

        g.gridy = fila;
        g.gridx = 0;
        fondo.add(new JLabel("Rol:"), g);
        g.gridx = 1;
        fondo.add(cmbRol, g);
        fila++;

        panelRol.setOpaque(false);
        g.gridy = fila;
        g.gridx = 0;
        g.gridwidth = 2;
        fondo.add(panelRol, g);
        fila++;

        actualizarPanelRol();

        btnRegistrar.setBackground(new Color(101, 67, 33));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setFont(new Font("SansSerif", Font.BOLD, 13));
        g.gridy = fila;
        g.gridx = 0;
        g.gridwidth = 2;
        fondo.add(btnRegistrar, g);
        fila++;

        lblMsg.setHorizontalAlignment(SwingConstants.CENTER);
        g.gridy = fila;
        fondo.add(lblMsg, g);
        fila++;

        JLabel lblVolver = new JLabel("<html><a href='#'>¿Ya tienes cuenta? Inicia sesión</a></html>");
        lblVolver.setHorizontalAlignment(SwingConstants.CENTER);
        lblVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        g.gridy = fila;
        fondo.add(lblVolver, g);
        lblVolver.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                new Login();
            }
        });

        setContentPane(fondo);
    }

    private int agregarFila(JPanel panel, GridBagConstraints g, int fila, String label, JComponent campo) {
        g.gridy = fila;
        g.gridx = 0;
        panel.add(new JLabel(label), g);
        g.gridx = 1;
        panel.add(campo, g);
        return fila + 1;
    }

    private void actualizarPanelRol() {
        panelRol.removeAll();
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        if ("cliente".equals(cmbRol.getSelectedItem())) {
            g.gridx = 0;
            g.gridy = 0;
            panelRol.add(lblCurso, g);
            g.gridx = 1;
            panelRol.add(txtCurso, g);
        } else {
            g.gridx = 0;
            g.gridy = 0;
            panelRol.add(lblTurno, g);
            g.gridx = 1;
            panelRol.add(cmbTurno, g);
        }
        panelRol.revalidate();
        panelRol.repaint();
        pack();
    }

    private void configurarEventos() {
        cmbRol.addActionListener(e -> actualizarPanelRol());
        btnRegistrar.addActionListener(e -> registrar());
    }

    private void registrar() {
        if (txtUsername.getText().isBlank() || txtNombre.getText().isBlank()
                || txtApellidos.getText().isBlank() || txtDni.getText().isBlank()
                || txtEmail.getText().isBlank()
                || new String(txtPassword.getPassword()).isBlank()) {
            mostrarError("Todos los campos son obligatorios.");
            return;
        }

        String rol = (String) cmbRol.getSelectedItem();
        boolean exito;

        if ("cliente".equals(rol)) {
            Cliente c = new Cliente(0,
                    txtUsername.getText().trim(),
                    new String(txtPassword.getPassword()),
                    txtEmail.getText().trim(),
                    txtNombre.getText().trim(),
                    txtApellidos.getText().trim(),
                    txtDni.getText().trim(),
                    txtCurso.getText().trim());
            exito = dao.registrarCliente(c);
        } else {
            Empleado e = new Empleado(0,
                    txtUsername.getText().trim(),
                    new String(txtPassword.getPassword()),
                    txtEmail.getText().trim(),
                    txtNombre.getText().trim(),
                    txtApellidos.getText().trim(),
                    txtDni.getText().trim(),
                    (String) cmbTurno.getSelectedItem());
            exito = dao.registrarEmpleado(e);
        }

        if (exito) {
            JOptionPane.showMessageDialog(this, "¡Registro exitoso! Ya puedes iniciar sesión.",
                    "Registro completado", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new Login();
        } else {
            mostrarError("Error al registrar. Username, email o DNI ya existen.");
        }
    }

    private void mostrarError(String msg) {
        lblMsg.setForeground(Color.RED);
        lblMsg.setText(msg);
    }
}
