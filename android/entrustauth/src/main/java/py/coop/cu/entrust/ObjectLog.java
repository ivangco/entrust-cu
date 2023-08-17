package py.coop.cu.entrust;

import java.time.LocalDateTime;

public class ObjectLog {

    private String metodo;
    private String parametroEntrada;
    private String respuestaSalida;
    private String estado;
    private String mensaje;
    private String fechaHora;

    public ObjectLog() {
        this.fechaHora = LocalDateTime.now().toString();
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getParametroEntrada() {
        return parametroEntrada;
    }

    public void setParametroEntrada(String parametroEntrada) {
        this.parametroEntrada = parametroEntrada;
    }

    public String getRespuestaSalida() {
        return respuestaSalida;
    }

    public void setRespuestaSalida(String respuestaSalida) {
        this.respuestaSalida = respuestaSalida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return "ObjectLog{" +
                "metodo='" + metodo + '\'' +
                ", parametroEntrada='" + parametroEntrada + '\'' +
                ", respuestaSalida='" + respuestaSalida + '\'' +
                ", estado='" + estado + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", fechaHora=" + fechaHora +
                '}';
    }
}
