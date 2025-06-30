package com.mykosan.model;

public class Room {
    private int roomId;
    private String roomName;
    private int price;
    private boolean occupied;

    public Room(int roomId, String roomName, int price, boolean occupied) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.price = price;
        this.occupied = occupied;
    }

    public int getRoomId() { return roomId; }
    public String getRoomName() { return roomName != null ? roomName : ""; }
    public int getPrice() { return price; }
    public boolean isOccupied() { return occupied; }

    public void setRoomId(int roomId) { this.roomId = roomId; }
    public void setRoomName(String roomName) { this.roomName = roomName; }
    public void setPrice(int price) { this.price = price; }
    public void setOccupied(boolean occupied) { this.occupied = occupied; }
}