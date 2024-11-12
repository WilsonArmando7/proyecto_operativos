package com.example.procesos;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ProgressBar;




public class InterfazPrincipal extends Application {

    public void actualizarTabla() {
        if (tablaProcesos != null) {
            tablaProcesos.refresh();
        }
    }



    private GestorDeProcesos gestorDeProcesos; // Instancia de GestorDeProcesos
    private ObservableList<Proceso> procesosData; // Lista observable para la tabla
    private TableView<Proceso> tablaProcesos;


    // Método privado renombrado para evitar conflicto.
    private void refrescarTabla() {
        tablaProcesos.refresh(); // Refresca la vista de la tabla.// Refresca la vista de la tabla para mostrar cambios
    }


    public void actualizarProcesoEnLista(Proceso proceso) {
        int index = procesosData.indexOf(proceso);
        if (index >= 0) {
            procesosData.set(index, proceso);
        } else {
            procesosData.add(proceso); // Si el proceso no está, agregarlo
        }
        tablaProcesos.refresh(); // Refresca la tabla después de actualizar la lista
    }




    @Override
    public void start(Stage primaryStage) {
        // Inicializar el GestorDeProcesos y la lista observable
        gestorDeProcesos = new GestorDeProcesos(this, 1024); // En `start` o donde lo declares.
        procesosData = FXCollections.observableArrayList();



        // Crear etiquetas y campos de texto para la entrada de datos
        Label idLabel = new Label("ID del Proceso:");
        TextField idField = new TextField();

        Label memoriaLabel = new Label("Memoria Requerida:");
        TextField memoriaField = new TextField();

        Label cpuLabel = new Label("CPU Requerido:");
        TextField cpuField = new TextField();

        Label prioridadLabel = new Label("Prioridad del Proceso:");
        TextField prioridadField = new TextField();

        // Botón para crear el proceso
        Button crearProcesoButton = new Button("Crear Proceso");
        // Botón para terminar el proceso seleccionado
        Button terminarProcesoButton = new Button("Terminar Proceso");

        // ComboBox para seleccionar el algoritmo de planificación
        Label algoritmoLabel = new Label("Algoritmo de Planificación:");
        ComboBox<String> algoritmoComboBox = new ComboBox<>();
        algoritmoComboBox.getItems().addAll("Round Robin", "Prioridad"); // Puedes agregar más algoritmos si lo deseas
        algoritmoComboBox.setValue("Round Robin"); // Valor por defecto

        // Botón para iniciar la planificación
        Button iniciarPlanificacionButton = new Button("Iniciar Planificación");

        // Estilo de los botones
        crearProcesoButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Verde con texto blanco
        iniciarPlanificacionButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;"); // Azul con texto blanco
        terminarProcesoButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;"); // Rojo con texto blanco


        // Configuración de evento para el botón de iniciar planificación
        iniciarPlanificacionButton.setOnAction(e -> {
            String algoritmoSeleccionado = algoritmoComboBox.getValue();

            // Llamar al método de planificación en GestorDeProcesos según el algoritmo seleccionado
            gestorDeProcesos.iniciarPlanificacion(algoritmoSeleccionado);

            // Refrescar la tabla cada segundo para reflejar los cambios de estado
            Timeline refrescarTabla = new Timeline(new KeyFrame(Duration.seconds(1), event -> tablaProcesos.refresh()));
            refrescarTabla.setCycleCount(Timeline.INDEFINITE); // Repetir indefinidamente mientras el Timeline esté activo
            refrescarTabla.play();
        });

        // Configuración de evento para el botón de crear proceso
        crearProcesoButton.setOnAction(e -> {
            try {
                // Leer los valores ingresados en los campos de texto
                int id = Integer.parseInt(idField.getText());
                String nombre = "Proceso-" + id;
                int memoria = Integer.parseInt(memoriaField.getText());
                int prioridad = Integer.parseInt(prioridadField.getText());

                // Llamar al método crearProceso en GestorDeProcesos sin asignarlo a una variable
                gestorDeProcesos.crearProceso(id, nombre, prioridad, memoria);

                // Añadir el proceso a la lista observable para mostrar en la tabla
                procesosData.add(new Proceso(id, nombre, prioridad, memoria));

            } catch (NumberFormatException ex) {
                System.out.println("Error: Asegúrese de que todos los campos estén correctamente llenos.");
            } catch (MemoriaInsuficienteException ex) {
                System.out.println(ex.getMessage());
            }
        });

        // Configuración de evento para el botón de terminar proceso
        terminarProcesoButton.setOnAction(e -> {
            // Obtener el proceso seleccionado en la tabla
            Proceso procesoSeleccionado = tablaProcesos.getSelectionModel().getSelectedItem();

            if (procesoSeleccionado != null) {
                // Llamar al método terminarProceso en GestorDeProcesos
                gestorDeProcesos.terminarProceso(procesoSeleccionado);

                // Eliminar el proceso de la lista observable para actualizar la tabla
                procesosData.remove(procesoSeleccionado);

                System.out.println("Proceso terminado: ID=" + procesoSeleccionado.getId());
            } else {
                System.out.println("Seleccione un proceso para terminar.");
            }
        });

        // Configurar la tabla
        procesosData = FXCollections.observableArrayList();
        tablaProcesos = new TableView<>(procesosData);

        // Crear las columnas de la tabla
        TableColumn<Proceso, Integer> columnaID = new TableColumn<>("ID");
        columnaID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        TableColumn<Proceso, Integer> columnaMemoria = new TableColumn<>("Memoria");
        columnaMemoria.setCellValueFactory(cellData -> cellData.getValue().memoriaProperty().asObject());

        // Verifica si necesitas o tienes el método cpuProperty en Proceso. Si no, elimina esta columna
        TableColumn<Proceso, Integer> columnaCPU = new TableColumn<>("CPU");
        //columnaCPU.setCellValueFactory(cellData -> cellData.getValue().cpuProperty().asObject());

        TableColumn<Proceso, Integer> columnaPrioridad = new TableColumn<>("Prioridad");
        columnaPrioridad.setCellValueFactory(cellData -> cellData.getValue().prioridadProperty().asObject());

        TableColumn<Proceso, String> columnaEstado = new TableColumn<>("Estado");
        columnaEstado.setCellValueFactory(cellData -> cellData.getValue().estadoProperty());

        TableColumn<Proceso, Integer> columnaUsoCPU = new TableColumn<>("Uso de CPU (%)");
        columnaUsoCPU.setCellValueFactory(new PropertyValueFactory<>("usoCPU"));

        TableColumn<Proceso, Integer> columnaUsoMemoria = new TableColumn<>("Uso de Memoria (MB)");
        columnaUsoMemoria.setCellValueFactory(new PropertyValueFactory<>("usoMemoria"));

        // Declara la barra de progreso como una columna adicional en la tabla
        TableColumn<Proceso, ProgressBar> columnaProgreso = new TableColumn<>("Progreso");
        columnaProgreso.setCellValueFactory(cellData -> cellData.getValue().getProgressBarProperty());

        // Añadir las columnas a la tabla
        tablaProcesos.getColumns().addAll(columnaID, columnaMemoria, columnaPrioridad, columnaEstado,columnaProgreso);

        // Layout para la interfaz
        VBox layout = new VBox(10);
        layout.getChildren().addAll(idLabel, idField, memoriaLabel, memoriaField, cpuLabel, cpuField, prioridadLabel, prioridadField, algoritmoLabel, algoritmoComboBox, crearProcesoButton, iniciarPlanificacionButton, terminarProcesoButton, tablaProcesos);

        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #FF5733, #FFC300);");



        // Configuración de la escena y la ventana principal
        Scene scene = new Scene(layout, 600, 500);
        primaryStage.setTitle("Sistema de Gestión de Procesos");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

