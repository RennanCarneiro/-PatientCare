package com.rennancarneiro.patientcare.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pacientes")

public class Paciente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Enumerated(EnumType.STRING)  private Sexo sexo;
    @Column(unique = true, nullable = false) private String cpf;
    private LocalDate dataNascimento;
    private String convenio;
    private String telefone;

    public Paciente() {

    }
     public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Sexo getSexo() {
        return sexo;
    }


    public String getCpf() {
        return cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }
}
