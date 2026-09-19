package com.rennancarneiro.patientcare.exception;

public class PacienteNaoEncontradoException extends RuntimeException {

    public PacienteNaoEncontradoException(Long id) {
        super("Paciente não encontrado com o id: " + id);
    }
}