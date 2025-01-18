package com.osrsGoalTracker.character.repository.entity;

/**
 * Utility class for generating sort keys for DynamoDB items.
 */
public final class SortKeyUtil {
    private static final String METADATA = "METADATA";
    private static final String CHARACTER = "CHARACTER";

    public static final String CHARACTER_METADATA_PREFIX = CHARACTER + "#" + METADATA + "#";

    /**
     * Default constructor to prevent instantiation.
     */
    private SortKeyUtil() {
        // Prevent instantiation
    }

    /**
     * Gets the sort key for character metadata.
     *
     * @param characterName The name of the character
     * @return The sort key for character metadata
     */
    public static String getCharacterMetadataSortKey(String characterName) {
        return String.format("%s#%s#%s", CHARACTER, METADATA, characterName);
    }
}
