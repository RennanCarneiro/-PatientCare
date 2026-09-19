package com.rennancarneiro.patientcare.service.impl;

import com.rennancarneiro.patientcare.dto.PacienteRequestDTO;
import com.rennancarneiro.patientcare.dto.PacienteResponseDTO;
import com.rennancarneiro.patientcare.exception.PacienteNaoEncontradoException;
import com.rennancarneiro.patientcare.mapper.PacienteMapper;
import com.rennancarneiro.patientcare.model.Paciente;
import com.rennancarneiro.patientcare.repository.PacienteRepository;
import com.rennancarneiro.patientcare.service.PacienteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;

    public PacienteServiceImpl(
            PacienteRepository pacienteRepository,
            PacienteMapper pacienteMapper) {

        this.pacienteRepository = pacienteRepository;
        this.pacienteMapper = pacienteMapper;
    }

    @Override
    public PacienteResponseDTO criar(PacienteRequestDTO dto) {

        Paciente paciente = pacienteMapper.toEntity(dto);

        Paciente pacienteSalvo = pacienteRepository.save(paciente);

        return pacienteMapper.toResponseDTO(pacienteSalvo);
    }

    @Override
    public List<PacienteResponseDTO> listar() {

        return pacienteRepository.findAll()
                .stream()
                .map(pacienteMapper::toResponseDTO)
                .toList();
    }

    @Override
    public PacienteResponseDTO buscarPorId(Long id) {

        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new PacienteNaoEncontradoException(id));

        return pacienteMapper.toResponseDTO(paciente);
    }

    @Override
    public PacienteResponseDTO atualizar(Long id, PacienteRequestDTO dto) {

        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new PacienteNaoEncontradoException(id));

        pacienteMapper.updateEntity(paciente, dto);

        Paciente pacienteAtualizado = pacienteRepository.save(paciente);

        return pacienteMapper.toResponseDTO(pacienteAtualizado);
    }

    @Override
    public void deletar(Long id) {

        if (!pacienteRepository.existsById(id)) {
            throw new PacienteNaoEncontradoException(id);
        }

        pacienteRepository.deleteById(id);
    }
}