package dansplugins.activitytracker.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dansplugins.activitytracker.utils.Logger;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Session implements ISession, Savable {

    private int ID;
    private UUID playerUUID;
    private LocalDateTime loginDate;
    private LocalDateTime logoutDate;
    private double minutesSpent;
    private boolean active;

    public Session(int ID, UUID playerUUID) {
        this.ID = ID;
        this.playerUUID = playerUUID;
        loginDate = LocalDateTime.now();
        active = true;
    }

    public Session(Map<String, String> data) {
        this.load(data);
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public UUID getPlayerUUID() {
        return playerUUID;
    }

    @Override
    public LocalDateTime getLoginDate() {
        return loginDate;
    }

    @Override
    public LocalDateTime getLogoutDate() {
        return logoutDate;
    }

    @Override
    public double getMinutesSpent() {
        return minutesSpent;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean b) {
        active = b;
    }

    @Override
    public boolean endSession() {
        if (!active) {
            return false;
        }
        logoutDate = LocalDateTime.now();
        minutesSpent = calculateMinutesSpent();
        setActive(false);
        return true;
    }

    private double calculateMinutesSpent() {
        Duration duration = Duration.between(loginDate, logoutDate);
        double seconds = duration.getSeconds();
        double minutes = seconds / 60;
        Logger.getInstance().log("Minutes calculated: " + minutes);
        return minutes;
    }

    @Override
    public Map<String, String> save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Map<String, String> saveMap = new HashMap<>();
        try {
            saveMap.put("ID", gson.toJson(ID));
            saveMap.put("playerUUID", gson.toJson(playerUUID));
            saveMap.put("loginDate", gson.toJson(loginDate.toString()));
            saveMap.put("logoutDate", gson.toJson(logoutDate.toString()));
            saveMap.put("minutesSpent", gson.toJson(minutesSpent));
            saveMap.put("active", gson.toJson(active));
        } catch (Exception e) {
            Logger.getInstance().log("Something went wrong saving a session.");
        }

        return saveMap;
    }

    @Override
    public void load(Map<String, String> data) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        ID = Integer.parseInt(gson.fromJson(data.get("ID"), String.class));
        playerUUID = UUID.fromString(gson.fromJson(data.get("playerUUID"), String.class));
        loginDate = LocalDateTime.parse(gson.fromJson(data.get("loginDate"), String.class));
        logoutDate = LocalDateTime.parse(gson.fromJson(data.get("logoutDate"), String.class));
        minutesSpent = Double.parseDouble(gson.fromJson(data.get("minutesSpent"), String.class));
        active = Boolean.parseBoolean(gson.fromJson(data.get("active"), String.class));
    }

}