package io.github.kmfrick.homeaut;

// Mock Stepper class
// Allows testing the code on x86
public class MockStepper {

    public Boolean turnOn() {
        System.out.println("Going forward!");
        return true;
    }

    public Boolean turnOff() {
        System.out.println("Going backward!");
        return true;
    }
}
