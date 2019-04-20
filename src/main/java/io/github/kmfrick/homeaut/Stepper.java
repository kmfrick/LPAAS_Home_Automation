package io.github.kmfrick.homeaut;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Stepper {
    private final GpioController gpio = GpioFactory.getInstance();
    // ULN2003 inputs 1, 2, 3, 4 are connected to GPIO pins 5, 6, 13 and 19
    // Under the WiringPi scheme used by Pi4J, these are pins 21, 22, 23 and 24
    private final GpioPinDigitalOutput uln1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21);
    private final GpioPinDigitalOutput uln2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22);
    private final GpioPinDigitalOutput uln3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23);
    private final GpioPinDigitalOutput uln4 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24);

    private final PinState[][] Seq;

    public Stepper() {
        Seq = new PinState[][] { 
            { PinState.HIGH, PinState.LOW,  PinState.LOW,  PinState.LOW  },
            { PinState.HIGH, PinState.HIGH, PinState.LOW,  PinState.LOW  },
            { PinState.LOW,  PinState.HIGH, PinState.LOW,  PinState.LOW  },
            { PinState.LOW,  PinState.HIGH, PinState.HIGH, PinState.LOW  },
            { PinState.LOW,  PinState.LOW,  PinState.HIGH, PinState.LOW  },
            { PinState.LOW,  PinState.LOW,  PinState.HIGH, PinState.HIGH },
            { PinState.LOW,  PinState.LOW,  PinState.LOW,  PinState.HIGH },
            { PinState.HIGH, PinState.LOW,  PinState.LOW,  PinState.HIGH } 
        };
    }

    private void setStep(PinState w1, PinState w2, PinState w3, PinState w4) {
        uln1.setState(w1);
        uln2.setState(w2);
        uln3.setState(w3);
        uln4.setState(w4);
    }

    private void forward(Integer delay, Integer steps) throws InterruptedException {
        for (var i = 0; i < steps; i++) {
            for (var j = 0; j < Seq.length; j++) {
                setStep(Seq[j][0], Seq[j][1], Seq[j][2], Seq[j][3]);
                Thread.sleep(delay);
            }
        }
        setStep(PinState.LOW, PinState.LOW, PinState.LOW, PinState.LOW);
    }

    private void backward(Integer delay, Integer steps) throws InterruptedException {
        for (var i = 0; i < steps; i++) {
            for (var j = Seq.length - 1; j >= 0; j--) {
                setStep(Seq[j][0], Seq[j][1], Seq[j][2], Seq[j][3]);
                Thread.sleep(delay);
            }
        }
        setStep(PinState.LOW, PinState.LOW, PinState.LOW, PinState.LOW);
    }

    public Boolean turnOn() {
        System.out.println("Going forward!");
        try {
            forward(5, 175);
        } catch (InterruptedException e) {
            System.err.println("cannot turn on.");
            return false;
        }
        return true;
    }

    public Boolean turnOff() {
        System.out.println("Going backward!");
        try {
            backward(5, 175);
        } catch (InterruptedException e) {
            System.err.println("cannot turn off.");
            return false;
        }
        return true;
    }
}
