# Data Models

## Overview

All models in the service follow these principles:
- Immutable using Lombok `@Value`
- Builder pattern using Lombok `@Builder`
- Request objects use `@Data` with `@NoArgsConstructor`
- Clear separation between domain models and DTOs
- Validation using Jakarta Validation annotations

## Domain Models

### Character Domain

```java
@Value
@Builder
public class Character {
    String userId;
    String name;
    LocalDateTime lastUpdated;
    LocalDateTime createdAt;
}

```

## Request/Response DTOs

### Character Endpoints

```java
@Data
@NoArgsConstructor
public class AddCharacterToUserRequest {
    @NotBlank(message = "Character name is required")
    private String characterName;
}

@Value
@Builder
public class GetCharactersForUserResponse {
    List<Character> characters;
}
```

## Database Models

### DynamoDB Entities

```java
@DynamoDBTable(tableName = "Characters")
public class CharacterEntity {
    @DynamoDBHashKey
    private String userId;
    
    @DynamoDBRangeKey
    private String characterName;
    
    @DynamoDBAttribute
    private String lastUpdated;

    @DynamoDBAttribute
    private String createdAt;
} 