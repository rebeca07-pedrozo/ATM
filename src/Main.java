import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Connection myConn = null;
        PreparedStatement pstmt = null;
        ResultSet myRes = null;
        Scanner scanner = new Scanner(System.in);

        try {
            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cajeroautomaticobd", "root", "Rb31415926535");
            System.out.println("CONEXION A DB EXITOSA");

            String ubicacionSeleccionada;
            String numeroTarjeta;
            String pin;
            int opcion;
            boolean continuar = true;

            System.out.println("Ubicaciones disponibles:");
            String ubicacionesSql = "SELECT ubicacion FROM CajeroAutomatico";
            pstmt = myConn.prepareStatement(ubicacionesSql);
            ResultSet ubicacionesRes = pstmt.executeQuery();

            while (ubicacionesRes.next()) {
                System.out.println("- " + ubicacionesRes.getString("ubicacion"));
            }

            System.out.print("Seleccione una ubicación: ");
            ubicacionSeleccionada = scanner.nextLine();

            while (continuar) {
                System.out.print("Ingrese su número de tarjeta: ");
                numeroTarjeta = scanner.nextLine();
                System.out.print("Ingrese su PIN: ");
                pin = scanner.nextLine();

                String pinSql = "SELECT COUNT(*) AS count FROM Tarjeta WHERE numero_tarjeta = ? AND clave = ?";
                pstmt = myConn.prepareStatement(pinSql);
                pstmt.setString(1, numeroTarjeta);
                pstmt.setString(2, pin);
                ResultSet pinRes = pstmt.executeQuery();
                pinRes.next();
                int count = pinRes.getInt("count");

                if (count == 0) {
                    System.out.println("Número de tarjeta o PIN incorrecto.");
                    System.out.println("1. Volver al menú de opciones");
                    System.out.println("2. Salir");
                    int decision = scanner.nextInt();
                    scanner.nextLine(); // Limpiar el buffer

                    if (decision == 2) {
                        System.out.println("Gracias por usar nuestros servicios.");
                        continuar = false; // Salir del bucle
                    }
                    continue;
                }

                String sql = "SELECT c.nombre, c.apellidos, cu.saldo_en_la_cuenta, "
                        + "CASE WHEN cu.estado_cuenta = 'activo' THEN 'cuenta activa' "
                        + "     ELSE 'cuenta inactiva' END AS estado_cuenta "
                        + "FROM Cliente c "
                        + "JOIN Tarjeta t ON c.id_cliente = t.id_cliente "
                        + "JOIN Cuenta cu ON cu.id_cliente = c.id_cliente "
                        + "WHERE t.numero_tarjeta = ?";

                pstmt = myConn.prepareStatement(sql);
                pstmt.setString(1, numeroTarjeta);
                myRes = pstmt.executeQuery();

                if (myRes.next()) {
                    String nombre = myRes.getString("nombre");
                    String apellidos = myRes.getString("apellidos");
                    double saldoEnCuenta = myRes.getDouble("saldo_en_la_cuenta");
                    String estadoCuenta = myRes.getString("estado_cuenta");

                    if ("cuenta activa".equals(estadoCuenta)) {
                        System.out.println("Bienvenido " + nombre + " " + apellidos + "!");

                        do {
                            System.out.println("Seleccione una opción:");
                            System.out.println("1. Retirar dinero");
                            System.out.println("2. Depositar dinero");
                            System.out.println("3. Salir");
                            opcion = scanner.nextInt();

                            switch (opcion) {
                                case 1:
                                    System.out.println("Su saldo en la cuenta es: " + saldoEnCuenta);
                                    System.out.print("Ingrese el monto a retirar: ");
                                    double montoRetiro = scanner.nextDouble();

                                    String saldoCajeroSql = "SELECT saldo_disponible_cajero FROM CajeroAutomatico WHERE ubicacion = ?";
                                    pstmt = myConn.prepareStatement(saldoCajeroSql);
                                    pstmt.setString(1, ubicacionSeleccionada);
                                    ResultSet saldoCajeroRes = pstmt.executeQuery();
                                    double saldoDisponibleCajero = 0;

                                    if (saldoCajeroRes.next()) {
                                        saldoDisponibleCajero = saldoCajeroRes.getDouble("saldo_disponible_cajero");
                                    }

                                    if (montoRetiro > saldoEnCuenta) {
                                        System.out.println("No tiene los fondos suficientes para realizar este retiro de su cuenta.");
                                    } else if (montoRetiro > saldoDisponibleCajero) {
                                        System.out.println("El cajero no tiene los fondos suficientes para realizar este retiro.");
                                    } else {
                                        saldoEnCuenta -= montoRetiro;
                                        System.out.println("Retiro exitoso. Su saldo actual es: " + saldoEnCuenta);

                                        System.out.print("¿Desea hacer más operaciones? (1. Sí / 2. No): ");
                                        int masOperaciones = scanner.nextInt();
                                        if (masOperaciones == 2) {
                                            System.out.println("Gracias, " + nombre + " " + apellidos + ", por usar nuestros servicios en " + ubicacionSeleccionada + ". ¡Vuelva pronto!");
                                            continuar = false;
                                        }
                                    }
                                    break;

                                case 2:
                                    System.out.println("Su saldo en la cuenta es: " + saldoEnCuenta);
                                    System.out.print("Ingrese el monto a depositar: ");
                                    double montoDeposito = scanner.nextDouble();
                                    saldoEnCuenta += montoDeposito;
                                    System.out.println("Depósito exitoso. Su nuevo saldo es: " + saldoEnCuenta);

                                    System.out.print("¿Desea hacer más operaciones? (1. Sí / 2. No): ");
                                    int masOperacionesDep = scanner.nextInt();
                                    if (masOperacionesDep == 2) {
                                        System.out.println("Gracias, " + nombre + " " + apellidos + ", por usar nuestros servicios en " + ubicacionSeleccionada + ". ¡Vuelva pronto!");
                                        continuar = false;
                                    }
                                    break;

                                case 3:
                                    System.out.println("Gracias, " + nombre + " " + apellidos + ", por usar nuestros servicios en " + ubicacionSeleccionada + ". ¡Vuelva pronto!");
                                    continuar = false;
                                    break;

                                default:
                                    System.out.println("Opción no válida. Intente de nuevo.");
                            }
                        } while (continuar);
                    } else {
                        System.out.println("La cuenta está inactiva.");
                    }
                } else {
                    System.out.println("No se encontró información para la tarjeta proporcionada.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (myRes != null) myRes.close();
                if (pstmt != null) pstmt.close();
                if (myConn != null) myConn.close();
                scanner.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}







