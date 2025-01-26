import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CajeroAutomaticoGUI extends JFrame implements ActionListener {
    private JTextField tfUbicacion, tfNumeroTarjeta, tfPin, tfMonto;
    private JTextArea taResultados;
    private JButton btnSiguiente, btnRetirar, btnDepositar, btnSalir;
    private Connection myConn;

    public CajeroAutomaticoGUI() {
        // Configurar la ventana
        setTitle("Cajero Automático");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Inicializar componentes
        tfUbicacion = new JTextField(20);
        tfNumeroTarjeta = new JTextField(20);
        tfPin = new JTextField(20);
        tfMonto = new JTextField(20);
        taResultados = new JTextArea(10, 30);
        btnSiguiente = new JButton("Siguiente");
        btnRetirar = new JButton("Retirar");
        btnDepositar = new JButton("Depositar");
        btnSalir = new JButton("Salir");

        // Agregar componentes a la ventana
        add(new JLabel("Ubicación:"));
        add(tfUbicacion);
        add(btnSiguiente);
        add(new JLabel("Número de tarjeta:"));
        add(tfNumeroTarjeta);
        add(new JLabel("PIN:"));
        add(tfPin);
        add(btnRetirar);
        add(btnDepositar);
        add(new JLabel("Monto:"));
        add(tfMonto);
        add(taResultados);
        add(btnSalir);

        // Configurar acciones de los botones
        btnSiguiente.addActionListener(this);
        btnRetirar.addActionListener(this);
        btnDepositar.addActionListener(this);
        btnSalir.addActionListener(this);

        // Deshabilitar campos de tarjeta y PIN hasta que se seleccione una ubicación
        tfNumeroTarjeta.setEnabled(false);
        tfPin.setEnabled(false);
        btnRetirar.setEnabled(false);
        btnDepositar.setEnabled(false);
        tfMonto.setEnabled(false);

        // Conectar a la base de datos
        try {
            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cajeroautomaticobd", "root", "Rb31415926535");
        } catch (SQLException e) {
            taResultados.setText("Error de conexión a la base de datos: " + e.getMessage());
        }

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSiguiente) {
            // Procesar la ubicación
            String ubicacionSeleccionada = tfUbicacion.getText();
            tfNumeroTarjeta.setEnabled(true);
            tfPin.setEnabled(true);
            taResultados.setText("Ubicación seleccionada: " + ubicacionSeleccionada);
        } else if (e.getSource() == btnRetirar) {
            // Lógica para retirar dinero
            realizarRetiro();
        } else if (e.getSource() == btnDepositar) {
            // Lógica para depositar dinero
            realizarDeposito();
        } else if (e.getSource() == btnSalir) {
            System.exit(0);
        }
    }

    private void realizarRetiro() {
        String numeroTarjeta = tfNumeroTarjeta.getText();
        String pin = tfPin.getText();
        double montoRetiro = Double.parseDouble(tfMonto.getText());

        // Lógica de retiro aquí (igual a tu código anterior)
        // ...

        taResultados.setText("Retiro exitoso."); // Muestra el resultado
    }

    private void realizarDeposito() {
        String numeroTarjeta = tfNumeroTarjeta.getText();
        String pin = tfPin.getText();
        double montoDeposito = Double.parseDouble(tfMonto.getText());

        // Lógica de depósito aquí (igual a tu código anterior)
        // ...

        taResultados.setText("Depósito exitoso."); // Muestra el resultado
    }

    public static void main(String[] args) {
        new CajeroAutomaticoGUI();
    }
}

