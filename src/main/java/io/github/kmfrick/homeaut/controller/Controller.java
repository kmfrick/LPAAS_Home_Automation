package io.github.kmfrick.homeaut.controller;

import alice.tuprolog.*;
import java.io.*;
import io.github.kmfrick.homeaut.model.Stepper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller {

    private static final Prolog engine = new Prolog();
    private static final String stepperClass = Stepper.class.getName();
    private static final String fileName = "/home/kmfrick/Documents/Code/IdeaProjects/homeaut/target/classes/io/github/kmfrick/homeaut/controller/Controller.pl";
    @FXML
    private static Button ToggleMainBtn;

    public void solve() {
        String goal = "";
        try (BufferedReader pl = new BufferedReader(new FileReader(fileName))) {
            while ((goal = pl.readLine()) != null) {
                if (goal.trim().isEmpty()) continue;
                System.out.println("?- " + goal);
                // interpreter main loop
                SolveInfo info = engine.solve(goal);
                if (engine.isHalted())
                    break;
                else if (!info.isSuccess())
                    System.out.println("no.");
                else if (!engine.hasOpenAlternatives()) {
                    System.out.println(info);
                } else { // main case
                    while (engine.hasOpenAlternatives()) {
                        info = engine.solveNext();
                        if (!info.isSuccess()) {
                            System.out.println("no.");
                            break;
                        } else {
                            System.out.println(info + " ?");           
                        } // end if
                    } // end while
                    if (!engine.hasOpenAlternatives())
                        System.out.println("no.");
                } // end main case
            } // end main loop
        } catch (MalformedGoalException ex) {
            System.err.println("syntax error.");
        } catch (IOException e) {
            System.err.println("error reading prolog file.");
        } catch (NoMoreSolutionException e) {
            System.out.println("no more solutions.");
        } catch (NullPointerException e) {
            System.out.println("NPE caused by goal " + goal);
        } // end try
    }
}