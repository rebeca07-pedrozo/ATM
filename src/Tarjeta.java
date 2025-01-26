public class Tarjeta {
    private String numeroTarjeta;
    private String fechaExpiracion;
    private String pin;
    private boolean bloqueada;

    public Tarjeta(String numeroTarjeta, String fechaExpiracion, String pin, boolean bloqueada) {
        this.numeroTarjeta = numeroTarjeta;
        this.fechaExpiracion = fechaExpiracion;
        this.pin = pin;
        this.bloqueada = bloqueada;
    }

    public boolean isBloqueada() {
        return bloqueada;
    }

    public boolean validarPin(String pin) {
        return this.pin.equals(pin);
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }
}
