package com.rennanarneiro.patientcare.service.impl;

import com.rennanarneiro.patientcare.dto.PacienteRequestDTO;
import com.rennanarneiro.patientcare.dto.PacienteResponseDTO;
import com.rennanarneiro.patientcare.exception.PacienteNaoEncontradoException;
import com.rennanarneiro.patientcare.mapper.PacienteMapper;
import com.rennanarneiro.patientcare.model.Paciente;
import com.rennanarneiro.patientcare.repository.PacienteRepository;
import com.rennanarneiro.patientcare.service.PacienteService;
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