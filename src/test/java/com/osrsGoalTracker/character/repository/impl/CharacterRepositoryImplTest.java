package com.osrsGoalTracker.character.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import com.osrsGoalTracker.character.model.Character;
import com.osrsGoalTracker.character.dao.CharacterDao;
import com.osrsGoalTracker.character.dao.entity.CharacterEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CharacterRepositoryImplTest {

    @Mock
    private CharacterDao characterDao;

    private CharacterRepositoryImpl characterRepository;

    @BeforeEach
    void setUp() {
        characterRepository = new CharacterRepositoryImpl(characterDao);
    }

    @Test
    void addCharacterToUser_ValidInputs_ReturnsCharacter() {
        // Given
        String userId = "testUser123";
        String characterName = "TestChar";
        Instant now = Instant.now();
        CharacterEntity characterEntity = CharacterEntity.builder()
                .name(characterName)
                .userId(userId)
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(characterDao.addCharacterToUser(userId, characterName)).thenReturn(characterEntity);

        // When
        Character result = characterRepository.addCharacterToUser(userId, characterName);

        // Then
        assertEquals(characterName, result.getName());
        assertEquals(userId, result.getUserId());
        verify(characterDao).addCharacterToUser(userId, characterName);
    }

    @Test
    void addCharacterToUser_NullUserId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> characterRepository.addCharacterToUser(null, "TestChar"));
    }

    @Test
    void addCharacterToUser_EmptyUserId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> characterRepository.addCharacterToUser("", "TestChar"));
    }

    @Test
    void addCharacterToUser_NullCharacterName_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> characterRepository.addCharacterToUser("testUser123", null));
    }

    @Test
    void addCharacterToUser_EmptyCharacterName_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> characterRepository.addCharacterToUser("testUser123", ""));
    }

    @Test
    void getCharactersForUser_ValidUserId_ReturnsCharacters() {
        // Given
        String userId = "testUser123";
        Instant now = Instant.now();
        CharacterEntity character1 = CharacterEntity.builder()
                .name("TestChar1")
                .userId(userId)
                .createdAt(now)
                .updatedAt(now)
                .build();

        CharacterEntity character2 = CharacterEntity.builder()
                .name("TestChar2")
                .userId(userId)
                .createdAt(now)
                .updatedAt(now)
                .build();

        List<CharacterEntity> characterEntities = Arrays.asList(character1, character2);
        when(characterDao.getCharactersForUser(userId)).thenReturn(characterEntities);

        // When
        List<Character> result = characterRepository.getCharactersForUser(userId);

        // Then
        assertEquals(2, result.size());
        assertEquals("TestChar1", result.get(0).getName());
        assertEquals(userId, result.get(0).getUserId());
        assertEquals("TestChar2", result.get(1).getName());
        assertEquals(userId, result.get(1).getUserId());
        verify(characterDao).getCharactersForUser(userId);
    }

    @Test
    void getCharactersForUser_NullUserId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> characterRepository.getCharactersForUser(null));
    }

    @Test
    void getCharactersForUser_EmptyUserId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> characterRepository.getCharactersForUser(""));
    }

    @Test
    void getCharactersForUser_WhitespaceUserId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> characterRepository.getCharactersForUser("   "));
    }
}