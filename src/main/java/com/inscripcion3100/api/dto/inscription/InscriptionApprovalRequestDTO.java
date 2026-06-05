package com.inscripcion3100.api.dto.inscription;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InscriptionApprovalRequestDTO {
    @NotNull(message = "Necesario el ID de la inscripcion")
    private Long inscriptionId;

    @NotNull(message = "Especifique si la inscripcion fue aprobada")
    private Boolean isApproved;

    //materias adeudadas
    @Size(max = 150)
    private String pendingSubjects;

    //documentacion faltante
    @Size(max = 500)
    private String pendingDocumentation;

    @Size(max = 500)
    private String comments;
}
