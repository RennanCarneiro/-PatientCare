package com.rennancarneiro.patientcare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rennancarneiro.patientcare.dto.PacienteRequestDTO;
import com.rennancarneiro.patientcare.dto.PacienteResponseDTO;
import com.rennancarneiro.patientcare.exception.GlobalExceptionHandler;
import com.rennancarneiro.patientcare.exception.PacienteNaoEncontradoException;
import com.rennancarneiro.patientcare.model.Sexo;
import com.rennancarneiro.patientcare.service.PacienteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PacienteControllerTest {

    private MockMvc mockMvc;

    private PacienteService pacienteService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        pacienteService = mock(PacienteService.class);

        PacienteController pacienteController =
                new PacienteController(pacienteService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(pacienteController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void deveCriarPaciente() throws Exception {

        // Arrange
        PacienteRequestDTO requestDTO = criarRequestDTO();

        PacienteResponseDTO responseDTO = criarResponseDTO();

        when(pacienteService.criar(any(PacienteRequestDTO.class)))
                .thenReturn(responseDTO);

        // Act + Assert
        mockMvc.perform(
                        post("/pacientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Maria Silva"))
                .andExpect(jsonPath("$.sexo").value("FEMININO"))
                .andExpect(jsonPath("$.cpf").value("12345678900"))
                .andExpect(jsonPath("$.convenio").value("Unimed"))
                .andExpect(jsonPath("$.telefone").value("81999999999"));

        verify(pacienteService)
                .criar(any(PacienteRequestDTO.class));
    }

    @Test
    void deveListarPacientes() throws Exception {

        // Arrange
        PacienteResponseDTO paciente1 = criarResponseDTO();

        PacienteResponseDTO paciente2 = new PacienteResponseDTO();
        paciente2.setId(2L);
        paciente2.setNome("João Silva");
        paciente2.setSexo(Sexo.MASCULINO);
        paciente2.setCpf("98765432100");
        paciente2.setDataNascimento(LocalDate.of(1990, 5, 10));
        paciente2.setConvenio("Hapvida");
        paciente2.setTelefone("81988888888");

        when(pacienteService.listar())
                .thenReturn(List.of(paciente1, paciente2));

        // Act + Assert
        mockMvc.perform(get("/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Maria Silva"))
                .andExpect(jsonPath("$[1].nome").value("João Silva"));

        verify(pacienteService).listar();
    }

    @Test
    void deveBuscarPacientePorId() throws Exception {

        // Arrange
        Long id = 1L;

        when(pacienteService.buscarPorId(id))
                .thenReturn(criarResponseDTO());

        // Act + Assert
        mockMvc.perform(get("/pacientes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Maria Silva"))
                .andExpect(jsonPath("$.cpf").value("12345678900"));

        verify(pacienteService).buscarPorId(id);
    }

    @Test
    void deveRetornar404AoBuscarPacienteInexistente() throws Exception {

        // Arrange
        Long id = 99L;

        when(pacienteService.buscarPorId(id))
                .thenThrow(new PacienteNaoEncontradoException(id));

        // Act + Assert
        mockMvc.perform(get("/pacientes/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        "Paciente não encontrado com o id: 99"
                ));

        verify(pacienteService).buscarPorId(id);
    }

    @Test
    void deveAtualizarPaciente() throws Exception {

        // Arrange
        Long id = 1L;

        PacienteRequestDTO requestDTO = criarRequestDTO();

        PacienteResponseDTO responseDTO = criarResponseDTO();
        responseDTO.setNome("Maria Souza");
        responseDTO.setConvenio("Hapvida");

        when(pacienteService.atualizar(
                eq(id),
                any(PacienteRequestDTO.class)
        )).thenReturn(responseDTO);

        // Act + Assert
        mockMvc.perform(
                        put("/pacientes/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Maria Souza"))
                .andExpect(jsonPath("$.convenio").value("Hapvida"));

        verify(pacienteService)
                .atualizar(eq(id), any(PacienteRequestDTO.class));
    }

    @Test
    void deveRetornar404AoAtualizarPacienteInexistente() throws Exception {

        // Arrange
        Long id = 99L;

        PacienteRequestDTO requestDTO = criarRequestDTO();

        when(pacienteService.atualizar(
                eq(id),
                any(PacienteRequestDTO.class)
        )).thenThrow(new PacienteNaoEncontradoException(id));

        // Act + Assert
        mockMvc.perform(
                        put("/pacientes/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        "Paciente não encontrado com o id: 99"
                ));

        verify(pacienteService)
                .atualizar(eq(id), any(PacienteRequestDTO.class));
    }

    @Test
    void deveDeletarPaciente() throws Exception {

        // Arrange
        Long id = 1L;

        // Act + Assert
        mockMvc.perform(delete("/pacientes/{id}", id))
                .andExpect(status().isNoContent());

        verify(pacienteService).deletar(id);
    }

    @Test
    void deveRetornar404AoDeletarPacienteInexistente() throws Exception {

        // Arrange
        Long id = 99L;

        org.mockito.Mockito.doThrow(
                new PacienteNaoEncontradoException(id)
        ).when(pacienteService).deletar(id);

        // Act + Assert
        mockMvc.perform(delete("/pacientes/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        "Paciente não encontrado com o id: 99"
                ));

        verify(pacienteService).deletar(id);
    }

    @Test
    void deveRetornar400QuandoDadosForemInvalidos() throws Exception {

        // Arrange
        PacienteRequestDTO requestDTO = new PacienteRequestDTO();

        requestDTO.setNome("");
        requestDTO.setCpf("");
        requestDTO.setTelefone("");

        // Act + Assert
        mockMvc.perform(
                        post("/pacientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nome")
                        .value("O nome é obrigatório"))
                .andExpect(jsonPath("$.sexo")
                        .value("O sexo é obrigatório"))
                .andExpect(jsonPath("$.dataNascimento")
                        .value("A data de nascimento é obrigatória"))
                .andExpect(jsonPath("$.telefone")
                        .value("O telefone é obrigatório"));
    }

    private PacienteRequestDTO criarRequestDTO() {

        PacienteRequestDTO dto = new PacienteRequestDTO();

        dto.setNome("Maria Silva");
        dto.setSexo(Sexo.FEMININO);
        dto.setCpf("12345678900");
        dto.setDataNascimento(LocalDate.of(1995, 8, 20));
        dto.setConvenio("Unimed");
        dto.setTelefone("81999999999");

        return dto;
    }

    private PacienteResponseDTO criarResponseDTO() {

        PacienteResponseDTO dto = new PacienteResponseDTO();

        dto.setId(1L);
        dto.setNome("Maria Silva");
        dto.setSexo(Sexo.FEMININO);
        dto.setCpf("12345678900");
        dto.setDataNascimento(LocalDate.of(1995, 8, 20));
        dto.setConvenio("Unimed");
        dto.setTelefone("81999999999");

        return dto;
    }
}