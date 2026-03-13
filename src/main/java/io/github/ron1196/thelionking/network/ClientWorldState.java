package io.github.ron1196.thelionking.network;

/**
 * Client-side cache of world state received from the server via LoginSyncPacket.
 * Used by client code (GUIs, renderers) that needs to know world state without
 * querying the server.
 */
public class ClientWorldState {
    public static int homePortalX;
    public static int homePortalY;
    public static int homePortalZ;
    public static boolean defeatedScar;
    public static int ziraStage;
    public static int pumbaaStage;
    public static boolean outlandersHostile;

    public static void reset() {
        homePortalX = 0;
        homePortalY = 0;
        homePortalZ = 0;
        defeatedScar = false;
        ziraStage = 0;
        pumbaaStage = 0;
        outlandersHostile = false;
    }
}
