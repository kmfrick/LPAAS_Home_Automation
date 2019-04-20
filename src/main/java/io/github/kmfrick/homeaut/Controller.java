package io.github.kmfrick.homeaut;

import java.io.*;
import alice.tuprolog.*;
import alice.tuprolog.event.OutputEvent;
import alice.tuprolog.event.OutputListener;
import alice.tuprolog.lib.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Controller {

    private static final Prolog engine = new Prolog();
    private static final Library oolib = engine.getLibrary("alice.tuprolog.lib.OOLibrary");
    private static final StringBuilder outputSB = new StringBuilder();

    @FXML
    private Button ToggleMainBtn;
    @FXML
    private TextFlow MainTF;
    @FXML
    private CheckBox ToggleMainCB;

    public Controller() {
        try {
            // Allow outputting debug stuff to Java
            engine.addOutputListener(new OutputListener() {
                @Override
                public void onOutput(OutputEvent e) {
                    outputSB.append(e.getMsg());
                }
            });
            // Define interface predicates
            engine.setTheory(new Theory(new FileInputStream("Theory.pl")));
            // Register stepper
            var mainStepper = new MockStepper();
            ((OOLibrary) oolib).register(new Struct("mainStepper"), mainStepper);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.err.println("theory not found.");
        } catch (IOException e) {
            System.err.println("error loading theory.");
        } catch (InvalidObjectIdException e) {
            System.err.println("error registering steppers");
        } catch (InvalidTheoryException e) {
            System.err.println("invalid theory.");
        }
    }

    @FXML
    private void mainToggleState(ActionEvent ev) {
        try {
            var switchQuery = engine.solve("toggle_state(mainStepper).");
            var state = switchQuery.isSuccess();
            if (state) {
                MainTF.setStyle("-fx-background-color: green;");
                MainTF.getChildren().clear();
                var resultText = new Text(switchQuery.toString());
                MainTF.getChildren().add(resultText);
            } else {
                MainTF.setStyle("-fx-background-color: red;");
                MainTF.getChildren().clear();
                var resultText = new Text(switchQuery.toString());
                MainTF.getChildren().add(resultText);
            }
            System.out.println("mainStepper is on? " + engine.solve("on(mainStepper).").isSuccess());
        } catch (MalformedGoalException e) {
            System.err.println("error with goal" + e.getCause());
        } 
        
        System.out.println(outputSB.toString());
        outputSB.setLength(0);
    }

    @FXML
    private void mainToggleIsStateful(ActionEvent ev) {
        try {
            var stateful = ToggleMainCB.isSelected();
			if (stateful) {
                engine.solve("set_is_stateful(mainStepper, true).");
            } else {
                engine.solve("set_is_stateful(mainStepper, false).");
            }
            System.out.println("mainStepper is stateful? " + engine.solve("stateful(mainStepper).").isSuccess());
        } catch (MalformedGoalException e) {
            System.err.println("error setting status.");
        }
    }
}