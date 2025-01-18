
# DynamoDB Schema for Goal Tracking and Notification System

## Overview

This schema is designed to support a goal tracking and notification platform for RuneScape characters. The system allows users to:
- Create and manage RuneScape-related goals.
- Track progress towards goals over time.
- Configure notification channels for goal updates.
- Support efficient queries for metadata, progress tracking, and notifications.

### Key Features
1. **Flexible Data Organization**: Supports storing metadata, goals, progress, and notifications using structured keys.
2. **Efficient Access Patterns**: Enables querying for user-specific data, goals, and progress.
3. **Scalable Design**: Accommodates daily progress updates for multiple goals while adhering to DynamoDB’s scalability limits.
4. **Low Maintenance**: Designed with minimal data duplication and optional TTL for data retention.

---

## DynamoDB Schema

### Primary Table: Goals Table
- **Partition Key (PK):** `USER#<user_id>`
- **Sort Key (SK):** Encodes various entity types and their metadata/data using structured prefixes.

---

### Sort Key Structure and Examples

#### 1. **Character Metadata**
   - **Sort Key:** `CHARACTER#METADATA#<character_name>`
   - **Purpose:** This is the metadata for the RuneScape character. It is used to store information about the character such as their name, createdAt, and updatedAt.
   - **Example Item:**
     ```json
     {
       "PK": "USER#12345",
       "SK": "CHARACTER#METADATA#Character123",
       "name": "Character123",
       "createdAt": "2025-01-01T00:00:00Z",
       "updatedAt": "2025-01-01T00:00:00Z"
     }
     ```

---

### Indexes

#### Primary Index
- **PK:** `USER#<user_id>`
- **SK:** Encodes metadata, notification channels, goals, and progress.

---
