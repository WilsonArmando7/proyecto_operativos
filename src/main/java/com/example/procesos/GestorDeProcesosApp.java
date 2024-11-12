package com.example.procesos;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GestorDeProcesosApp extends Application {

    private GestorDeProcesos gestor;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sistema de Gestión de Procesos");

        // Inicializar el gestor de procesos con 1000 MB de memoria total
        gestor = new GestorDeProcesos(new InterfazPrincipal(), 1000);

        // Configuración básica de la interfaz (por ahora solo una estructura vacía)
        VBox layoutPrincipal = new VBox();
        Scene scene = new Scene(layoutPrincipal, 400, 300);
        primaryStage.setScene(scene);

        // Mostrar la ventana
        primaryStage.show();

        // Llamamos a métodos para crear y planificar procesos
        iniciarProcesos();
    }

    private void iniciarProcesos() {
        try {
            gestor.crearProceso(1, "Proceso1", 300, 200);
            gestor.crearProceso(2, "Proceso2", 200, 300);
            gestor.crearProceso(3, "Proceso3", 400, 500);
        } catch (MemoriaInsuficienteException e) {
            System.out.println("Error al crear el proceso: " + e.getMessage());
        }

        gestor.mostrarProcesos();

        try {
            gestor.planificarProcesos(100); // Asumiendo que este método existe
        } finally {
            System.out.println("Planificación de procesos finalizada.");
        }
    }

    public static void main(String[] args) {
        launch(args); // Lanza la aplicación JavaFX
    }
}
