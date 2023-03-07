package com.desafiovotacaoapi.desafiovotacaoapi.validation;

import com.desafiovotacaoapi.desafiovotacaoapi.exception.AssociateInvalidVoteException;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Associate;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Vote;

import java.util.List;
import java.util.Objects;

public class ValidateVoteAssociate {

    public static void validateAssociateCanVote(List<Vote> topicVotes, Associate targetAssociate) {

        topicVotes.forEach(vote -> {
            if (Objects.equals(vote.getAssociate().getId(), targetAssociate.getId())) {
                throw new AssociateInvalidVoteException("Associate already voted for this topic!");
            }
        });

    }

}
