package com.desafiovotacaoapi.desafiovotacaoapi.service;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.associateDto.CreateAssociateDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.associateDto.GetAssociateDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Associate;

import java.util.List;

public interface AssociateService {

    Associate createAssociate(CreateAssociateDTO associateDTO);

    Associate getAssociateByID(Long associateId);

    List<GetAssociateDTO> getAllAssociates();

}
