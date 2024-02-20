package com.example.dto;

import lombok.Data;

@Data
public class RepositoryDTO {
    private Long id;
    private String name;
    private String webUrl;

    public RepositoryDTO(Long id, String name, String webUrl) {
        this.id = id;
        this.name = name;
        this.webUrl = webUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
