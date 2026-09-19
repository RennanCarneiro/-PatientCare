package com.rennancarneiro.patientcare.mapper;

import com.rennancarneiro.patientcare.dto.PacienteRequestDTO;
import com.rennancarneiro.patientcare.model.Paciente;
import com.rennancarneiro.patientcare.model.Sexo;
import org.junit.jupiter.api.Test;
import com.rennancarneiro.patientcare.dto.PacienteResponseDTO;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PacienteMapperTest {

    @Test
    void deveConverterRequestDTOParaPaciente() {

        // Arrange
        PacienteRequestDTO dto = new PacienteRequestDTO();

        dto.setNome("Maria Silva");
        dto.setSexo(Sexo.FEMININO);
        dto.setCpf("12345678900");
        dto.setDataNascimento(LocalDate.of(1995, 8, 20));
        dto.setConvenio("Unimed");
        dto.setTelefone("81999999999");

        PacienteMapper mapper = new PacienteMapper();

        // Act
        Paciente paciente = mapper.toEntity(dto);

        // Assert
        assertEquals("Maria Silva", paciente.getNome());
        assertEquals(Sexo.FEMININO, paciente.getSexo());
        assertEquals("12345678900", paciente.getCpf());
        assertEquals(LocalDate.of(1995, 8, 20), paciente.getDataNascimento());
        assertEquals("Unimed", paciente.getConvenio());
        assertEquals("81999999999", paciente.getTelefone());
    }

    @Test
    void deveConverterPacienteParaResponseDTO() {

        // Arrange
        Paciente paciente = new Paciente();

        paciente.setNome("João Silva");
        paciente.setSexo(Sexo.MASCULINO);
        paciente.setCpf("12345678900");
        paciente.setDataNascimento(LocalDate.of(1990, 5, 10));
        paciente.setConvenio("Unimed");
        paciente.setTelefone("81988888888");

        PacienteMapper mapper = new PacienteMapper();

        // Act
        PacienteResponseDTO dto = mapper.toResponseDTO(paciente);

        // Assert
        assertEquals("João Silva", dto.getNome());
        assertEquals(Sexo.MASCULINO, dto.getSexo());
        assertEquals("12345678900", dto.getCpf());
        assertEquals(LocalDate.of(1990, 5, 10), dto.getDataNascimento());
        assertEquals("Unimed", dto.getConvenio());
        assertEquals("81988888888", dto.getTelefone());
    }

    @Test
    void deveAtualizarPacienteComDadosDoRequestDTO() {

        // Arrange
        Paciente paciente = new Paciente();

        paciente.setNome("Maria Silva");
        paciente.setConvenio("Unimed");

        PacienteRequestDTO dto = new PacienteRequestDTO();

        dto.setNome("Maria Souza");
        dto.setSexo(Sexo.FEMININO);
        dto.setCpf("98765432100");
        dto.setDataNascimento(LocalDate.of(1995, 8, 20));
        dto.setConvenio("Hapvida");
        dto.setTelefone("81977777777");

        PacienteMapper mapper = new PacienteMapper();

        // Act
        mapper.updateEntity(paciente, dto);

        // Assert
        assertEquals("Maria Souza", paciente.getNome());
        assertEquals(Sexo.FEMININO, paciente.getSexo());
        assertEquals("98765432100", paciente.getCpf());
        assertEquals(LocalDate.of(1995, 8, 20), paciente.getDataNascimento());
        assertEquals("Hapvida", paciente.getConvenio());
        assertEquals("81977777777", paciente.getTelefone());
    }
}