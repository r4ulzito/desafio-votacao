package com.desafiovotacaoapi.desafiovotacaoapi.validation;

import com.desafiovotacaoapi.desafiovotacaoapi.exception.AssociateInvalidVoteException;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Associate;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Topic;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Vote;
import com.desafiovotacaoapi.desafiovotacaoapi.model.enums.Answer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidateVoteAssociateTest {

    @Test
    @DisplayName("Deve lançar uma exceção caso o associado ja tenha um voto na lista de votos referente ao topico")
    public void validateAssociateCanVoteTest() {

        Associate fakeAssociate = new Associate(1L, "Associate1");
        Topic fakeTopic = new Topic(1L, "Title1", "Description1");

        List<Vote> voteList = new ArrayList<>();
        voteList.add(new Vote(1L, fakeAssociate, fakeTopic, Answer.YES));

        String errorMessage = "";

        try {
            ValidateVoteAssociate.validateAssociateCanVote(voteList, fakeAssociate);

        } catch (AssociateInvalidVoteException ex) {
            errorMessage = ex.getMessage();
        }

        assertEquals(errorMessage, "Associate already voted for this topic!");

    }

    @Test
    @DisplayName("Nao deve fazer nada caso o associado nao tenha um voto na lista de votos referente ao topico")
    public void validateAssociateCanVoteWithValidAssociateTest() {

        Associate fakeAssociate = new Associate(1L, "Associate1");
        Topic fakeTopic = new Topic(1L, "Title1", "Description1");

        List<Vote> voteList = new ArrayList<>();
        voteList.add(new Vote(1L, new Associate(), fakeTopic, Answer.YES));

        String errorMessage = "";

        try {
            ValidateVoteAssociate.validateAssociateCanVote(voteList, fakeAssociate);
        } catch (AssociateInvalidVoteException ex) {
            errorMessage = ex.getMessage();
        }

        assertEquals(errorMessage, "");

    }

}