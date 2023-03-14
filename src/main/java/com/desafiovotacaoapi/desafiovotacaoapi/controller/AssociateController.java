package com.desafiovotacaoapi.desafiovotacaoapi.controller;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.associateDto.CreateAssociateDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.associateDto.GetAssociateDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Associate;
import com.desafiovotacaoapi.desafiovotacaoapi.service.AssociateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/associates")
@Tag(name = "Associate")
public class AssociateController {

    private final AssociateService associateService;

    @Autowired
    public AssociateController(AssociateService service) {
        this.associateService = service;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = {@Content()}),
    })
    @Operation(summary = "Cria um associado")
    @PostMapping
    public ResponseEntity<Associate> createAssociate(@Valid @RequestBody CreateAssociateDTO data) {

        return ResponseEntity.status(HttpStatus.CREATED).body(this.associateService.createAssociate(data));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
    })
    @Operation(summary = "Busca todos os associados")
    @GetMapping
    public ResponseEntity<List<GetAssociateDTO>> getAllAsssociates() {
        return ResponseEntity.status(HttpStatus.OK).body(this.associateService.getAllAssociates());
    }

}
