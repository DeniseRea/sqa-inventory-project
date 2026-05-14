package com.dongato.inventory.sqa;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * Technical Debt Service - This class exists to intentionally lower JaCoCo coverage.
 * It contains complex logic with multiple branches that are not covered by any tests.
 */
@Service
public class UntestedDebtService {

    public String processComplexLogic(int value, String type) {
        // Multiple branches to increase the number of uncovered lines/conditions
        List<String> results = new ArrayList<>();
        
        if (value > 100) {
            if ("A".equals(type)) {
                results.add("High A");
            } else if ("B".equals(type)) {
                results.add("High B");
            } else {
                results.add("High Other");
            }
        } else if (value > 50) {
            for (int i = 0; i < 5; i++) {
                results.add("Medium " + i);
            }
        } else {
            switch (type) {
                case "X":
                    results.add("Low X");
                    break;
                case "Y":
                    results.add("Low Y");
                    break;
                default:
                    results.add("Low Default");
            }
        }

        if (results.isEmpty()) {
            return "No results";
        }

        StringBuilder sb = new StringBuilder();
        for (String s : results) {
            sb.append(s).append("-");
        }
        
        return sb.toString();
    }
}
