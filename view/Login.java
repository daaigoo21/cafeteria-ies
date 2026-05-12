package view;

import dao.UsuarioDAOImpl;
import model.Usuario;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    private final JTextField txtUsername = new JTextField(20);
    private final JPasswordField txtPassword = new JPasswordField(20);
    private final JButton btnEntrar = new JButton("Entrar");
    private final JLabel lblError = new JLabel(" ");

    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

    public Login() {
        super("Cafetería IES Francisco Ayala — Login");
        construirUI();
        configurarEventos();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void construirUI() {
        JPanel fondo = new JPanel(new GridBagLayout());
        fondo.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        fondo.setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titulo = new JLabel("☕ Cafetería IES", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(new Color(80, 50, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        fondo.add(titulo, gbc);

        JLabel subtitulo = new JLabel("Francisco Ayala", SwingConstants.CENTER);
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitulo.setForeground(Color.GRAY);
        gbc.gridy = 1;
        fondo.add(subtitulo, gbc);

        gbc.gridy = 2;
        fondo.add(new JSeparator(), gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 3;
        gbc.gridx = 0;
        fondo.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        fondo.add(txtUsername, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        fondo.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        fondo.add(txtPassword, gbc);

        btnEntrar.setBackground(new Color(101, 67, 33));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        fondo.add(btnEntrar, gbc);

        lblError.setForeground(Color.RED);
        lblError.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 6;
        fondo.add(lblError, gbc);

        JLabel lblRegistro = new JLabel("<html><a href='#'>¿No tienes cuenta? Regístrate</a></html>");
        lblRegistro.setHorizontalAlignment(SwingConstants.CENTER);
        lblRegistro.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridy = 7;
        fondo.add(lblRegistro, gbc);
        lblRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                new Registro();
            }
        });

        setContentPane(fondo);
    }

    private void configurarEventos() {
        btnEntrar.addActionListener(e -> intentarLogin());
        txtPassword.addActionListener(e -> intentarLogin());
    }

    private void intentarLogin() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            lblError.setText("Por favor rellena todos los campos.");
            return;
        }

        Usuario usuario = usuarioDAO.validar(user, pass);
        if (usuario != null) {
            dispose();
            new Principal(usuario);
        } else {
            lblError.setText("Usuario o contraseña incorrectos.");
            txtPassword.setText("");
        }
    }
}
