package com.example;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BackendService {

    private static Random random = new Random();

    private static String[] items = {
            "Lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing", "elit,", "sed", "do", "eiusmod",
            "tempor", "incididunt", "ut", "labore", "et", "dolore", "magna", "aliqua", "Ut", "enim", "ad", "minim",
            "veniam", "quis", "nostrud", "exercitation", "ullamco", "laboris", "nisi", "ut", "aliquip", "ex", "ea"
    };

    private static String[] contacts = {
            "commodo", "consequat", "Duis", "aute", "irure", "dolor", "in", "reprehenderit", "in", "voluptate",
            "velit", "esse", "cillum", "dolore", "eu", "fugiat", "nulla", "pariatur", "excepteur", "sint", "occaecat",
            "cupidatat", "non", "proident, ", "sunt", "in", "culpa", "qui", "officia", "deserunt", "mollit", "anim"
    };

    public List<Entity> findAllEntities() {
        return IntStream.rangeClosed(0, 2000000).boxed()
                .map(this::createNewEntity)
                .collect(Collectors.toList());
    }

    private Entity createNewEntity(int id) {
        Entity entity = new Entity();
        entity.setId(id);
        entity.setItem(items[random.nextInt(items.length)]);
        entity.setContact(contacts[random.nextInt(contacts.length)]);

        return entity;
    }

}