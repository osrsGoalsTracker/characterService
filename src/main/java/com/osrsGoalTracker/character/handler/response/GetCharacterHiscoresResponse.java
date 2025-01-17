package com.osrsGoalTracker.character.handler.response;

import java.util.List;

import com.osrsGoalTracker.character.model.Activity;
import com.osrsGoalTracker.character.model.Skill;

import lombok.Builder;
import lombok.Value;

/**
 * Response object for player statistics.
 */
@Value
@Builder
public class GetCharacterHiscoresResponse {
    /**
     * The name of the character.
     */
    private final String characterName;

    /**
     * The list of skills.
     */
    private final List<Skill> skills;

    /**
     * The list of activities.
     */
    private final List<Activity> activities;
}
