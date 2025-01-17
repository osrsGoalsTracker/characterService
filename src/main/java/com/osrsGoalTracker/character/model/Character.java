package com.osrsGoalTracker.character.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

/**
 * Model representing a RuneScape character.
 */
@Value
@Builder
public class Character {
    /**
     * The name of the character.
     */
    private final String name;

    /**
     * The ID of the user who owns this character.
     */
    private final String userId;

    /**
     * The timestamp when the character was created.
     */
    private final LocalDateTime createdAt;

    /**
     * The timestamp when the character was last updated.
     */
    private final LocalDateTime updatedAt;
}