package com.osrsGoalTracker.character.repository;

import java.util.List;

import com.osrsGoalTracker.character.model.Character;

/**
 * Service interface for managing player data operations.
 */
public interface CharacterRepository {
    /**
     * Retrieves all players associated with a user.
     *
     * @param userId The ID of the user
     * @return A list of players associated with the user
     * @throws IllegalArgumentException if userId is null or empty
     */
    List<Character> getCharactersForUser(String userId);

    /**
     * Adds a character to a user.
     *
     * @param userId          The ID of the user
     * @param characterName The name of the character
     * @return The created character entity
     */
    Character addCharacterToUser(String userId, String characterName);
}
