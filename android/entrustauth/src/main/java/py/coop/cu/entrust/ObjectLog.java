package py.coop.cu.entrust;

import java.time.LocalDateTime;

public class ObjectLog {

    private String metodo;
    private String parametrosEntrada;
    private String respuestaSalida;
    private String estado;
    private String mensaje;
    private String fechaHora;

    public ObjectLog() {
        this.fechaHora = LocalDateTime.now().toString();
        this.estado = "ok";
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
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

    public String getParametrosEntrada() {
        return parametrosEntrada;
    }

    public void setParametrosEntrada(String parametrosEntrada) {
        this.parametrosEntrada = parametrosEntrada;
    }

    @Override
    public String toString() {
        return "ObjectLog{" +
                "metodo='" + metodo + '\'' +
                ", parametrosEntrada='" + parametrosEntrada + '\'' +
                ", respuestaSalida='" + respuestaSalida + '\'' +
                ", estado='" + estado + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", fechaHora='" + fechaHora + '\'' +
                '}';
    }
}

