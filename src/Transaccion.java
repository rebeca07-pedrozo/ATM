public class Transaccion {
    private int idTransaccion;
    private String tipo;
    private double monto;

    public Transaccion(int idTransaccion, String tipo, double monto) {
        this.idTransaccion = idTransaccion;
        this.tipo = tipo;
        this.monto = monto;
    }

    public String getTipo() {
        return tipo;
    }

    public double getMonto() {
        return monto;
    }
}
