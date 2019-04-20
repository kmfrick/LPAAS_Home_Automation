package io.github.kmfrick.homeaut;


public class MockStepper {

    private Boolean status;
    public void Stepper() {
        status = false;
    }

    public Boolean turnOn() {
        System.out.println("Going forward!");
        return true;
    }

    public Boolean turnOff() {
        System.out.println("Going backward!");
        return true;
    }
}
