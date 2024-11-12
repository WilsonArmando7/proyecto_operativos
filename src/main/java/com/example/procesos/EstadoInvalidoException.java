package com.example.procesos;
import com.example.procesos.EstadoInvalidoException;



    public class EstadoInvalidoException extends Exception {
        public EstadoInvalidoException(String mensaje) {
            super(mensaje);
        }
}

