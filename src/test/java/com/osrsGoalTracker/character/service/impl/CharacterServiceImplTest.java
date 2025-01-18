package com.osrsGoalTracker.character.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import com.osrsGoalTracker.character.model.Character;
import com.osrsGoalTracker.character.repository.CharacterRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CharacterServiceImplTest {

    @Mock
    private CharacterRepository characterRepository;

    @InjectMocks
    private CharacterServiceImpl characterService;

    private static final String USER_ID = "testUser";
    private static final String CHARACTER_NAME = "testCharacter";

    @Test
    void addCharacterToUser_Success() {
        Instant now = Instant.now();
        Character expectedCharacter = Character.builder()
                .userId(USER_ID)
                .name(CHARACTER_NAME)
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(characterRepository.addCharacterToUser(USER_ID, CHARACTER_NAME))
                .thenReturn(expectedCharacter);

        Character result = characterService.addCharacterToUser(USER_ID, CHARACTER_NAME);

        assertEquals(expectedCharacter, result);
        verify(characterRepository).addCharacterToUser(USER_ID, CHARACTER_NAME);
    }

    @Test
    void addCharacterToUser_NullUserId() {
        assertThrows(IllegalArgumentException.class,
                () -> characterService.addCharacterToUser(null, CHARACTER_NAME));
    }

    @Test
    void addCharacterToUser_EmptyUserId() {
        assertThrows(IllegalArgumentException.class,
                () -> characterService.addCharacterToUser("", CHARACTER_NAME));
    }

    @Test
    void addCharacterToUser_NullCharacterName() {
        assertThrows(IllegalArgumentException.class,
                () -> characterService.addCharacterToUser(USER_ID, null));
    }

    @Test
    void addCharacterToUser_EmptyCharacterName() {
        assertThrows(IllegalArgumentException.class,
                () -> characterService.addCharacterToUser(USER_ID, ""));
    }

    @Test
    void getCharactersForUser_Success() {
        Instant now = Instant.now();
        List<Character> expectedCharacters = Arrays.asList(
                Character.builder()
                        .userId(USER_ID)
                        .name(CHARACTER_NAME)
                        .createdAt(now)
                        .updatedAt(now)
                        .build());

        when(characterRepository.getCharactersForUser(USER_ID))
                .thenReturn(expectedCharacters);

        List<Character> result = characterService.getCharactersForUser(USER_ID);

        assertEquals(expectedCharacters, result);
        verify(characterRepository).getCharactersForUser(USER_ID);
    }

    @Test
    void getCharactersForUser_NullUserId() {
        assertThrows(IllegalArgumentException.class,
                () -> characterService.getCharactersForUser(null));
    }

    @Test
    void getCharactersForUser_EmptyUserId() {
        assertThrows(IllegalArgumentException.class,
                () -> characterService.getCharactersForUser(""));
    }
}