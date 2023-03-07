package com.desafiovotacaoapi.desafiovotacaoapi.mapper;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.associateDto.CreateAssociateDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Associate;

public interface AssociateMapper {

    static Associate buildAssociate(CreateAssociateDTO associateDTO) {
        return Associate.builder()
                .name(associateDTO.name())
                .build();
    }

}
