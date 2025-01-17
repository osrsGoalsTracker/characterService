package com.osrsGoalTracker.character.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SkillTest {

    @Test
    void testSkillBuilder() {
        // Given
        String name = "Attack";
        int rank = 100000;
        int level = 99;
        long xp = 13034431;

        // When
        Skill skill = Skill.builder()
                .name(name)
                .rank(rank)
                .level(level)
                .xp(xp)
                .build();

        // Then
        assertEquals(name, skill.getName());
        assertEquals(rank, skill.getRank());
        assertEquals(level, skill.getLevel());
        assertEquals(xp, skill.getXp());
    }

    @Test
    void testSkillEquality() {
        // Given
        Skill skill1 = Skill.builder()
                .name("Attack")
                .rank(100000)
                .level(99)
                .xp(13034431)
                .build();

        Skill skill2 = Skill.builder()
                .name("Attack")
                .rank(100000)
                .level(99)
                .xp(13034431)
                .build();

        Skill differentSkill = Skill.builder()
                .name("Defence")
                .rank(100000)
                .level(99)
                .xp(13034431)
                .build();

        // Then
        assertEquals(skill1, skill2, "Two skills with the same values should be equal");
        assertNotEquals(skill1, differentSkill, "Skills with different values should not be equal");
        assertEquals(skill1.hashCode(), skill2.hashCode(), "Equal skills should have the same hash code");
    }

    @Test
    void testSkillValidValues() {
        // Given
        String name = "Attack";
        int rank = 1;
        int level = 1;
        long xp = 0;

        // When
        Skill skill = Skill.builder()
                .name(name)
                .rank(rank)
                .level(level)
                .xp(xp)
                .build();

        // Then
        assertTrue(skill.getRank() >= 0, "Rank should be non-negative");
        assertTrue(skill.getLevel() >= 1, "Level should be at least 1");
        assertTrue(skill.getXp() >= 0, "XP should be non-negative");
    }
}