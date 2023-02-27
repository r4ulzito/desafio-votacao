package com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto;

import jakarta.validation.constraints.NotBlank;

public record CreateTopicDTO(

        @NotBlank(message = "The topic need a title!")
        String title,

        @NotBlank(message = "The topic need a description!")
        String description
) {
}
