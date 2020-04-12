package com.pulselive.jsomner.league_table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Jake Somner
 */
public class LeagueTable {

    /**
     * This exists for debugging purposes only
     *
     * @param args
     */
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

        for (Match m : matches) {
            System.out.println(m.getHomeTeam() + " " + m.getHomeScore() + ":" + m.getAwayScore() + " " + m.getAwayTeam());
        }

        LeagueTable lt = new LeagueTable(matches);
        for (LeagueTableEntry lte : lt.getTableEntries()) {
            System.out.printf("%s\t\t| %d | %d | %d | %d | %d | %d | %d | %d \n",
                    lte.getTeamName(),
                    lte.getPlayed(),
                    lte.getPoints(),
                    lte.getWon(),
                    lte.getDrawn(),
                    lte.getLost(),
                    lte.getGoalsFor(),
                    lte.getGoalsAgainst(),
                    lte.getGoalDifference());
        }
    }
    private List<LeagueTableEntry> tableEntries;
    private List<String> tableNames;

    /**
     * Constructor
     *
     * @param matches List of matches to be turned into LeagueTable
     */
    public LeagueTable(final List<Match> matches) {
        this.tableEntries = new ArrayList<>();
        this.tableNames = new ArrayList<>();

        // Step 1) Get all names
        this.tableNames = readNames(this.tableNames, matches);
        for (String name : tableNames) {
            LeagueTableEntry lte = new LeagueTableEntry(name, 0, 0, 0, 0, 0, 0, 0, 0);
            this.tableEntries.add(lte);
        }
        System.out.println();

        // TODO: Step 3) For each match:
        //  Step 3.1) Who wins/loses?
        //  Step 3.2) What is goal difference?
        //  Step 3.3) Reorganise in list
        for (Match match : matches) {
            String[] players = getPlayers(match);

            /**
             * -1 = home win 0 = draw 1 = away win
             */
            int gameStatus = getStatus(match);
            int[] goalsScored = getGoalsScored(match);

            switch (gameStatus) {
                case (-1):
                    addGame(players[0], 'w', goalsScored[0]);
                    addGame(players[1], 'l', goalsScored[1]);
                    break;
                case (0):
                    addGame(players[0], 'd', goalsScored[0]);
                    addGame(players[1], 'd', goalsScored[1]);
                    break;
                case (1):
                    addGame(players[0], 'l', goalsScored[0]);
                    addGame(players[1], 'w', goalsScored[1]);
                    break;
                default:
                    // If getStatus() works, this should never be read
                    System.out.println("Unexpected outcome");
                    break;
            }
        }
    }

    /**
     * Get the ordered list of league table entries for this league table.
     *
     * @return tableEntries
     */
    public List<LeagueTableEntry> getTableEntries() {
        return this.tableEntries;
    }

    /**
     * Reads all names from list of matches
     *
     * @param tableNames Current list of table names
     * @param matches List of matches to be read
     * @return Updated list of table names
     */
    private List<String> readNames(List<String> tableNames, List<Match> matches) {
        for (Match match : matches) {
            if (!isInList(tableNames, match.getHomeTeam())) {
                tableNames.add(match.getHomeTeam());
                // Step 2) Sort list of names
                Collections.sort(tableNames);
            }

            if (!isInList(tableNames, match.getAwayTeam())) {
                tableNames.add(match.getAwayTeam());
                // Step 2) Sort list of names
                Collections.sort(tableNames);
            }
        }

        return tableNames;
    }

    /**
     * Binary search function, is string in list of strings?
     *
     * @param stringList List of strings to search
     * @param desiredString String being searched for
     * @return True if desiredString is found in stringList
     */
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

    private String[] getPlayers(Match match) {
        String[] results = {match.getHomeTeam(), match.getAwayTeam()};
        return results;
    }

    private int getStatus(Match match) {
        if (match.getHomeScore() > match.getAwayScore()) {
            return -1;
        } else if (match.getHomeScore() < match.getAwayScore()) {
            return 1;
        } else {
            return 0;
        }
    }

    private int[] getGoalsScored(Match match) {
        int[] results = {match.getHomeScore(), match.getAwayScore()};
        return results;
    }

    private void addGame(String player, char c, int i) {
        // Linear search: Where tableEntries.getTeamName == player: 
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
