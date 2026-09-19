package com.rennancarneiro.patientcare.service;
import com.rennancarneiro.patientcare.dto.PacienteRequestDTO;
import com.rennancarneiro.patientcare.dto.PacienteResponseDTO;
import java.util.List;

public interface PacienteService {
    PacienteResponseDTO criar(PacienteRequestDTO dto);

    List<PacienteResponseDTO> listar();

    PacienteResponseDTO buscarPorId(Long id);

    PacienteResponseDTO atualizar(Long id, PacienteRequestDTO dto);

    void deletar(Long id);
}
