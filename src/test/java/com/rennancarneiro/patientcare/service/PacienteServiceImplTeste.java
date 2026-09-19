package com.rennancarneiro.patientcare.service;

import com.rennancarneiro.patientcare.dto.PacienteRequestDTO;
import com.rennancarneiro.patientcare.dto.PacienteResponseDTO;
import com.rennancarneiro.patientcare.mapper.PacienteMapper;
import com.rennancarneiro.patientcare.model.Paciente;
import com.rennancarneiro.patientcare.repository.PacienteRepository;
import com.rennancarneiro.patientcare.service.impl.PacienteServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import java.util.Optional;
import com.rennancarneiro.patientcare.exception.PacienteNaoEncontradoException;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PacienteServiceImplTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private PacienteMapper pacienteMapper;

    @InjectMocks
    private PacienteServiceImpl pacienteService;

    @Test
    void deveCriarPaciente() {

        // Arrange
        PacienteRequestDTO requestDTO = new PacienteRequestDTO();
        Paciente paciente = new Paciente();

        PacienteResponseDTO responseDTO = new PacienteResponseDTO();
        responseDTO.setNome("Maria Silva");

        when(pacienteMapper.toEntity(requestDTO))
                .thenReturn(paciente);

        when(pacienteRepository.save(paciente))
                .thenReturn(paciente);

        when(pacienteMapper.toResponseDTO(paciente))
                .thenReturn(responseDTO);

        // Act
        PacienteResponseDTO resultado = pacienteService.criar(requestDTO);

        // Assert
        assertEquals("Maria Silva", resultado.getNome());

        verify(pacienteRepository).save(paciente);
    }

    @Test
    void deveListarPacientes() {

        // Arrange
        Paciente paciente1 = new Paciente();
        paciente1.setNome("Maria Silva");

        Paciente paciente2 = new Paciente();
        paciente2.setNome("João Souza");

        PacienteResponseDTO response1 = new PacienteResponseDTO();
        response1.setNome("Maria Silva");

        PacienteResponseDTO response2 = new PacienteResponseDTO();
        response2.setNome("João Souza");

        when(pacienteRepository.findAll())
                .thenReturn(List.of(paciente1, paciente2));

        when(pacienteMapper.toResponseDTO(paciente1))
                .thenReturn(response1);

        when(pacienteMapper.toResponseDTO(paciente2))
                .thenReturn(response2);

        // Act
        List<PacienteResponseDTO> resultado = pacienteService.listar();

        // Assert
        assertEquals(2, resultado.size());
        assertEquals("Maria Silva", resultado.get(0).getNome());
        assertEquals("João Souza", resultado.get(1).getNome());

        verify(pacienteRepository).findAll();
    }

    @Test
    void deveBuscarPacientePorId() {

        // Arrange
        Long id = 1L;

        Paciente paciente = new Paciente();
        paciente.setNome("Maria Silva");

        PacienteResponseDTO responseDTO = new PacienteResponseDTO();
        responseDTO.setNome("Maria Silva");

        when(pacienteRepository.findById(id))
                .thenReturn(Optional.of(paciente));

        when(pacienteMapper.toResponseDTO(paciente))
                .thenReturn(responseDTO);

        // Act
        PacienteResponseDTO resultado = pacienteService.buscarPorId(id);

        // Assert
        assertEquals("Maria Silva", resultado.getNome());

        verify(pacienteRepository).findById(id);
    }
    @Test
    void deveLancarExcecaoQuandoPacienteNaoExistir() {

        // Arrange
        Long id = 99L;

        when(pacienteRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        PacienteNaoEncontradoException exception = assertThrows(
                PacienteNaoEncontradoException.class,
                () -> pacienteService.buscarPorId(id)
        );

        assertEquals(
                "Paciente não encontrado com o id: 99",
                exception.getMessage()
        );

        verify(pacienteRepository).findById(id);
    }
    @Test
    void deveAtualizarPaciente() {

        // Arrange
        Long id = 1L;

        PacienteRequestDTO requestDTO = new PacienteRequestDTO();

        Paciente paciente = new Paciente();
        paciente.setNome("Maria Silva");

        PacienteResponseDTO responseDTO = new PacienteResponseDTO();
        responseDTO.setNome("Maria Souza");

        when(pacienteRepository.findById(id))
                .thenReturn(Optional.of(paciente));

        when(pacienteRepository.save(paciente))
                .thenReturn(paciente);

        when(pacienteMapper.toResponseDTO(paciente))
                .thenReturn(responseDTO);

        // Act
        PacienteResponseDTO resultado =
                pacienteService.atualizar(id, requestDTO);

        // Assert
        assertEquals("Maria Souza", resultado.getNome());

        verify(pacienteRepository).findById(id);
        verify(pacienteMapper).updateEntity(paciente, requestDTO);
        verify(pacienteRepository).save(paciente);
        verify(pacienteMapper).toResponseDTO(paciente);
    }
    @Test
    void deveLancarExcecaoAoAtualizarPacienteInexistente() {

        // Arrange
        Long id = 99L;
        PacienteRequestDTO requestDTO = new PacienteRequestDTO();

        when(pacienteRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                PacienteNaoEncontradoException.class,
                () -> pacienteService.atualizar(id, requestDTO)
        );

        verify(pacienteRepository).findById(id);
    }
    @Test
    void deveDeletarPaciente() {

        // Arrange
        Long id = 1L;

        when(pacienteRepository.existsById(id))
                .thenReturn(true);

        // Act
        pacienteService.deletar(id);

        // Assert
        verify(pacienteRepository).existsById(id);
        verify(pacienteRepository).deleteById(id);
    }
    @Test
    void deveLancarExcecaoAoDeletarPacienteInexistente() {

        // Arrange
        Long id = 99L;

        when(pacienteRepository.existsById(id))
                .thenReturn(false);

        // Act + Assert
        assertThrows(
                PacienteNaoEncontradoException.class,
                () -> pacienteService.deletar(id)
        );

        verify(pacienteRepository).existsById(id);
    }
}