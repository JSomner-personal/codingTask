package com.pulselive.jsomner.league_table;

import java.util.ArrayList;
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
        
        List<Match> matches = new ArrayList<>();
        matches.add(m1);
        matches.add(m2);
        matches.add(m3);
        
        System.out.println(matches);
        
        LeagueTable lt = new LeagueTable(matches);
        
        System.out.println(lt);
    }

    public LeagueTable(final List<Match> matches) {
    }

    /**
     * Get the ordered list of league table entries for this league table.
     *
     * @return
     */
    public List<LeagueTableEntry> getTableEntries() {

    }
}
