package com.osrsGoalTracker.character.handler.request;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request object for adding a RuneScape character to a user's account.
 */
@Data
@NoArgsConstructor
public class AddCharacterToUserRequest {
    private String characterName;
}
