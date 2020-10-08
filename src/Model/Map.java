package Model;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Map {
    private Player player;
    private Room currentRoom;
    private Room previousRoom;

    private ArrayList<Room> allRooms;
    private ArrayList<Item> allItems;
    private ArrayList<Puzzle> allPuzzles;
    private ArrayList<Monster> allMonsters;

    public Map() {

    }

    public void loadNewGame() throws FileNotFoundException {
        player = FileManager.readPlayer("player.txt");
        allRooms = FileManager.readRooms("rooms.txt");
        allItems = FileManager.readItems("items.txt");
        allPuzzles = FileManager.readPuzzles("puzzles.txt");
        allMonsters = FileManager.readMonsters("monsters.txt");

        currentRoom = allRooms.get(0);
    }

    public void loadSavedGame() throws FileNotFoundException {
        player = FileManager.readPlayer("savePlayer.txt");
        allRooms = FileManager.readRooms("saveRooms.txt");
        allItems = FileManager.readItems("saveItems.txt");
        allPuzzles = FileManager.readPuzzles("savePuzzles.txt");
        allMonsters = FileManager.readMonsters("saveMonsters.txt");

        currentRoom = allRooms.get(0);
    }

    public void saveGameProgress() throws FileNotFoundException {
        FileManager.savePlayer(player);
        FileManager.saveRooms(allRooms);
        FileManager.saveItems(allItems);
        FileManager.savePuzzles(allPuzzles);
        FileManager.saveMonsters(allMonsters);
    }

    private Room getRoom(String roomId) throws InvalidRoomException {
        if (roomId.equals("0")) {
            throw new InvalidRoomException("You cant go that direction");
        }

        Room room = null;
        for (int i = 0; i < allRooms.size(); i++) {
            if (allRooms.get(i).getRoomId().equals(roomId)) {
                room = allRooms.get(i);
            }
        }

        currentRoom.setVisited(true);
        previousRoom = currentRoom;
        return room;
    }

    private Item getItem(String itemName) throws InvalidItemException {

        itemName = itemName.trim();

        Item item = null;
        for (int i = 0; i < allItems.size(); i++) {
            if (allItems.get(i).getItemName().equalsIgnoreCase(itemName)) {
                item = allItems.get(i);
            }
        }

        if (item == null) {
            throw new InvalidItemException("This item is not here");
        }

        return item;
    }

    private Puzzle getPuzzle(String puzzleId) throws InvalidPuzzleException {
        puzzleId = puzzleId.trim();

        Puzzle puzzle = null;

        for (int i = 0; i < allPuzzles.size(); i++) {
            if (allPuzzles.get(i).getId().equals(puzzleId)) {
                puzzle = allPuzzles.get(i);
            }
        }

        if (puzzle == null) {
            throw new InvalidPuzzleException();
        }

        return puzzle;
    }

    private Monster getMonster(String monsterID) throws InvalidMonsterException {

        monsterID = monsterID.trim();

        Monster monster = null;

        for (int i = 0; i < allMonsters.size(); i++) {
            if (allMonsters.get(i).getMonsterID().equals(monsterID)) {
                monster = allMonsters.get(i);
            }
        }

        if (monster == null) {
            throw new InvalidMonsterException();
        }

        return monster;
    }

    public String movePlayerToPreviousRoom() {
        currentRoom.setVisited(true);
        currentRoom = previousRoom;
        return currentRoom.getDescription();
    }

    public String movePlayerTo(String roomID) throws InvalidRoomException {
        if (roomID.equals(currentRoom.getConnection1ID())) {
            return movePlayerToC1();
        } else if (roomID.equals(currentRoom.getConnection2ID())) {
            return movePlayerToC2();
        } else if (roomID.equals(currentRoom.getConnection3ID())) {
            return movePlayerToC3();
        } else if (roomID.equals(currentRoom.getConnection4ID())) {
            return movePlayerToC4();
        } else {
            throw new InvalidRoomException("That room is not valid");
        }
    }

    public boolean isConnectionLocked(String roomID) throws InvalidRoomException {
        if (roomID.equals(currentRoom.getConnection1ID())) {
            return currentRoom.isConnection1Locked();
        } else if (roomID.equals(currentRoom.getConnection2ID())) {
            return currentRoom.isConnection2Locked();
        } else if (roomID.equals(currentRoom.getConnection3ID())) {
            return currentRoom.isConnection3Locked();
        } else if (roomID.equals(currentRoom.getConnection4ID())) {
            return currentRoom.isConnection4Locked();
        } else {
            throw new InvalidRoomException("That room is not valid");
        }
    }

    public String movePlayerToC1() throws InvalidRoomException {
        if (currentRoom.isConnection1Locked()) {
            throw new InvalidRoomException("That room is locked");
        }

        currentRoom = getRoom(currentRoom.getConnection1ID());
        return currentRoom.getDescription();
    }

    public String movePlayerToC2() throws InvalidRoomException {
        if (currentRoom.isConnection2Locked()) {
            throw new InvalidRoomException("That room is locked");
        }

        currentRoom = getRoom(currentRoom.getConnection2ID());
        return currentRoom.getDescription();
    }

    public String movePlayerToC3() throws InvalidRoomException {
        if (currentRoom.isConnection3Locked()) {
            throw new InvalidRoomException("That room is locked");
        }

        currentRoom = getRoom(currentRoom.getConnection3ID());
        return currentRoom.getDescription();
    }

    public String movePlayerToC4() throws InvalidRoomException {
        if (currentRoom.isConnection4Locked()) {
            throw new InvalidRoomException("That room is locked");
        }

        currentRoom = getRoom(currentRoom.getConnection4ID());
        return currentRoom.getDescription();
    }

    public String getCurrentRoomID() {
        return currentRoom.getRoomId();
    }

    public boolean getCurrentRoomIsVisited() {
        return currentRoom.isVisited();
    }

    public boolean getRoomIsVisited(int index) {
        return allRooms.get(index).isVisited();
    }

    public boolean getRoomHasItems(int index) {
        return allRooms.get(index).getItems().size() > 0;
    }

    public String getCurrentRoomDescription() {
        return currentRoom.getDescription();
    }

    public ArrayList<String> getCurrentRoomItems() {
        return currentRoom.getItems();
    }

    public ArrayList<String> getCurrentRoomValidConnections() {
        return currentRoom.getValidConnections();
    }

    public ArrayList<String> getCurrentRoomAllConnections() {
        return currentRoom.getAllConnections();
    }

    public int getCurrentRoomPattern() {
        return currentRoom.getPattern();
    }

    public ArrayList<String> getPlayerInventory() throws InvalidItemException {
        return player.getInventory();
    }

    public String getPlayerInventoryItemType(int index) throws InvalidItemException {
        switch (getItem(player.getInventory().get(index)).getItemType()) {
            case WEAPON:
                return "W";
            case ARMOR:
                return "A";
            case CONSUMABLE:
                return "C";
            case KEY:
                return "K";
            case RELIC:
                return "R";
            default:
                return "W";
        }
    }

    public String getPlayerEquippedItem() {
        return player.getEquippedItem();
    }

    public boolean getIfPlayerEquippedItem() {
        return !player.getEquippedItem().equalsIgnoreCase("");
    }

    public boolean getIfPlayerEquippedKeyItem() {
        return player.getEquippedItem().contains("key") || player.getEquippedItem().contains("Key");
    }

    public String inspectItem(String item) throws InvalidItemException {
        if (!currentRoom.getItems().contains(item) && !player.getInventory().contains(item)) {
            throw new InvalidItemException("This item is not here");
        }
        return getItem(item).getDescription();
    }

    public String pickupPlayerItem(String item) throws InvalidItemException {
        currentRoom.dropItem(item);
        return player.pickupItem(item);
    }

    public String dropPlayerItem(String item) throws InvalidItemException {
        currentRoom.pickupItem(item);
        return player.dropItem(item);
    }

    public String equipPlayerItem(String item) throws InvalidItemException {
        return player.setEquippedItem(item);
    }

    public String unequipPlayerItem() {
        return player.clearEquippedItem();
    }

    public boolean compareEquipedPlayerItem(String item) {
        return item.equals(player.getEquippedItem());
    }

    public boolean isCurrentRoomPuzzleSolved() {
        try {
            return getPuzzle(currentRoom.getPuzzleId()).isSolved();
        } catch (InvalidPuzzleException e) {
            return true;
        }
    }

    public String getPuzzleQuestion() throws InvalidPuzzleException {
        return getPuzzle(currentRoom.getPuzzleId()).getQuestion();
    }

    public boolean solveCurrentRoomPuzzle(String text) throws InvalidPuzzleException {
        return getPuzzle(currentRoom.getPuzzleId()).solvePuzzle(text);
    }

    public int getPuzzleAttempts() throws InvalidPuzzleException {
        return getPuzzle(currentRoom.getPuzzleId()).getAttempts();
    }

    public int getPuzzleAttemptsRemaining() throws InvalidPuzzleException {
        return getPuzzle(currentRoom.getPuzzleId()).getAttemptsRemaining();
    }

    public void puzzleReduceAttemptsRemaining() throws InvalidPuzzleException {
        getPuzzle(currentRoom.getPuzzleId()).reduceAttemptsRemaining();
    }

    public String getPuzzleHint() throws InvalidPuzzleException {
        return getPuzzle(currentRoom.getPuzzleId()).getHint();
    }

    public int applyPuzzleDamageToPlayer() throws InvalidPuzzleException {
        int damage = getPuzzle(currentRoom.getPuzzleId()).getDamage();
        player.acceptDamage(damage);
        return damage;
    }

    public boolean isCurrentRoomMonsterDead() {
        try {
            return getMonster(currentRoom.getMonsterID()).isMonsterDead();
        } catch (InvalidMonsterException e) {
            return true;
        }
    }

    public String getMonsterName() throws InvalidMonsterException {
        return getMonster(currentRoom.getMonsterID()).getName();
    }

    public String getMonsterDescription() throws InvalidMonsterException {
        String description = "";
        Monster monster = getMonster(currentRoom.getMonsterID());

        description += "Name: " + monster.getName();
        description += "\nDescription:" + monster.getDescription();
        description += "\nHealth: " + monster.getHealthPoints();
        description += "\nDamage: " + monster.getAttackPoints();

        return description;
    }

    public String getPlayerStats() {
        return player.getStats();
    }

    public String getMonsterStats() throws InvalidMonsterException {
        return getMonster(currentRoom.getMonsterID()).getStats();
    }

    public int getMonsterHealth() throws InvalidMonsterException {
        Monster monster = getMonster(currentRoom.getMonsterID());

        return monster.getHealthPoints();
    }

    public int getMonsterMaxHealth() throws InvalidMonsterException {
        Monster monster = getMonster(currentRoom.getMonsterID());

        return monster.getMaxHealthPoints();
    }

    public int getMonsterDamage() throws InvalidMonsterException {
        return getMonster(currentRoom.getMonsterID()).getAttackPoints();
    }

    public int getPlayerDamage() {
        int playerDamage = player.getDefaultAttack();

        try {
            Item equippedItem = getItem(player.getEquippedItem());
            if (equippedItem == null || equippedItem.getItemType() != ItemType.WEAPON) {

            } else {
                playerDamage += equippedItem.getItemDelta();
            }
        } catch (InvalidItemException e) {

        }

        return playerDamage;
    }

    public int getPlayerHealth() {
        return player.getHealth();
    }

    public int getPlayerMaxHealth() {
        return player.getMaxHealthPoints();
    }

    public String playerAttacksMonster() throws InvalidMonsterException {
        Monster monster = getMonster(currentRoom.getMonsterID());
        Item equippedItem = null;
        try {
            equippedItem = getItem(player.getEquippedItem());
        } catch (InvalidItemException e) {

        }

        if (equippedItem == null || equippedItem.getItemType() != ItemType.WEAPON) {
            monster.acceptDamage(player.getDefaultAttack());
            return "player did " + player.getDefaultAttack() + " damage";
        } else {
            monster.acceptDamage(player.getDefaultAttack() + equippedItem.getItemDelta());
            return "player did " + (player.getDefaultAttack() + equippedItem.getItemDelta()) + " damage";
        }

    }

    public int monsterAttacksPlayer() throws InvalidMonsterException {
        Monster monster = getMonster(currentRoom.getMonsterID());
        int damage = monster.getAttackPoints();

        Item equippedItem = null;
        try {
            equippedItem = getItem(player.getEquippedItem());
        } catch (InvalidItemException e) {

        }

        if (equippedItem == null || equippedItem.getItemType() != ItemType.ARMOR) {
            player.acceptDamage(damage);
        } else {
            damage -= equippedItem.getItemDelta();
            player.acceptDamage(damage);
        }

        return damage;
    }

    public String playerHeal() throws InvalidItemException {
        Item equippedItem = null;

        try {
            equippedItem = getItem(player.getEquippedItem());
        } catch (InvalidItemException e) {

        }

        if (equippedItem == null || equippedItem.getItemType() != ItemType.CONSUMABLE) {
            throw new InvalidItemException("Consumable must be equipped to use");
        } else {
            player.acceptHeal(equippedItem.getItemDelta());
            player.dropItem(player.getEquippedItem());
            player.clearEquippedItem();
        }

        return equippedItem.getItemName() + " is used";
    }

    public String playerUnlocksDoor(String connection) throws InvalidItemException {
        Item equippedItem = null;
        try {
            equippedItem = getItem(player.getEquippedItem());
        } catch (InvalidItemException e) {

        }

        if (equippedItem == null || equippedItem.getItemType() != ItemType.KEY) {
            throw new InvalidItemException("Key must be equipped.");
        }

        if (connection.equalsIgnoreCase(currentRoom.getConnection1ID())) {
            if (currentRoom.isConnection1Locked()) {
                if (equippedItem.getItemDelta() == currentRoom.getConnection1Key()) {
                    currentRoom.setConnection1Locked(false);

                    player.dropItem(player.getEquippedItem());
                    player.clearEquippedItem();

                    return connection + " is now unlocked";
                } else {
                    throw new InvalidItemException(equippedItem.getItemName() + " is not the right key");
                }
            } else {
                throw new InvalidItemException(connection + " is already unlocked");
            }

        } else if (connection.equalsIgnoreCase(currentRoom.getConnection2ID())) {
            if (currentRoom.isConnection2Locked()) {
                if (equippedItem.getItemDelta() == currentRoom.getConnection2Key()) {
                    currentRoom.setConnection2Locked(false);

                    player.dropItem(player.getEquippedItem());
                    player.clearEquippedItem();

                    return connection + " is now unlocked";
                } else {
                    throw new InvalidItemException(equippedItem.getItemName() + " is not the right key");
                }
            } else {
                throw new InvalidItemException(connection + " is already unlocked");
            }
        } else if (connection.equalsIgnoreCase(currentRoom.getConnection3ID())) {
            if (currentRoom.isConnection3Locked()) {
                if (equippedItem.getItemDelta() == currentRoom.getConnection3Key()) {
                    currentRoom.setConnection3Locked(false);

                    player.dropItem(player.getEquippedItem());
                    player.clearEquippedItem();

                    return connection + " is now unlocked";
                } else {
                    throw new InvalidItemException(equippedItem.getItemName() + " is not the right key");
                }
            } else {
                throw new InvalidItemException(connection + " is already unlocked");
            }
        } else if (connection.equalsIgnoreCase(currentRoom.getConnection4ID())) {
            if (currentRoom.isConnection4Locked()) {
                if (equippedItem.getItemDelta() == currentRoom.getConnection4Key()) {
                    currentRoom.setConnection4Locked(false);

                    player.dropItem(player.getEquippedItem());
                    player.clearEquippedItem();

                    return connection + " is now unlocked";
                } else {
                    throw new InvalidItemException(equippedItem.getItemName() + " is not the right key");
                }
            } else {
                throw new InvalidItemException(connection + " is already unlocked");
            }
        } else {
            throw new InvalidItemException(connection + " is not a valid direction");
        }
    }

    public boolean getWinCondition() {
        for (int i = 0; i < player.getInventory().size(); i++) {
            Item item = null;
            try {
                item = getItem(player.getInventory().get(i));
            } catch (InvalidItemException e) {

            }

            if (item != null && item.getItemType() == ItemType.RELIC) {
                return true;
            }
        }
        return false;
    }
}