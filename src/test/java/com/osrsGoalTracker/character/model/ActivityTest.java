package com.osrsGoalTracker.character.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ActivityTest {

    @Test
    void testActivityBuilder() {
        // Given
        String name = "Clue Scrolls (all)";
        int rank = 50000;
        int score = 100;

        // When
        Activity activity = Activity.builder()
                .name(name)
                .rank(rank)
                .score(score)
                .build();

        // Then
        assertEquals(name, activity.getName());
        assertEquals(rank, activity.getRank());
        assertEquals(score, activity.getScore());
    }

    @Test
    void testActivityEquality() {
        // Given
        Activity activity1 = Activity.builder()
                .name("Clue Scrolls (all)")
                .rank(50000)
                .score(100)
                .build();

        Activity activity2 = Activity.builder()
                .name("Clue Scrolls (all)")
                .rank(50000)
                .score(100)
                .build();

        Activity differentActivity = Activity.builder()
                .name("Clue Scrolls (hard)")
                .rank(50000)
                .score(100)
                .build();

        // Then
        assertEquals(activity1, activity2, "Two activities with the same values should be equal");
        assertNotEquals(activity1, differentActivity, "Activities with different values should not be equal");
        assertEquals(activity1.hashCode(), activity2.hashCode(), "Equal activities should have the same hash code");
    }

    @Test
    void testActivityValidValues() {
        // Given
        String name = "Clue Scrolls (all)";
        int rank = 1;
        int score = 0;

        // When
        Activity activity = Activity.builder()
                .name(name)
                .rank(rank)
                .score(score)
                .build();

        // Then
        assertTrue(activity.getRank() >= 0, "Rank should be non-negative");
        assertTrue(activity.getScore() >= 0, "Score should be non-negative");
    }
}