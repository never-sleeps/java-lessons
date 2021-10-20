package ru.neo4j.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PhoneUser {
    private String id;
    private String name;

    private List<Phone> phones;
}
