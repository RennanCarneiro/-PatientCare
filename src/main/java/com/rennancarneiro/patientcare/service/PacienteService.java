import com.rennanarneiro.patientcare.dto.PacienteRequestDTO;
import com.rennanarneiro.patientcare.dto.PacienteResponseDTO;

public interface PacientService {
    PacienteResponseDTO criar(PacienteRequestDTO dto);

    List<PacienteResponseDTO> listar();

    PacienteResponseDTO buscarPorId(Long id);

    PacienteResponseDTO atualizar(Long id, PacienteRequestDTO dto);

    void deletar(Long id);
}
