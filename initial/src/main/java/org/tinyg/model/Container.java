package org.tinyg.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Container {

    private Long id;

    private Transport transport;
    private List<ContainerLoad> containerLoad = new ArrayList<>();
    private ContainerType type;
    private String serialNumber;

    public Container(Long id, ContainerType type, String serialNumber) {
        this.id = id;
        this.type = type;
        this.serialNumber = serialNumber;
    }

    public void addContainerLoad(final ContainerLoad containerLoad) {
        if (ContainerType.PARTIAL_LOAD.equals(this.getType()) || this.containerLoad.isEmpty()) {
            this.containerLoad.add(containerLoad);
        }
    }
}
