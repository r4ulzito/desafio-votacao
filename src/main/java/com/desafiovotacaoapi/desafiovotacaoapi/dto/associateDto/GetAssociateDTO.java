package com.desafiovotacaoapi.desafiovotacaoapi.dto.associateDto;

import com.desafiovotacaoapi.desafiovotacaoapi.model.Associate;

public record GetAssociateDTO(
        Long id,
        String name
) {

    public GetAssociateDTO(Associate associate) {
        this(associate.getId(), associate.getName());
    }
}
