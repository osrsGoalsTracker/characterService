package com.osrsGoalTracker.character.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.osrsGoalTracker.character.model.Character;
import com.osrsGoalTracker.character.repository.CharacterRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CharacterServiceImplTest {

    @Mock
    private CharacterRepository characterRepository;

    private CharacterServiceImpl characterService;

    @BeforeEach
    void setUp() {
        characterService = new CharacterServiceImpl(characterRepository);
    }

    @Test
    void getCharactersForUser_ValidUserId_ReturnsCharacters() {
        // Given
        String userId = "testUser123";
        LocalDateTime now = LocalDateTime.now();
        List<Character> expectedCharacters = Arrays.asList(
                Character.builder()
                        .name("TestChar1")
                        .userId(userId)
                        .createdAt(now)
                        .updatedAt(now)
                        .build(),
                Character.builder()
                        .name("TestChar2")
                        .userId(userId)
                        .createdAt(now)
                        .updatedAt(now)
                        .build());

        when(characterRepository.getCharactersForUser(userId)).thenReturn(expectedCharacters);

        // When
        List<Character> actualCharacters = characterService.getCharactersForUser(userId);

        // Then
        assertEquals(expectedCharacters, actualCharacters);
        verify(characterRepository).getCharactersForUser(userId);
    }

    @Test
    void getCharactersForUser_NullUserId_ThrowsIllegalArgumentException() {
        // When/Then
        assertThrows(IllegalArgumentException.class,
                () -> characterService.getCharactersForUser(null));
    }

    @Test
    void getCharactersForUser_EmptyUserId_ThrowsIllegalArgumentException() {
        // When/Then
        assertThrows(IllegalArgumentException.class,
                () -> characterService.getCharactersForUser(""));
    }

    @Test
    void getCharactersForUser_WhitespaceUserId_ThrowsIllegalArgumentException() {
        // When/Then
        assertThrows(IllegalArgumentException.class,
                () -> characterService.getCharactersForUser("   "));
    }

    @Test
    void addCharacterToUser_ValidInputs_ReturnsCharacter() {
        // Given
        String userId = "testUser123";
        String characterName = "TestChar";
        LocalDateTime now = LocalDateTime.now();
        Character expectedCharacter = Character.builder()
                .name(characterName)
                .userId(userId)
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(characterRepository.addCharacterToUser(userId, characterName)).thenReturn(expectedCharacter);

        // When
        Character actualCharacter = characterService.addCharacterToUser(userId, characterName);

        // Then
        assertEquals(expectedCharacter, actualCharacter);
        verify(characterRepository).addCharacterToUser(userId, characterName);
    }
}