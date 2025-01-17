package com.osrsGoalTracker.character.service.impl;

import java.util.List;

import com.google.inject.Inject;
import com.osrsGoalTracker.character.model.Character;
import com.osrsGoalTracker.character.repository.CharacterRepository;
import com.osrsGoalTracker.character.service.CharacterService;

import lombok.extern.log4j.Log4j2;

/**
 * Default implementation of the PlayerService interface.
 */
@Log4j2
public class CharacterServiceImpl implements CharacterService {
    private final CharacterRepository characterRepository;

    /**
     * Constructs a new CharacterServiceImpl.
     *
     * @param characterRepository The CharacterRepository instance to use for data
     *                            operations
     */
    @Inject
    public CharacterServiceImpl(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    @Override
    public List<Character> getCharactersForUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            log.error("User ID cannot be null or empty");
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        String trimmedUserId = userId.trim();
        log.info("Getting characters for user {}", trimmedUserId);
        return characterRepository.getCharactersForUser(trimmedUserId);
    }

    @Override
    public Character addCharacterToUser(String userId, String characterName) {
        return characterRepository.addCharacterToUser(userId, characterName);
    }
}
