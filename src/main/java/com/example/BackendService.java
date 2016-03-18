package com.example;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BackendService {

    private static final int ENTITIES_COUNT = 3000000;

    private final static String[] items = {
            "Lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing", "elit,", "sed", "do", "eiusmod",
            "tempor", "incididunt", "ut", "labore", "et", "dolore", "magna", "aliqua", "Ut", "enim", "ad", "minim",
            "veniam", "quis", "nostrud", "exercitation", "ullamco", "laboris", "nisi", "ut", "aliquip", "ex", "ea"
    };

    private final static String[] contacts = {
            "commodo", "consequat", "Duis", "aute", "irure", "dolor", "in", "reprehenderit", "in", "voluptate",
            "velit", "esse", "cillum", "dolore", "eu", "fugiat", "nulla", "pariatur", "excepteur", "sint", "occaecat",
            "cupidatat", "non", "proident, ", "sunt", "in", "culpa", "qui", "officia", "deserunt", "mollit", "anim"
    };

    public Entity getById(int id) {
        Entity entity = new Entity();
        entity.setId(id);
        entity.setItem(items[id % items.length]);
        entity.setContact(contacts[id % contacts.length]);

        return entity;
    }

    public List<Entity> findAllEntities() {
        try {
            Thread.sleep(5000); // simulate slow process

            return IntStream.rangeClosed(0, ENTITIES_COUNT).boxed()
                    .map(this::getById)
                    .collect(Collectors.toList());

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}