package com.osrsGoalTracker.character.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class CharacterTest {

    @Test
    void testCharacterBuilder() {
        // Given
        String name = "TestCharacter";
        String userId = "user123";
        LocalDateTime now = LocalDateTime.now();

        // When
        Character character = Character.builder()
                .name(name)
                .userId(userId)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Then
        assertEquals(name, character.getName());
        assertEquals(userId, character.getUserId());
        assertEquals(now, character.getCreatedAt());
        assertEquals(now, character.getUpdatedAt());
    }

    @Test
    void testCharacterEquality() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Character character1 = Character.builder()
                .name("TestCharacter")
                .userId("user123")
                .createdAt(now)
                .updatedAt(now)
                .build();

        Character character2 = Character.builder()
                .name("TestCharacter")
                .userId("user123")
                .createdAt(now)
                .updatedAt(now)
                .build();

        Character differentCharacter = Character.builder()
                .name("DifferentCharacter")
                .userId("user123")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Then
        assertEquals(character1, character2, "Two characters with the same values should be equal");
        assertNotEquals(character1, differentCharacter, "Characters with different values should not be equal");
        assertEquals(character1.hashCode(), character2.hashCode(), "Equal characters should have the same hash code");
    }
}