package com.osrsGoalTracker.character.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;

import org.junit.jupiter.api.Test;

class CharacterTest {

        private static final String USER_ID = "testUser";
        private static final String CHARACTER_NAME = "testCharacter";

        @Test
        void builder_Success() {
                Instant now = Instant.now();

                Character character = Character.builder()
                                .name(CHARACTER_NAME)
                                .userId(USER_ID)
                                .createdAt(now)
                                .updatedAt(now)
                                .build();

                assertNotNull(character);
                assertEquals(CHARACTER_NAME, character.getName());
                assertEquals(USER_ID, character.getUserId());
                assertEquals(now, character.getCreatedAt());
                assertEquals(now, character.getUpdatedAt());
        }

        @Test
        void equals_SameValues_ReturnsTrue() {
                Instant now = Instant.now();

                Character character1 = Character.builder()
                                .name(CHARACTER_NAME)
                                .userId(USER_ID)
                                .createdAt(now)
                                .updatedAt(now)
                                .build();

                Character character2 = Character.builder()
                                .name(CHARACTER_NAME)
                                .userId(USER_ID)
                                .createdAt(now)
                                .updatedAt(now)
                                .build();

                assertEquals(character1, character2);
        }
}