
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jake Somner
 * @email JSomner@iCloud.com
 * @version 14 April 2020
 */
public class LeagueTable {

    private final List<LeagueTableEntry> tableEntries;
    private List<String> tableNames;

    /**
     * Constructor
     *
     * @param matches List of matches to be turned into a League Table
     */
    public LeagueTable(final List<Match> matches) {
        this.tableEntries = new ArrayList<>();
        this.tableNames = new ArrayList<>();

        // Pass 1: Read all names, remove duplicates, order alphabetically
        this.tableNames = readNames(this.tableNames, matches);
        // Now add this sorted list to the tableEntries by creating a series of new instances, each with 0 for the int values - as if no matches have yet been played
        for (String name : this.tableNames) {
            LeagueTableEntry lte = new LeagueTableEntry(name, 0, 0, 0, 0, 0, 0, 0, 0);
            this.tableEntries.add(lte);
        }

        // Pass 2: For each match, get the teams, the result and the score. Use this information to update the League Table, one match result at a time
        for (Match match : matches) {
            String[] teams = getTeams(match);
            int gameStatus = getStatus(match);
            int[] goalsScored = getGoalsScored(match);

            addGame(teams, gameStatus, goalsScored);
        }

        // Sort the table as specified in the brief (Points, Goal Difference, Goals For, Team Name)
        sortTable(this.tableEntries, this.tableEntries.size());
    }

    /**
     * Get the ordered list of league table entries for this league table.
     *
     * @return List of League Table Entries
     */
    public List<LeagueTableEntry> getTableEntries() {
        return this.tableEntries;
    }

    /**
     * Reads all matches and returns a list of unique team names
     *
     * @param tableNames Current list of team names
     * @param matches List of matches
     * @return Updated list of team names
     */
    private List<String> readNames(List<String> tableNames, List<Match> matches) {
        // If the team name already exists in the list of team names, reject it. If it doesn't exist, add it.
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

    /**
     * Binary search functions
     *
     * @param stringList Sorted list of strings to be searched
     * @param desiredString String being searched for
     * @return True if desiredString is found in stringList
     */
    private boolean isInList(List<String> stringList, String desiredString) {
        // If the list is empty, it won't contain anything
        if (stringList.isEmpty()) {
            return false;
        }

        // Otherwise, perform a binary search (this is performed on a sorted list of strings - stringList)
        int low = 0;
        int high = stringList.size() - 1;
        int mid;

        // At this point we are not interested in *where* in the list the item is, only whether it exists or not
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

    /**
     * Returns both teams in a given match
     *
     * @param match A given match
     * @return An array in the form [Home team, away team]
     */
    private String[] getTeams(Match match) {
        String[] results = {match.getHomeTeam(), match.getAwayTeam()};
        return results;
    }

    /**
     * Returns the result of a given match. -1: A home win 0: A draw 1: An away
     * win
     *
     * @param match A given match
     * @return Status of the match
     */
    private int getStatus(Match match) {
        // This allowed for a simple indicator of who won a given match
        if (match.getHomeScore() > match.getAwayScore()) {
            return -1;
        } else if (match.getHomeScore() < match.getAwayScore()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Returns an array containing both home and away goals in a given match
     *
     * @param match A given match
     * @return An array in the form [Home score, Away score]
     */
    private int[] getGoalsScored(Match match) {
        int[] results = {match.getHomeScore(), match.getAwayScore()};
        return results;
    }

    /**
     * Adds a match's results to the league table
     *
     * @param teams A string array of the teams in the match in the form [Home
     * team, Away team]
     * @param result The status of the match (-1, 0, 1)
     * @param goalsScored An int array of the goals scored in the match in the
     * form [Home score, Away score]
     */
    private void addGame(String[] teams, int result, int[] goalsScored) {
        for (LeagueTableEntry lte : this.tableEntries) {
            switch (result) {
                case (-1):
                    // If the home team won, give them three points and give the away team 0 points

                    // Here we switch to a linear search because we are looking for a string in a list of objects
                    if (lte.getTeamName().compareTo(teams[0]) == 0) {
                        lte.setPlayed(lte.getPlayed() + 1);
                        lte.setWon(lte.getWon() + 1);
                        lte.setPoints(lte.getPoints() + 3);
                        lte.setGoalsFor(lte.getGoalsFor() + goalsScored[0]);
                        lte.setGoalsAgainst(lte.getGoalsAgainst() + goalsScored[1]);
                        lte.setGoalDifference(lte.getGoalsFor() - lte.getGoalsAgainst());
                    }
                    // The linear search is performed twice per match, once for each team
                    if (lte.getTeamName().compareTo(teams[1]) == 0) {
                        lte.setPlayed(lte.getPlayed() + 1);
                        lte.setLost(lte.getLost() + 1);
                        // lte.setPoints(lte.getPoints() + 0);
                        lte.setGoalsFor(lte.getGoalsFor() + goalsScored[1]);
                        lte.setGoalsAgainst(lte.getGoalsAgainst() + goalsScored[0]);
                        lte.setGoalDifference(lte.getGoalsFor() - lte.getGoalsAgainst());
                    }
                    break;
                case (0):
                    // If the game is a draw, give both teams one point
                    if (lte.getTeamName().compareTo(teams[0]) == 0) {
                        lte.setPlayed(lte.getPlayed() + 1);
                        lte.setDrawn(lte.getDrawn() + 1);
                        lte.setPoints(lte.getPoints() + 1);
                        lte.setGoalsFor(lte.getGoalsFor() + goalsScored[0]);
                        lte.setGoalsAgainst(lte.getGoalsAgainst() + goalsScored[1]);
                        lte.setGoalDifference(lte.getGoalsFor() - lte.getGoalsAgainst());
                    }
                    if (lte.getTeamName().compareTo(teams[1]) == 0) {
                        lte.setPlayed(lte.getPlayed() + 1);
                        lte.setDrawn(lte.getDrawn() + 1);
                        lte.setPoints(lte.getPoints() + 1);
                        lte.setGoalsFor(lte.getGoalsFor() + goalsScored[1]);
                        lte.setGoalsAgainst(lte.getGoalsAgainst() + goalsScored[0]);
                        lte.setGoalDifference(lte.getGoalsFor() - lte.getGoalsAgainst());
                    }
                    break;
                case (1):
                    // If the away team win, give them three points and the home team 0 points
                    if (lte.getTeamName().compareTo(teams[0]) == 0) {
                        lte.setPlayed(lte.getPlayed() + 1);
                        lte.setLost(lte.getDrawn() + 1);
                        // lte.setPoints(lte.getPoints() + 0);
                        lte.setGoalsFor(lte.getGoalsFor() + goalsScored[0]);
                        lte.setGoalsAgainst(lte.getGoalsAgainst() + goalsScored[1]);
                        lte.setGoalDifference(lte.getGoalsFor() - lte.getGoalsAgainst());
                    }
                    if (lte.getTeamName().compareTo(teams[1]) == 0) {
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

    /**
     * This function, combined with mergeLists, perform a Merge Sort to organise
     * the League Table Entries in the desired way. It is called recursively
     * until the tableEntries parameter is only a single item. It will then
     * begin to merge them back towards a single item, ordering them in the
     * process.
     *
     * @param tableEntries The unsorted list of entries
     * @param size The length of the unsorted list
     */
    private void sortTable(List<LeagueTableEntry> tableEntries, int size) {
        // Once the tableEntries parameter is only a single unit, move on to merging them
        if (size < 2) {
            return;
        }

        int mid = size / 2;

        List<LeagueTableEntry> leftList = new ArrayList<>();
        List<LeagueTableEntry> rightList = new ArrayList<>();

        // Separate the given list into two smaller lists
        for (int pointer = 0; pointer < mid; pointer++) {
            leftList.add(tableEntries.get(pointer));
        }
        for (int pointer = mid; pointer < size; pointer++) {
            rightList.add(tableEntries.get(pointer));
        }

        // Perform the same function on each of the new lists until the escape condition (size = 1) is satisfied
        sortTable(leftList, mid);
        sortTable(rightList, size - mid);

        // Once we have our lists separated, we will begin to merge them
        mergeLists(tableEntries, leftList, rightList, mid, size - mid);
    }

    /**
     * Merges separated lists back together in the order specified by the brief
     * (Points, Goal Difference, Goals For, Team Name)
     *
     * @param listOfTeams The original unsorted list
     * @param leftList The 'left' of the two separated lists
     * @param rightList The 'right' of the two separated lists
     * @param leftSize The size of the 'left' list
     * @param rightSize The size of the 'right' list
     */
    private void mergeLists(List<LeagueTableEntry> listOfTeams, List<LeagueTableEntry> leftList, List<LeagueTableEntry> rightList, int leftSize, int rightSize) {
        // The following are used to indicate which position in the three lists is being accessed
        // The position in the 'left' list
        int leftPointer = 0;
        // The position in the 'right' list
        int rightPointer = 0;
        // The position in the 'merged' or 'final' list
        int mergedPointer = 0;

        // While there are items in both lists, compare them
        // Add the larger of the two items (i.e. the one with more points) to the first position in the original list
        while (leftPointer < leftSize && rightPointer < rightSize) {
            if (leftList.get(leftPointer).getPoints() > rightList.get(rightPointer).getPoints()) {
                // If the team on the left has more points than the team on the right, it is considered larger and should be sorted into the higher position
                listOfTeams.set(mergedPointer, leftList.get(leftPointer));
                mergedPointer++;
                leftPointer++;
            } else if (leftList.get(leftPointer).getPoints() < rightList.get(rightPointer).getPoints()) {
                listOfTeams.set(mergedPointer, rightList.get(rightPointer));
                mergedPointer++;
                rightPointer++;
            } else {
                // If both teams have the same points, the next criteria is compared - Goal Difference
                // If the team on the left has a higher goal difference than the team on the right, it is considered larger and should be sorted into the higher position
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
                    // If both teams have the same goal difference, the next criteria is compared - Goals For
                    // If the team on the left has a higher number of goals scored than the team on the right, it is considered larger and should be sorted into the higher position
                    if (leftList.get(leftPointer).getGoalsFor() > rightList.get(rightPointer).getGoalsFor()) {
                        listOfTeams.set(mergedPointer, leftList.get(leftPointer));
                        mergedPointer++;
                        leftPointer++;
                    } else if (leftList.get(leftPointer).getGoalsFor() < rightList.get(rightPointer).getGoalsFor()) {
                        listOfTeams.set(mergedPointer, rightList.get(rightPointer));
                        mergedPointer++;
                        rightPointer++;
                    } else {
                        // If both teams have the same number of goals scored, the next criteria is compared - Team Name
                        // If the team on the left has a name that is earlier in the alphabet than the team on the right, it is considered larger and should be sorted into the higher position

                        // The compareTo function uses character value to determine if one string is greater than or equal to another
                        // Here: A negative int is returned if the 'left team' comes before the 'right team' in the alphabet
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

        // Once one of the two lists has been completed, further comparisons aren't required.
        // The remining objects can be done in one action
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