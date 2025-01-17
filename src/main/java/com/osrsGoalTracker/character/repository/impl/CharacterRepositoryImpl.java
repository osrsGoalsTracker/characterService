package com.osrsGoalTracker.character.repository.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.osrsGoalTracker.character.model.Character;
import com.osrsGoalTracker.character.repository.CharacterRepository;
import com.osrsGoalTracker.character.dao.CharacterDao;
import com.osrsGoalTracker.character.dao.entity.CharacterEntity;

import lombok.extern.log4j.Log4j2;

/**
 * Default implementation of the CharacterRepository interface.
 */
@Log4j2
public class CharacterRepositoryImpl implements CharacterRepository {
    private final CharacterDao characterDao;

    /**
     * Constructs a new DefaultCharacterRepository.
     *
     * @param characterDao The CharacterDao instance to use for data operations
     */
    @Inject
    public CharacterRepositoryImpl(CharacterDao characterDao) {
        this.characterDao = characterDao;
    }

    @Override
    public Character addCharacterToUser(String userId, String characterName) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        if (characterName == null || characterName.trim().isEmpty()) {
            throw new IllegalArgumentException("Character name cannot be null or empty");
        }

        String trimmedUserId = userId.trim();
        String trimmedCharacterName = characterName.trim();

        log.info("Adding character {} to user {}", trimmedCharacterName, trimmedUserId);

        CharacterEntity character = characterDao.addCharacterToUser(trimmedUserId, trimmedCharacterName);
        return convertToCharacter(character);
    }

    @Override
    public List<Character> getCharactersForUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            log.error("User ID cannot be null or empty");
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        String trimmedUserId = userId.trim();
        log.info("Getting players for user {} from DAO", trimmedUserId);
        List<CharacterEntity> characters = characterDao.getCharactersForUser(trimmedUserId);
        return convertToCharacters(characters);
    }

    private Character convertToCharacter(CharacterEntity character) {
        return Character.builder()
                .name(character.getName())
                .userId(character.getUserId())
                .build();
    }

    private List<Character> convertToCharacters(List<CharacterEntity> characters) {
        return characters.stream()
                .map(this::convertToCharacter)
                .collect(Collectors.toList());
    }
}
