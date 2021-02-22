/*
* File: SimulationGUI.java
* Author: John Kucera
* Date: December 4, 2020
* Purpose: This Java program constructs a GUI that holds a Traffic Simulation.
* When start button is pressed, 3 cars and 3 traffic lights are created as Threads
* and start running. When the cars run, they travel in a straight line at random
* speed and their position gets updated live in the GUI. When the lights run,
* they change colors at fixed intervals. Cars will stop at red lights. There
* are also buttons to add cars and add lights, but 5 of each are the limit.
* There are buttons stop, pause, and continue which the user can press that
* provide that function for the simulation. Finally, there is a timestamp
* updated every second.
* 
* Inner classes: TimeStamp, TrafficLight, and Car implement runnable and have 
* run() methods. TimeStamp instance is used as a thread that updates the timestamp in
* the GUI. TrafficLight instances are used as threads that generate traffic lights
* in the simulation with changing light colors. Car instances are used as 
* threads that generate cars in the simulation that move forward and have their
* locations updated in the GUI. DrawVisual inner class extends JComponent and
* provides the code for displaying the visual of the traffic simulation
* information.
*/

// import necessary java classes
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

// SimulationGUI Class
public class SimulationGUI extends JFrame {
    
    // Variable Initialization: Traffic Lights,Cars,TimeStamp,some Buttons,Visual
    private int lightID = 0;
    private int lightX = 1000;
    private boolean lightFinishSleep = false;
    private long timeAtPause = 0L;
    private long timeAtContinue = 0L;
    private final TrafficLight[] lightsArr = new TrafficLight[5];
    private int carID = 0;
    private final Car[] carsArr = new Car[5];
    private final JLabel ts = new JLabel("00:00:00 TZ");
    private boolean stop = false;
    private boolean pause = false;
    private final DrawVisual drawVisual = new DrawVisual();
    
    // Global Lock
    private final Object lock = new Object();
    
    // Table: Traffic Lights
    private final String[][] dataSkeleton = null;
    private final String[] lightsColumns = {"Light #", "(X,Y) Meters", "Color"};
    private final DefaultTableModel lightsModel = new DefaultTableModel(dataSkeleton, lightsColumns);
    
    // Table: Cars
    private final String[] carsColumns = {"Car #", "(X,Y) Meters", "Speed (KPH)"};
    private final DefaultTableModel carsModel = new DefaultTableModel(dataSkeleton, carsColumns);

    // GUI Constructor
    public SimulationGUI() {
        // Panel: Control (Start/Start/Pause/Continue)
        final JButton startBtn = new JButton("Start"); // Buttons+Panel creation
        final JButton stopBtn = new JButton("Stop");
        final JButton continueBtn = new JButton("Continue");
        final JButton pauseBtn = new JButton("Pause");
        final JPanel controlPanel = new JPanel();
        stopBtn.setEnabled(false); // Turn off other buttons until start
        pauseBtn.setEnabled(false);
        continueBtn.setEnabled(false);
        controlPanel.add(startBtn); // Add buttons to panel
        controlPanel.add(stopBtn);
        controlPanel.add(pauseBtn);
        controlPanel.add(continueBtn);
        
        // Panel: Timestamp 
        final JLabel tsLbl = new JLabel("Current Time: ");
        final JPanel tsPanel = new JPanel();
        tsPanel.add(tsLbl); // Add Labels to panel
        tsPanel.add(ts);
        
        // Panel: Add cars/lights
        final JButton addLightBtn = new JButton("Add Traffic Light"); 
        final JButton addCarBtn = new JButton("Add Car"); // Panel+Button creation
        final JPanel addPanel = new JPanel();
        addLightBtn.setEnabled(false); // Turn off buttons until start
        addCarBtn.setEnabled(false);
        addPanel.add(addLightBtn); // Add buttons to panel
        addPanel.add(addCarBtn);
        
        // Traffic Lights data, component creation
        final JTable lightsTbl = new JTable(lightsModel);
        lightsTbl.setPreferredScrollableViewportSize(lightsTbl.getPreferredSize());
        final JScrollPane lightsScroll = new JScrollPane(lightsTbl);
        
        // Cars data, component creation, put into panel
        final JTable carsTbl = new JTable(carsModel);
        carsTbl.setPreferredScrollableViewportSize(carsTbl.getPreferredSize());
        final JScrollPane carsScroll = new JScrollPane(carsTbl);
        
        // Panel: Combine lights and cars into one panel
        final JPanel dataPanel = new JPanel(new BorderLayout());
        dataPanel.add(lightsScroll, BorderLayout.WEST);
        dataPanel.add(carsScroll, BorderLayout.CENTER);
        
        // Creating Constraints for GridBag
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2,2,2,2);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Panel: Put all buttons together, use GridBagLayout
        final JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout());
        buttonsPanel.add(controlPanel, gbc);
        gbc.gridy = 1;
        buttonsPanel.add(tsPanel, gbc);
        gbc.gridy = 2;
        buttonsPanel.add(addPanel, gbc);
        
        // Frame: Add all components to main frame
        setLayout(new BorderLayout());
        add(buttonsPanel, BorderLayout.PAGE_START);
        add(dataPanel, BorderLayout.CENTER);
        
        // Frame characteristics
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Traffic Simulation");
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(485,252);
        
        // Thread: TimeStamp
        final TimeStamp time = new TimeStamp();
        final Thread timeThread = new Thread(time);
        
        // Create visual frame
        final JFrame visualFrame = new JFrame();
        
        // Listener: Start Button
        startBtn.addActionListener((ActionEvent e) -> {
            // Thread: Time, start
            timeThread.start();
            
            // Traffic Lights + Cars: Add 3
            for (int i = 0; i < 3; i++) {
                // Traffic Lights
                lightsArr[lightID] = new TrafficLight(lightID+1, lightX);
                addTrafficLight(lightsArr[lightID]);

                // Cars
                carsArr[carID] = new Car(carID+1);
                addCar(carsArr[carID]);
            } // end of for
            
            // Buttons: Disable start, enable the others
            startBtn.setEnabled(false);
            stopBtn.setEnabled(true);
            pauseBtn.setEnabled(true);
            addLightBtn.setEnabled(true);
            addCarBtn.setEnabled(true);
            
            // Visual Frame: characteristics, add graphic to frame
            visualFrame.add(drawVisual);
            visualFrame.setVisible(true);
            visualFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            visualFrame.setTitle("Traffic Simulation - Visual");
            visualFrame.setLocationRelativeTo(null);
            visualFrame.setResizable(false);
            visualFrame.setSize(1250, 290);
            toFront(); // control frame to front
        }); // end of listener

        // Listener: Stop Button
        stopBtn.addActionListener((ActionEvent e) -> {
            // Threads all exit out of run() method and get terminated
            stop = true;
            
            // Buttons: Disable all
            stopBtn.setEnabled(false);
            pauseBtn.setEnabled(false);
            continueBtn.setEnabled(false);
            addLightBtn.setEnabled(false);
            addCarBtn.setEnabled(false);
        }); // end of listener

        // Listener: Pause Button
        pauseBtn.addActionListener((ActionEvent e) -> {
            // Threads: all call wait()
            pause = true;
            
            // Store time at pause for each traffic light
            storePauseTime();
            
            // Buttons: Disable all except continue
            pauseBtn.setEnabled(false);
            continueBtn.setEnabled(true);
            addLightBtn.setEnabled(false);
            addCarBtn.setEnabled(false);
        }); // end of listener
        
        // Listener: Continue Button
        continueBtn.addActionListener((ActionEvent e) -> {
            // Tell traffic lights that they need to finish their sleep
            // before changing colors
            lightFinishSleep = true;
            
            // Threads: all get notify() and wake up
            synchronized (lock) {
                lock.notifyAll();
            } // end of block
            
            // Threads continue run(), store time at continue
            pause = false;
            storeContTime();
            
            // Buttons: enable pause, disable continue
            pauseBtn.setEnabled(true);
            continueBtn.setEnabled(false);
            // Only enable addLight/addCar buttons if max is not reached
            if (lightID < 5) {
                addLightBtn.setEnabled(true);
            } // end of if
            if (carID < 5) {
                addCarBtn.setEnabled(true);
            } // end of if
        }); // end of listener

        // Listener: Add Light button
        addLightBtn.addActionListener((ActionEvent e) -> {
            // Traffic Light + Thread: Create new light
            lightsArr[lightID] = new TrafficLight(lightID+1, lightX);
            addTrafficLight(lightsArr[lightID]);
            
            // Button: Disable add light
            if (lightID >= 5) {
                addLightBtn.setEnabled(false);
                addLightBtn.setText("Max Traffic Lights Reached");
            } // end of if
        }); // end of listener

        // Listener: Add Car button
        addCarBtn.addActionListener((ActionEvent e) -> {
            // Car + Thread: Create new car
            carsArr[carID] = new Car(carID+1);
            addCar(carsArr[carID]);
            
            // Button: Disable add car
            if (carID >= 5) {
                addCarBtn.setEnabled(false);
                addCarBtn.setText("Max Cars Reached");
            } // end of if
        }); // end of listener
    } // end of constructor
         
    // Method: Store time at pause
    private void storePauseTime() {
        Date datePause = new Date();
        timeAtPause = datePause.getTime();
    } // end of method
    
    // Method: Store time at continue
    private void storeContTime() {
        Date continueDate = new Date();
        timeAtContinue = continueDate.getTime();
    } // end of method

    // Method: Create Thread for Traffic Light, run it
    private void addTrafficLight(TrafficLight trafficLight){
        Thread lightThread = new Thread(trafficLight);
        lightThread.start();
        lightX += 1000; // add 1000m to distance
        lightID++;
    } // end of method
    
    // Method: Create Thread for Car, run it
    private void addCar(Car car){
        Thread carThread = new Thread(car);
        carThread.start();
        carID++;
    } // end of method
        
    // Inner Class: TimeStamp, contains run() for the thread
    private class TimeStamp implements Runnable {
        // Variable: Initialize time format
        private final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a z");
        
        // Constructor
        public TimeStamp() {
            
        } // end of constructor
        
        // Method: run()
        @Override
        public synchronized void run() {
            while(!stop) { // not stopped
                // invokeLater to edit GUI: Timestamp String update
                SwingUtilities.invokeLater(() -> {
                    ts.setText(new String(timeFormat.format(new Date())));
                }); // end of invokeLater
                
                // Update time after each second
                try {
                    Thread.sleep(1000);
                } // end of try
                catch (InterruptedException ex) {
                    System.out.println("Interrupted Exception: TimeStamp sleep");
                } // end of catch
                
                // When paused: call wait()
                if (pause) {
                    synchronized (lock) {
                        try {
                            lock.wait();
                        } // end of try
                        catch (InterruptedException ex) {
                            System.out.println("Interrupted Exception: TimeStamp wait");
                        } // end of catch
                    } // end of synchronized
                } // end of if
            } // end of while
        } // end of run()
    } // end of inner class
    
    // Inner Class: TrafficLight, implements Runnable to run threads
    private class TrafficLight implements Runnable {
        // Variable Initialization
        private long startSleep = 0L;
        private final long greenSleep = 10000L;
        private final long yellowSleep = 2000L;
        private final long redSleep = 8000L;
        private double lightX = 0.0; // meters
        private final int lightY = 0; // meters
        private String coordinate;
        private String color = null;
        private int lightRow = 0;
        private boolean lightWaited = false;
        
        // Constructor
        TrafficLight(int lightNum, int lightX) {
            this.lightX = lightX;
            coordinate = "(" + lightX + ", " + lightY + ")";
            color = "RED";
            // invokeLater to edit GUI: Table update
            SwingUtilities.invokeLater(() -> {
                lightsModel.addRow(new Object[] {lightNum, coordinate, color});
                lightRow = lightsModel.getRowCount()-1;
            }); // end of invokeLater
        } // end of constructor
        
        // Method: run()
        @Override
        public synchronized void run() {
            while (!stop) { // not stopped
                // Change color, invokeLater to update GUI table and visual
                changeColor();
                SwingUtilities.invokeLater(() -> {
                    lightsModel.setValueAt(color, lightRow, 2);
                    drawVisual.repaint();
                }); // end of invokeLater
                
                // 2nd timer to store sleep time
                Date startDate = new Date();
                startSleep = startDate.getTime();
                    
                // Sleep at color for specified time
                try {
                    if ("GREEN".equals(color)) { // Green light
                        Thread.sleep(greenSleep);
                    } // end of if
                    else if ("YELLOW".equals(color)) { // yellow light
                        Thread.sleep(yellowSleep);
                    } // end of else if
                    else if ("RED".equals(color)) { // red light
                        Thread.sleep(redSleep);
                    } // end of else if
                } // end of try
                catch (InterruptedException e) {
                    System.out.println("Interrupted Exception: TrafficLight sleep");
                } // end of catch
                
                // When paused: call wait()
                while (pause) { 
                    synchronized (lock) {
                        try { 
                            lock.wait();
                            lightWaited = true; // confirm that wait() has been called
                        } // end of try
                        catch (InterruptedException ex) {
                            System.out.println("Interrupted Exception: TrafficLight wait");
                        } // end of catch
                    } // end of synchronized
                    
                    // Sleep the light for the remaining time after continue
                    try {
                        if ("GREEN".equals(color)) { // Green light
                            Thread.sleep(Math.abs(greenSleep - (timeAtPause - startSleep)));
                        } // end of if
                        else if ("YELLOW".equals(color)) { // Yellow light
                            Thread.sleep(Math.abs(yellowSleep - (timeAtPause - startSleep)));
                        } // end of else if
                        else if ("RED".equals(color)) { // Red light
                            Thread.sleep(Math.abs(redSleep - (timeAtPause - startSleep)));
                        } // end of else if
                    } // end of try
                    catch (InterruptedException e) {
                        System.out.println("Interrupted Exception: TrafficLight sleep2");
                    } // end of catch
                } // end of if
                
                // If paused but continued before 1st sleep finished,
                // sleep the light for the remaining time
                if (lightFinishSleep && !lightWaited) {
                    try {
                        Thread.sleep(timeAtContinue - timeAtPause);
                    } // end of try
                    catch (InterruptedException e) {
                        System.out.println("Interrupted Exception: TrafficLight sleep3");
                    } // end of catch
                    lightWaited = false;
                    lightFinishSleep = false;
                } // end of if
            } // end of while    
        } // end of run()
        
        // Method: change traffic light color
        private synchronized void changeColor() {
            if ("GREEN".equals(color)) {
                color = "YELLOW";
            } // end of if
            else if ("YELLOW".equals(color)) {
                color = "RED";
            } // end of else if
            else if ("RED".equals(color)) {
                color = "GREEN";
            } // end of else if
        } // end of method
        
        // Method: get light x coordinate
        public double getLightX() {
            return lightX;
        } // end of method
        
        // Method: get color as string
        public String getColorString() {
            return color;
        } // end of method
        
        // Method: get color as Color
        public Color getColor() {
            if ("GREEN".equals(color)) {
                return Color.GREEN;
            } // end of if
            else if ("YELLOW".equals(color)) {
                return Color.YELLOW;
            } // end of else if
            else {
                return Color.RED;
            } // end of else
        } // end of method
        
        
    } // end of inner class

    // Inner class: Car, implements Runnable to run threads
    private class Car implements Runnable {
        // Variable Initialization
        private double carX = 0.0; // meters
        private final int carY = 0; // meters
        private String coordinate = null; 
        private final double initialSpeed; // km/hr
        private double currentSpeed; // km/hr
        private final Random r = new Random();
        private final DecimalFormat df = new DecimalFormat("#");
        private int carRow = 0;
        
        // Constructor
        Car(int carNum) {
            coordinate = "(" + carX + ", " + carY + ")";
            initialSpeed = r.nextInt(100) + 1;
            currentSpeed = initialSpeed;
            // invokeLater to edit GUI: Table update
            SwingUtilities.invokeLater(() -> {
                carsModel.addRow(new Object[] {
                    carNum, coordinate, Double.toString(currentSpeed)});
                carRow = carsModel.getRowCount() - 1;
            }); // end of invokeLater
        } // end of constructor
        
        // Method: run()
        @Override
        public synchronized void run() {
            while (!stop) { // not stopped
                // Update coordinate
                coordinate = "(" + df.format(carX) + ", " + carY + ")";
                
                // invokeLater to edit GUI: Table update for coordinate and visual
                SwingUtilities.invokeLater(() -> {
                    carsModel.setValueAt(coordinate, carRow, 1);
                    drawVisual.repaint();
                }); // end of invokeLater
                
                // Sleep for 10 milliseconds, then increment distance
                try {
                    Thread.sleep(10);
                        // Convert: 1Km per 1Hour = 1m per 3600milliseconds
                        // = 1/360 m per 10 milliseconds
                        // Distance = speed * time
                        // Distance traveled in 10 MS = (speed * 1 MS) / 360
                    carX += (currentSpeed/360);
                } // end of try
                catch (InterruptedException e) {
                        System.out.println("Interrupted Exception: Car sleep");
                } // end of catch
                
                // STOPPING AT RED LIGHTS:
                for (int i = 0; i < lightID; i++) { // for all lights
                    if ((lightsArr[i].getLightX() - 1 ) < carX &&
                            carX < lightsArr[i].getLightX()) { // check if car is at light
                        if ("RED".equals(lightsArr[i].getColorString())) { // light is red
                            // set speed to 0
                            currentSpeed = 0.0;
                        } // end of inner if

                        // light is not red
                        else if ("YELLOW".equals(lightsArr[i].getColorString()) ||
                                "GREEN".equals(lightsArr[i].getColorString())) {
                            // continue driving
                            currentSpeed = initialSpeed;    
                        } // end of else if
                        
                        // invokeLater to edit GUI: Table update for speed
                        SwingUtilities.invokeLater(() -> {
                            carsModel.setValueAt(currentSpeed, carRow, 2);
                        }); // end of invokeLater
                    } // end of if
                } // end of for
                
                // When paused: call wait()
                if (pause) {
                    synchronized (lock) {
                        try {
                            lock.wait();
                        } // end of try
                        catch (InterruptedException ex) {
                            System.out.println("Interrupted Exception: Car wait");
                        } // end of catch
                    } // end of synchronized
                } // end of if
                
                // If car goes past 5000m, loop back to beginning
                if (carX >= 5000) {
                    carX = 0;
                } // end of if
            } // end of while
        } // end of run()
        
        // Method: Get car x position in pixels
        public int getCarXPx() {
                // Convert: Ratio of XCoordinate on 5000m road to 1150 pixel line
            return (int)((carX/5000)*1150);
        } // end of method
    } // end of inner class
    
    // Inner class: DrawVisual, draws the visual display of traffic
    private class DrawVisual extends JComponent {
        // Constructor
        DrawVisual() {

        } // end of constructor
        
        // Method: paintComponent to use Graphics
        @Override
        protected void paintComponent (Graphics g) {
            super.paintComponents(g);
            // Draw Road lines
            for (int roadDist = 0; roadDist <= 175; roadDist += 35) {
                g.fillRect(40,30+roadDist,1150,2);
            } // end of for

            // Draw Car Labels
            int carLblDist = 0;
            for (int i = 0; i < 5; i++) {
                g.drawString("Car " + String.valueOf(i+1), 10, 50+carLblDist);
                carLblDist += 35;
            } // end of for

            // Draw Lights + Light labels
            int lightDist = 223;
            for (int i = 0; i < lightID; i++) {
                g.setColor(Color.BLACK);
                g.drawString("Light " + String.valueOf(i+1), 40+lightDist, 240);
                g.setColor(lightsArr[i].getColor());
                g.fillOval(50+lightDist, 215, 10, 10);
                lightDist += 230;
            } // end of for

            // Draw Cars
            int carDist = 0;
            for (int i = 0; i < carID; i++) {
                g.setColor(Color.BLUE);
                g.fillRect(40+carsArr[i].getCarXPx(), 40+carDist, 5, 15);
                carDist += 35;
            } // end of for
        } // end of method
    } // end of inner class
    
    // Method: main
    public static void main(String[] args) {
        SimulationGUI gui = new SimulationGUI();
    } // end of main method
} // end of class

