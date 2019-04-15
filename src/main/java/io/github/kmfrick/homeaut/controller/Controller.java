package io.github.kmfrick.homeaut.controller;

import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;

public class Controller {

    public static void solve() {
        var engine = new Prolog();
        var controllerClass = "io.github.kmfrick.homeaut.controller.Controller";
        var controllerArgs = "\"Hi!\"";
        try {
            var info = engine.solve(" class('" + controllerClass + "') <- salute(" + controllerArgs + "). ");
            if (engine.isHalted())
                return;
            else if (!info.isSuccess())
                System.out.println("no.");
            else if (!engine.hasOpenAlternatives()) {
                System.out.println(info);
            } else { // main case
                System.out.println(info + " ?");
                while (engine.hasOpenAlternatives()) {
                    info = engine.solveNext();
                    if (!info.isSuccess()) {
                        System.out.println("no.");
                        break;
                    } else {
                        System.out.println(info + " ?");
                    } // endif
                } // endwhile
                if (!engine.hasOpenAlternatives())
                    System.out.println("no.");
            } // end main case
        } catch (MalformedGoalException ex) {
            System.err.println("syntax error.");
        } catch (NoMoreSolutionException e) {
            System.out.println("no more solutions.");
        }
    }// end try

    
    public static Boolean salute(String i) {
        System.out.println("I salute you! " + i);
        return true;
    }
}