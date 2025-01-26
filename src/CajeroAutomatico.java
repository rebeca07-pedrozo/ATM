public class CajeroAutomatico {
    private int idCajero;
    private String ubicacion;
    private double saldoDisponible;

    public CajeroAutomatico(int idCajero, String ubicacion, double saldoDisponible) {
        this.idCajero = idCajero;
        this.ubicacion = ubicacion;
        this.saldoDisponible = saldoDisponible;
    }

    public double getSaldoDisponible() {
        return saldoDisponible;
    }

    public void setSaldoDisponible(double saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }
}

