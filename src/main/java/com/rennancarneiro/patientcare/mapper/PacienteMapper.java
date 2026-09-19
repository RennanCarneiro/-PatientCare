package com.rennancarneiro.patientcare.mapper;

import com.rennancarneiro.patientcare.dto.PacienteRequestDTO;
import com.rennancarneiro.patientcare.dto.PacienteResponseDTO;
import com.rennancarneiro.patientcare.model.Paciente;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {

    public Paciente toEntity(PacienteRequestDTO dto) {

        Paciente paciente = new Paciente();

        paciente.setNome(dto.getNome());
        paciente.setSexo(dto.getSexo());
        paciente.setCpf(dto.getCpf());
        paciente.setDataNascimento(dto.getDataNascimento());
        paciente.setConvenio(dto.getConvenio());
        paciente.setTelefone(dto.getTelefone());

        return paciente;
    }

    public PacienteResponseDTO toResponseDTO(Paciente paciente) {

        PacienteResponseDTO dto = new PacienteResponseDTO();

        dto.setId(paciente.getId());
        dto.setNome(paciente.getNome());
        dto.setSexo(paciente.getSexo());
        dto.setCpf(paciente.getCpf());
        dto.setDataNascimento(paciente.getDataNascimento());
        dto.setConvenio(paciente.getConvenio());
        dto.setTelefone(paciente.getTelefone());

        return dto;
    }

    public void updateEntity(Paciente paciente, PacienteRequestDTO dto) {

        paciente.setNome(dto.getNome());
        paciente.setSexo(dto.getSexo());
        paciente.setCpf(dto.getCpf());
        paciente.setDataNascimento(dto.getDataNascimento());
        paciente.setConvenio(dto.getConvenio());
        paciente.setTelefone(dto.getTelefone());
    }
}