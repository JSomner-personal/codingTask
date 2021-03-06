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
        Match m4 = new Match("Leicester", "Bolton Wanderers", 0, 5);
        Match m5 = new Match("Liverpool", "West Ham", 2, 1);
        Match m6 = new Match("Middlesbrough", "Arsenal", 0, 4);
        Match m7 = new Match("Sunderland", "Ipswich", 1, 0);
        Match m8 = new Match("Tottenham Hotspurs", "Aston Villa", 0, 0);
        Match m9 = new Match("Chelsea", "Newcastle", 1, 1);
        Match m10 = new Match("Manchester United", "Fulham", 3, 2);
        Match m11 = new Match("Everton", "Tottenham Hotspurs", 1, 1);
        Match m12 = new Match("Ipswich", "Derby", 3, 1);
        Match m13 = new Match("Arsenal", "Leeds", 1, 2);
        Match m14 = new Match("Bolton Wanderers", "Middlesbrough", 1, 0);
        Match m15 = new Match("Fulham", "Sunderland", 2, 0);
        Match m16 = new Match("Blackburn Rovers", "Manchester United", 2, 2);

        List<Match> matches = new ArrayList<>();
        matches.add(m1);
        matches.add(m2);
        matches.add(m3);
        matches.add(m4);
        matches.add(m5);
        matches.add(m6);
        matches.add(m7);
        matches.add(m8);
        matches.add(m9);
        matches.add(m10);
        matches.add(m11);
        matches.add(m12);
        matches.add(m13);
        matches.add(m14);
        matches.add(m15);
        matches.add(m16);

        matches.forEach((m) -> {
            System.out
                    .println(m.getHomeTeam() + " " + m.getHomeScore() + ":" + m.getAwayScore() + " " + m.getAwayTeam());
        });

        LeagueTable lt = new LeagueTable(matches);
        System.out.println(lt);
    }

    private final List<LeagueTableEntry> tableEntries;
    private List<String> tableNames;

    /**
     * Constructor
     *
     * @param matches List of matches to be turned into LeagueTable
     */
    public LeagueTable(final List<Match> matches) {
        this.tableEntries = new ArrayList<>();
        this.tableNames = new ArrayList<>();

        this.tableNames = readNames(this.tableNames, matches);
        tableNames.stream().map((name) -> new LeagueTableEntry(name, 0, 0, 0, 0, 0, 0, 0, 0)).forEachOrdered((lte) -> {
            this.tableEntries.add(lte);
        });
        System.out.println();

        matches.forEach((match) -> {
            String[] players = getPlayers(match);

            /**
             * -1 = home win 0 = draw 1 = away win
             */
            int gameStatus = getStatus(match);
            int[] goalsScored = getGoalsScored(match);

            addGame(players, gameStatus, goalsScored);
        });

        sortTable(this.tableEntries, this.tableEntries.size());

        System.out.println("--------------------------------------------");

        this.tableEntries.forEach((lte) -> {
            System.out.printf("%s\n| %d | %d | %d | %d | %d | %d | %d\t| %d \n", lte.getTeamName(), lte.getPlayed(),
                    lte.getWon(), lte.getDrawn(), lte.getLost(), lte.getGoalsFor(), lte.getGoalsAgainst(),
                    lte.getGoalDifference(), lte.getPoints());
        });
    }

    public List<LeagueTableEntry> getTableEntries() {
        return this.tableEntries;
    }

    private List<String> readNames(List<String> tableNames, List<Match> matches) {
        matches.stream().map((match) -> {
            if (!isInList(tableNames, match.getHomeTeam())) {
                tableNames.add(match.getHomeTeam());
                Collections.sort(tableNames);
            }
            return match;
        }).filter((match) -> (!isInList(tableNames, match.getAwayTeam()))).map((match) -> {
            tableNames.add(match.getAwayTeam());
            return match;
        }).forEachOrdered((_item) -> {
            // Step 2) Sort list of names
            Collections.sort(tableNames);
        });

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

    private void addGame(String[] players, int result, int[] goalsScored) {
        // Linear search: Where tableEntries.getTeamName == player:
        for (LeagueTableEntry lte : this.tableEntries) {
            switch (result) {
                case (-1):
                    if (lte.getTeamName().compareTo(players[0]) == 0) {
                        lte.setPlayed(lte.getPlayed() + 1);
                        lte.setWon(lte.getWon() + 1);
                        lte.setPoints(lte.getPoints() + 3);
                        lte.setGoalsFor(lte.getGoalsFor() + goalsScored[0]);
                        lte.setGoalsAgainst(lte.getGoalsAgainst() + goalsScored[1]);
                        lte.setGoalDifference(lte.getGoalsFor() - lte.getGoalsAgainst());
                    }
                    if (lte.getTeamName().compareTo(players[1]) == 0) {
                        lte.setPlayed(lte.getPlayed() + 1);
                        lte.setLost(lte.getLost() + 1);
                        lte.setPoints(lte.getPoints() + 0);
                        lte.setGoalsFor(lte.getGoalsFor() + goalsScored[1]);
                        lte.setGoalsAgainst(lte.getGoalsAgainst() + goalsScored[0]);
                        lte.setGoalDifference(lte.getGoalsFor() - lte.getGoalsAgainst());
                    }
                    break;
                case (0):
                    if (lte.getTeamName().compareTo(players[0]) == 0) {
                        lte.setPlayed(lte.getPlayed() + 1);
                        lte.setDrawn(lte.getDrawn() + 1);
                        lte.setPoints(lte.getPoints() + 1);
                        lte.setGoalsFor(lte.getGoalsFor() + goalsScored[0]);
                        lte.setGoalsAgainst(lte.getGoalsAgainst() + goalsScored[1]);
                        lte.setGoalDifference(lte.getGoalsFor() - lte.getGoalsAgainst());
                    }
                    if (lte.getTeamName().compareTo(players[1]) == 0) {
                        lte.setPlayed(lte.getPlayed() + 1);
                        lte.setDrawn(lte.getDrawn() + 1);
                        lte.setPoints(lte.getPoints() + 1);
                        lte.setGoalsFor(lte.getGoalsFor() + goalsScored[1]);
                        lte.setGoalsAgainst(lte.getGoalsAgainst() + goalsScored[0]);
                        lte.setGoalDifference(lte.getGoalsFor() - lte.getGoalsAgainst());
                    }
                    break;
                case (1):
                    if (lte.getTeamName().compareTo(players[0]) == 0) {
                        lte.setPlayed(lte.getPlayed() + 1);
                        lte.setLost(lte.getDrawn() + 1);
                        lte.setPoints(lte.getPoints() + 0);
                        lte.setGoalsFor(lte.getGoalsFor() + goalsScored[0]);
                        lte.setGoalsAgainst(lte.getGoalsAgainst() + goalsScored[1]);
                        lte.setGoalDifference(lte.getGoalsFor() - lte.getGoalsAgainst());
                    }
                    if (lte.getTeamName().compareTo(players[1]) == 0) {
                        lte.setPlayed(lte.getPlayed() + 1);
                        lte.setWon(lte.getWon() + 1);
                        lte.setPoints(lte.getPoints() + 3);
                        lte.setGoalsFor(lte.getGoalsFor() + goalsScored[1]);
                        lte.setGoalsAgainst(lte.getGoalsAgainst() + goalsScored[0]);
                        lte.setGoalDifference(lte.getGoalsFor() - lte.getGoalsAgainst());
                    }
                    break;
            }
        }
    }

    private void sortTable(List<LeagueTableEntry> tableEntries, int size) {
        if (size < 2) {
            return;
        }

        int mid = size / 2;

        List<LeagueTableEntry> leftList = new ArrayList<>();
        List<LeagueTableEntry> rightList = new ArrayList<>();

        for (int pointer = 0; pointer < mid; pointer++) {
            leftList.add(tableEntries.get(pointer));
        }
        for (int pointer = mid; pointer < size; pointer++) {
            rightList.add(tableEntries.get(pointer));
        }

        sortTable(leftList, mid);
        sortTable(rightList, size - mid);

        mergeLists(tableEntries, leftList, rightList, mid, size - mid);
    }

    private void mergeLists(List<LeagueTableEntry> listOfTeams, List<LeagueTableEntry> leftList,
            List<LeagueTableEntry> rightList, int leftSize, int rightSize) {
        int leftPointer = 0;
        int rightPointer = 0;
        int mergedPointer = 0;

        while (leftPointer < leftSize && rightPointer < rightSize) {
            if (leftList.get(leftPointer).getPoints() > rightList.get(rightPointer).getPoints()) {
                listOfTeams.set(mergedPointer, leftList.get(leftPointer));
                mergedPointer++;
                leftPointer++;
            } else if (leftList.get(leftPointer).getPoints() < rightList.get(rightPointer).getPoints()) {
                listOfTeams.set(mergedPointer, rightList.get(rightPointer));
                mergedPointer++;
                rightPointer++;
            } else {
                if (leftList.get(leftPointer).getGoalDifference() > rightList.get(rightPointer).getGoalDifference()) {
                    listOfTeams.set(mergedPointer, leftList.get(leftPointer));
                    mergedPointer++;
                    leftPointer++;
                } else if (leftList.get(leftPointer).getGoalDifference() < rightList.get(rightPointer)
                        .getGoalDifference()) {
                    listOfTeams.set(mergedPointer, rightList.get(rightPointer));
                    mergedPointer++;
                    rightPointer++;
                } else {
                    if (leftList.get(leftPointer).getGoalsFor() > rightList.get(rightPointer).getGoalsFor()) {
                        listOfTeams.set(mergedPointer, leftList.get(leftPointer));
                        mergedPointer++;
                        leftPointer++;
                    } else if (leftList.get(leftPointer).getGoalsFor() < rightList.get(rightPointer).getGoalsFor()) {
                        listOfTeams.set(mergedPointer, rightList.get(rightPointer));
                        mergedPointer++;
                        rightPointer++;
                    } else {
                        if (leftList.get(leftPointer).getTeamName()
                                .compareTo(rightList.get(rightPointer).getTeamName()) < 0) {
                            listOfTeams.set(mergedPointer, leftList.get(leftPointer));
                            mergedPointer++;
                            leftPointer++;
                        } else if (leftList.get(leftPointer).getTeamName()
                                .compareTo(rightList.get(rightPointer).getTeamName()) > 0) {
                            listOfTeams.set(mergedPointer, rightList.get(rightPointer));
                            mergedPointer++;
                            rightPointer++;
                        }
                    }
                }
            }
        }

        while (leftPointer < leftSize) {
            listOfTeams.set(mergedPointer, leftList.get(leftPointer));
            mergedPointer++;
            leftPointer++;
        }
        while (rightPointer < rightSize) {
            listOfTeams.set(mergedPointer, rightList.get(rightPointer));
            mergedPointer++;
            rightPointer++;
        }
    }
}
