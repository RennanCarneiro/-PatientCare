package com.rennancarneiro.patientcare.repository;
import com.rennancarneiro.patientcare.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    
}
