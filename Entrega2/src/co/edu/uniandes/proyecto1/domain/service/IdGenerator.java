package co.edu.uniandes.proyecto1.domain.service;

import java.util.UUID;

public final class IdGenerator {
    private IdGenerator() { }
    public static String newId() {
        return UUID.randomUUID().toString();
    }
}


