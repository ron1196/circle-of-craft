package io.github.ron1196.thelionking.quest;

/**
 * Marker interface for quest stage enums. Each questline defines its own enum
 * implementing this interface, making stage IDs resilient to reordering.
 */
public interface LKStage {
    String name(); // enums already provide this
}
