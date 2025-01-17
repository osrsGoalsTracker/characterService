package com.osrsGoalTracker.character.service;

import java.util.List;

import com.osrsGoalTracker.character.model.Character;

/**
 * Service interface for character-related operations.
 */

public interface CharacterService {
    /**
     * Retrieves all characters associated with a user.
     *
     * @param userId The ID of the user
     * @return A list of characters associated with the user
     */
    List<Character> getCharactersForUser(String userId);

    /**
     * Adds a character to a user's account.
     *
     * @param userId        The ID of the user
     * @param characterName The name of the character to add
     * @return The added character
     */
    Character addCharacterToUser(String userId, String characterName);
}
