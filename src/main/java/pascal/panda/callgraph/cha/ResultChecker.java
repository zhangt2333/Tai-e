/*
 * Panda - A Program Analysis Framework for Java
 *
 * Copyright (C) 2020 Tian Tan <tiantan@nju.edu.cn>
 * Copyright (C) 2020 Yue Li <yueli@nju.edu.cn>
 * All rights reserved.
 *
 * This software is designed for the "Static Program Analysis" course at
 * Nanjing University, and it supports a subset of Java features.
 * Panda is only for educational and academic purposes, and any form of
 * commercial use is disallowed.
 */

package pascal.panda.callgraph.cha;

import pascal.panda.callgraph.CallGraph;
import pascal.panda.util.SootUtils;
import pascal.panda.util.CollectionUtils;
import soot.Body;
import soot.BriefUnitPrinter;
import soot.G;
import soot.SootMethod;
import soot.Unit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Boosts class hierarchy analysis and checks whether the analysis result
 * is correct by comparing it with prepared expected result.
 */
public class ResultChecker {

    // ---------- static members ----------
    /**
     * The current result checker
     */
    private static ResultChecker checker;
    private final Set<String> mismatches = new TreeSet<>();
    // ---------- instance members ----------
    private Map<String, Map<String, String>> expectedResults;

    ResultChecker(Path filePath) {
        readExpectedResult(filePath);
    }

    private static void setChecker(ResultChecker checker) {
        ResultChecker.checker = checker;
    }

    public static boolean isAvailable() {
        return checker != null;
    }

    public static ResultChecker get() {
        return checker;
    }

    /**
     * The entry function of whole checking mechanism.
     *
     * @param args the arguments for running Soot
     * @param path the path string of the expected result file
     * @return the mismatched information in form of set of strings
     */
    public static Set<String> check(String[] args, String path) {
        ResultChecker checker = new ResultChecker(Paths.get(path));
        setChecker(checker);
        CallGraphPrinter.setOutput(false);

        G.reset(); // reset the whole Soot environment
        Main.main(args);
        return checker.getMismatches();
    }

    public Set<String> getMismatches() {
        return mismatches;
    }

    public void compare(Body body, CallGraph<Unit, SootMethod> callGraph) {
        String method = body.getMethod().getSignature();
        Map<String, String> expectedCallEdges =
                expectedResults.getOrDefault(method, Collections.emptyMap());
        BriefUnitPrinter up = new BriefUnitPrinter(body);
        body.getUnits().forEach(u -> {
            String callUnit = SootUtils.unitToString(up, u);
            String expected = expectedCallEdges.get(callUnit);
            Collection<SootMethod> callees = callGraph.getCallees(u);
            String given = CallGraphPrinter.v().calleesToString(callees);
            if (expected != null) {
                if (!expected.equals(given)) {
                    mismatches.add(String.format(
                            "\nCallees of %s, expected: %s, given: %s",
                            callUnit, expected, given));
                }
            } else if (!callGraph.getCallees(u).isEmpty()) {
                mismatches.add(String.format(
                        "\nCallees of %s, expected: [], given: %s",
                        callUnit, given));
            }
        });
    }

    /**
     * Reads expected result from given file path.
     */
    private void readExpectedResult(Path filePath) {
        expectedResults = new TreeMap<>();
        String currentMethod = null; // method signature
        try {
            for (String line : Files.readAllLines(filePath)) {
                if (isMethodSignature(line)) {
                    currentMethod = line;
                } else if (!isEmpty(line)) {
                    String[] splits = line.split(" -> ");
                    String callUnit = splits[0];
                    String callees = splits[1];
                    CollectionUtils.addToMapMap(expectedResults, currentMethod, callUnit, callees);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read " + filePath
                    + " caused by " + e);
        }
    }

    private boolean isMethodSignature(String line) {
        return line.startsWith("<");
    }

    private boolean isEmpty(String line) {
        return line.trim().equals("");
    }
}