package org.tinyg.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContainerLoad {

    private Long id;

    private Container container;
    private Double weight;
    private Double height;
    private Double width;

    public ContainerLoad(Long id, Double weight, Double height, Double width) {
        this.id = id;
        this.weight = weight;
        this.height = height;
        this.width = width;
    }
}
