package com.example.procesos;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ProgressBar;


public class Proceso {
    private IntegerProperty id;
    private StringProperty nombre;
    private IntegerProperty prioridad;
    private IntegerProperty memoria;

    private StringProperty estado;
    private int usoCPU;
    private int usoMemoria;
    private final ObjectProperty<ProgressBar> progressBar;




    // Constructor

    public Proceso(int id, String nombre, int prioridad, int memoria) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.prioridad = new SimpleIntegerProperty(prioridad);
        this.memoria = new SimpleIntegerProperty(memoria);
        this.estado = new SimpleStringProperty(EstadoProceso.LISTO.name());
        // Inicialización de estado

        // Inicializa la barra de progreso
        this.progressBar = new SimpleObjectProperty<>(new ProgressBar(0));
    }

    public ObjectProperty<ProgressBar> getProgressBarProperty() {
        return progressBar;
    }

    public void actualizarProgreso(double progreso) {
        progressBar.get().setProgress(progreso);
    }


    public StringProperty estadoProperty() {
        return estado;
    }

    // Getters para TableView
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public IntegerProperty prioridadProperty() {
        return prioridad;
    }

    public IntegerProperty memoriaProperty() {
        return memoria;
    }

    // Métodos adicionales
    public int getId() {
        return id.get();
    }

    public String getNombre() {
        return nombre.get();
    }

    public int getPrioridad() {
        return prioridad.get();
    }

    public int getMemoria() {
        return memoria.get();
    }

    public String getEstado() {
        return estado.get();
    }


    public void setId(int id) {
        this.id.set(id);
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public void setPrioridad(int prioridad) {
        this.prioridad.set(prioridad);
    }

    public void setMemoria(int memoria) {
        this.memoria.set(memoria);
    }

    public void setEstado(EstadoProceso estado) {
        this.estado.set(estado.name()); // Convierte el valor a String
    }

    public int getUsoCPU() {
        return usoCPU;
    }

    public void setUsoCPU(int usoCPU) {
        this.usoCPU = usoCPU;
    }

    public int getUsoMemoria() {
        return usoMemoria;
    }

    public void setUsoMemoria(int usoMemoria) {
        this.usoMemoria = usoMemoria;
    }




    @Override
    public String toString() {
        return "Proceso{" +
                "id=" + id.get() +
                ", nombre='" + nombre.get() + '\'' +
                ", prioridad=" + prioridad.get() +
                ", estado=" + estado + // Acceder a estado directamente
                ", memoria=" + memoria.get() +
                '}';
    }
}
