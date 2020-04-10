package com.pulselive.jsomner.league_table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Jake Somner
 */
public class LeagueTable {

    public static void main(String[] args) {
        Match m1 = new Match("Charlton Athletic", "Everton", 1, 2);
        Match m2 = new Match("Derby", "Blackburn Rovers", 2, 1);
        Match m3 = new Match("Leeds", "Southampton", 2, 0);
        Match m12 = new Match("Ipswitch", "Derby", 3, 1);

        List<Match> matches = new ArrayList<>();
        matches.add(m1);
        matches.add(m2);
        matches.add(m3);
        matches.add(m12);

        System.out.println(matches);

        LeagueTable lt = new LeagueTable(matches);

        System.out.println(lt);
    }
    private List<LeagueTableEntry> tableEntries;
    private List<String> tableNames;

    public LeagueTable(final List<Match> matches) {
        this.tableEntries = new ArrayList<>();
        this.tableNames = new ArrayList<>();

        /**
         * Step 1) Get all names
         * Step 2) Sort list of names
         * Step 3) For each match:
         * Step 3.1) Who wins/loses?
         * Step 3.2) What is goal difference?
         * Step 3.3) Reorganise in list
         */
        // Step 1
        this.tableNames = readNames(this.tableNames, matches);
    }

    /**
     * Get the ordered list of league table entries for this league table.
     *
     * @return
     */
    public List<LeagueTableEntry> getTableEntries() {
        return this.tableEntries;
    }

    private List<String> readNames(List<String> tableNames, List<Match> matches) {
        for (Match match : matches) {
            if (!isInList(tableNames, match.getHomeTeam())) {
                tableNames.add(match.getHomeTeam());
                Collections.sort(tableNames);
            }

            if (!isInList(tableNames, match.getAwayTeam())) {
                tableNames.add(match.getAwayTeam());
                Collections.sort(tableNames);
            }
        }

        return tableNames;
    }

    private boolean isInList(List<String> stringList, String desiredString) {
        if (stringList.isEmpty()) {
            return false;
        }

        int low = 0;
        int high = stringList.size() - 1;
        int mid;

        boolean found = false;
        while (low <= high && !found) {
            mid = (low + high) / 2;
            if (stringList.get(mid).compareTo(desiredString) < 0) {
                low = mid + 1;
            } else if (stringList.get(mid).compareTo(desiredString) > 0) {
                high = mid - 1;
            } else {
                found = true;
            }
        }
        return found;
    }
}
