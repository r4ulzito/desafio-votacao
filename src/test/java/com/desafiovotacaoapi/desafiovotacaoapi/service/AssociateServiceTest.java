package com.desafiovotacaoapi.desafiovotacaoapi.service;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.associateDto.CreateAssociateDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.associateDto.GetAssociateDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.exception.NullQueryResultExcepetion;
import com.desafiovotacaoapi.desafiovotacaoapi.mapper.AssociateMapper;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Associate;
import com.desafiovotacaoapi.desafiovotacaoapi.repository.AssociateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AssociateServiceTest {

    private AssociateService service;

    @Captor
    private ArgumentCaptor<Associate> captor;

    @Mock
    private AssociateRepository associateRepositoryMock;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        this.service = new AssociateService(associateRepositoryMock);
    }

    private List<Associate> associatesList() {
        List<Associate> list = new ArrayList<>();

        list.add(new Associate(1L, "Associate 1"));
        list.add(new Associate(2L, "Associate 2"));
        list.add(new Associate(3L, "Associate 3"));

        return list;

    }

    @Test
    @DisplayName("Deve criar um associado")
    public void createAssociateTest() {

        CreateAssociateDTO createAssociateDataMock = new CreateAssociateDTO("Name Teste");

        Mockito.when(associateRepositoryMock.save(Mockito.any(Associate.class)))
                .thenReturn(AssociateMapper.buildAssociate(createAssociateDataMock));

        Associate newAssociate = this.service.createAssociate(createAssociateDataMock);

        Mockito.verify(associateRepositoryMock).save(this.captor.capture());

        Associate associateCaptor = captor.getValue();

        assertEquals(newAssociate.getName(), associateCaptor.getName());
        Mockito.verify(associateRepositoryMock, Mockito.times(1)).save(associateCaptor);
    }

    @Test
    @DisplayName("Deve retornar o Associado com o ID passado por parâmetro")
    public void getAssociateByIdTest() {

        List<Associate> associateList = associatesList();

        Mockito.when(associateRepositoryMock.findById(1L)).thenReturn(Optional.ofNullable(associateList.get(0)));

        Associate findAssociate = this.service.getAssociateByID(1L);

        assertEquals(findAssociate.getId(), associateList.get(0).getId());
        assertEquals(findAssociate.getName(), associateList.get(0).getName());
        Mockito.verify(associateRepositoryMock, Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar uma Exceção caso nao encontre nenhum associado com o ID indicado")
    public void getAssociateByIdPassingInexistentAssociateIdTest() {

        Mockito.when(associateRepositoryMock.findById(4L)).thenReturn(Optional.empty());

        try {
            Associate findAssociate = this.service.getAssociateByID(4L);

            Mockito.verifyNoInteractions(associateRepositoryMock);
        } catch (NullQueryResultExcepetion ex) {
            assertEquals(ex.getMessage(), "Associate not found!");
        }

    }

    @Test
    @DisplayName("Deve retornar uma lista com todos os associados")
    public void getAllAssociatesTest() {
        List<Associate> associateList = associatesList();

        Mockito.when(associateRepositoryMock.findAll()).thenReturn(associateList);

        List<GetAssociateDTO> associatesList = this.service.getAllAssociates();

        assertEquals(associatesList.size(), 3);
        assertEquals(associatesList.get(1).id(), 2);
        assertEquals(associatesList.get(1).name(), "Associate 2");
        Mockito.verify(associateRepositoryMock, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia caso nao exista nenhum associado salvo")
    public void getAllAssociatesWithNoAssociatesTest() {
        List<Associate> associateList = new ArrayList<>();

        Mockito.when(associateRepositoryMock.findAll()).thenReturn(associateList);

        List<GetAssociateDTO> associatesList = this.service.getAllAssociates();

        assertEquals(associatesList.size(), 0);
        Mockito.verify(associateRepositoryMock, Mockito.times(1)).findAll();
    }

}


