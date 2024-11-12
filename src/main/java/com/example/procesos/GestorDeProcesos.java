package com.example.procesos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.animation.PauseTransition;


public class GestorDeProcesos {
    private List<Proceso> listaProcesos;
    private int memoriaTotal;
    private int memoriaUsada;
    private Timeline timeline;
    private InterfazPrincipal interfazPrincipal;

    // Constructor
    public GestorDeProcesos(InterfazPrincipal interfazPrincipal, int memoriaTotal) {
        this.interfazPrincipal = interfazPrincipal;
        listaProcesos = new ArrayList<>();
        this.memoriaTotal = memoriaTotal;
        this.memoriaUsada = 0;

        // Inicializar y actualizar tabla
        interfazPrincipal.actualizarTabla();
    }

    // Método planificarProcesos que acepta un entero
    public void planificarProcesos(int tiempo) {
        System.out.println("Planificación de procesos en ejecución por " + tiempo + " unidades de tiempo.");
    }

    // Método para crear un nuevo proceso y añadirlo a la lista
    public void crearProceso(int id, String nombre, int prioridad, int memoria) throws MemoriaInsuficienteException {
        // Verificar si el ID ya existe en la lista de procesos
        for (Proceso proceso : listaProcesos) {
            if (proceso.getId() == id) {
                System.out.println("Error: El ID " + id + " ya está en uso. Por favor, elija otro ID.");
                return; // Termina el método sin crear el proceso
            }
        }




        if (memoriaUsada + memoria > memoriaTotal) {
            throw new MemoriaInsuficienteException("No hay suficiente memoria para crear el proceso " + nombre);
        }
        Proceso proceso = new Proceso(id, nombre, prioridad, memoria);
        listaProcesos.add(proceso);
        memoriaUsada += memoria;
        System.out.println("Proceso creado: " + proceso + ", Memoria asignada: " + memoria + "MB");
    }

    // Método para mostrar todos los procesos
    public void mostrarProcesos() {
        if (listaProcesos.isEmpty()) {
            System.out.println("No hay procesos en el sistema.");
        } else {
            System.out.println("Lista de procesos:");
            for (Proceso proceso : listaProcesos) {
                System.out.println(proceso);
            }
        }
    }

    // Método para terminar un proceso y liberar recursos
    public void terminarProceso(Proceso proceso) {
        proceso.setEstado(EstadoProceso.TERMINADO);
        memoriaUsada -= proceso.getMemoria();
        System.out.println("Proceso terminado: " + proceso.getId());
    }

    // Método para iniciar la planificación
    public void iniciarPlanificacion(String algoritmo) {
        switch (algoritmo) {
            case "Round Robin":
                planificarRoundRobin();
                break;
            case "Prioridad":
                planificarPorPrioridad();
                break;
            default:
                System.out.println("Algoritmo no soportado.");
        }
    }

    // Algoritmo de Round Robin
    private void planificarRoundRobin() {
        int quantum = 2;

        timeline = new Timeline(new KeyFrame(Duration.seconds(quantum), event -> {
            boolean procesoEjecutado = false;

            for (Proceso proceso : listaProcesos) {
                if (proceso.getEstado().equals(EstadoProceso.LISTO.name())) {
                    proceso.setEstado(EstadoProceso.EJECUTANDO);
                    interfazPrincipal.actualizarProcesoEnLista(proceso); // Actualiza el estado a EJECUTANDO
                    System.out.println("Ejecutando proceso: " + proceso.getId());

                    // Simula el uso de CPU y memoria
                    proceso.setUsoCPU((int) (Math.random() * 100)); // Valor aleatorio de uso de CPU entre 0 y 100%
                    proceso.setUsoMemoria((int) (Math.random() * proceso.getMemoria())); // Valor aleatorio de uso de memoria
                    // Actualiza el proceso en la lista observable de InterfazPrincipal
                    interfazPrincipal.actualizarProcesoEnLista(proceso);

                    // Simulación del progreso en la barra
                    new Thread(() -> {
                        for (int i = 0; i <= 100; i += 10) { // Incrementa el progreso de 0 a 100%
                            try {
                                Thread.sleep(quantum * 200); // Pausa para cada incremento, ajusta según el quantum
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            double progreso = i / 100.0;
                            proceso.actualizarProgreso(progreso); // Actualiza el progreso en el proceso
                            interfazPrincipal.actualizarProcesoEnLista(proceso); // Refresca en la interfaz
                        }

                        // Cambia el estado a LISTO una vez que el progreso llega a 100%
                        proceso.setEstado(EstadoProceso.LISTO);
                        proceso.actualizarProgreso(0); // Restablece el progreso para la siguiente ejecución
                        interfazPrincipal.actualizarProcesoEnLista(proceso); // Refresca en la interfaz
                    }).start();

                    procesoEjecutado = true;
                    break;
                }
            }

            if (!procesoEjecutado) {
                timeline.stop();
                System.out.println("Todos los procesos han sido ejecutados o están en espera.");
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


    // Algoritmo por Prioridad
    private void planificarPorPrioridad() {
        listaProcesos.sort(Comparator.comparingInt(Proceso::getPrioridad));
        for (Proceso proceso : listaProcesos) {
            if ("LISTO".equals(proceso.getEstado())) {
                proceso.setEstado(EstadoProceso.EJECUTANDO);
                System.out.println("Ejecutando proceso con prioridad: " + proceso.getId());
                proceso.setEstado(EstadoProceso.LISTO);
            }
        }
    }
}
