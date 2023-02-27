package com.desafiovotacaoapi.desafiovotacaoapi.dto.associateDto;

import jakarta.validation.constraints.NotBlank;

public record CreateAssociateDTO(

        @NotBlank(message = "The user needs a name!")
        String name

) {
}
