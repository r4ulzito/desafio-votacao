package com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto;

import jakarta.validation.constraints.NotBlank;

public record CreateTopicDTO(

        @NotBlank(message = "The topic needs a title!")
        String title,

        @NotBlank(message = "The topic needs a description!")
        String description
) {
}
