package dansplugins.activitytracker.data;

import dansplugins.activitytracker.objects.ActivityRecord;
import dansplugins.activitytracker.objects.Session;
import dansplugins.activitytracker.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class PersistentData {

    private static PersistentData instance;

    private ArrayList<ActivityRecord> activityRecords = new ArrayList<>();

    private PersistentData() {

    }

    public static PersistentData getInstance() {
        if (instance == null) {
            instance = new PersistentData();
        }
        return instance;
    }

    public ArrayList<ActivityRecord> getActivityRecords() {
        return activityRecords;
    }

    public ActivityRecord getActivityRecord(Player player) {
        return getActivityRecord(player.getUniqueId());
    }

    public ActivityRecord getActivityRecord(UUID playerUUID) {
        for (ActivityRecord record : activityRecords) {
            if (record.getPlayerUUID().equals(playerUUID)) {
                return record;
            }
        }
        return null;
    }

    public void addRecord(ActivityRecord recordToAdd) {
        if (!activityRecords.contains(recordToAdd)) {
            activityRecords.add(recordToAdd);
        }
    }

    public void removeRecord(ActivityRecord recordToRemove) {
        activityRecords.remove(recordToRemove);
    }

    public Session getSession(int sessionID) {
        for (ActivityRecord record : activityRecords) {
            Session session = record.getSession(sessionID);
            if (session != null) {
                return session;
            }
        }
        return null;
    }

    public void endCurrentSessions() {
        Logger.getInstance().log("Ending current sessions.");
        for (Player player : Bukkit.getOnlinePlayers()) {
            ActivityRecord record = getActivityRecord(player);
            record.getMostRecentSession().endSession();
        }
    }
}
