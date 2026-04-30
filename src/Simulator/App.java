package simulator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application {

    private static final int WIDTH = 1000, HEIGHT = 600;

    private WaveFunction wf;
    private Potential potential;
    private Hamiltonian hamiltonian;
    private Solver solver;
    private Renderer renderer;

    private boolean paused = false;

    @Override
    public void start(Stage stage) {
        int N = 1000;
        double dx = 0.05;

        wf = new WaveFunction(N, dx);
        potential = new Potential(N);

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Slider omegaSlider = slider(0.01, 1.0, 0.05);
        Slider kSlider = slider(0, 20, 4);
        Slider sigmaSlider = slider(0.5, 10, 2);
        Slider dtSlider = slider(0.00001, 0.001, 0.0001);

        ComboBox<String> potentialBox = new ComboBox<>();
        potentialBox.getItems().addAll("Harmonic", "Barrier");
        potentialBox.setValue("Harmonic");

        Label omegaLabel = new Label();
        Label kLabel = new Label();
        Label sigmaLabel = new Label();
        Label dtLabel = new Label();

        Button resetBtn = new Button("Reset");
        Button pauseBtn = new Button("Pause");

        initialize(
                potentialBox.getValue(),
                omegaSlider.getValue(),
                sigmaSlider.getValue(),
                kSlider.getValue(),
                dtSlider.getValue());

        renderer = new Renderer(wf, potential);

        omegaSlider.valueProperty().addListener((obs, o, n) -> {
            omegaLabel.setText("ω = %.3f".formatted(n.doubleValue()));
            rebuild(potentialBox.getValue(), n.doubleValue(), dtSlider.getValue());
        });

        kSlider.valueProperty().addListener((obs, o, n) -> {
            kLabel.setText("k = %.2f".formatted(n.doubleValue()));
            initialize(potentialBox.getValue(), omegaSlider.getValue(), sigmaSlider.getValue(), n.doubleValue(),
                    dtSlider.getValue());
        });

        sigmaSlider.valueProperty().addListener((obs, o, n) -> {
            sigmaLabel.setText("σ = %.2f".formatted(n.doubleValue()));
            initialize(potentialBox.getValue(), omegaSlider.getValue(), n.doubleValue(), kSlider.getValue(),
                    dtSlider.getValue());
        });

        dtSlider.valueProperty().addListener((obs, o, n) -> {
            dtLabel.setText("Δt = %.6f".formatted(n.doubleValue()));
            solver.setDt(n.doubleValue());
        });

        potentialBox.valueProperty().addListener((obs, o, n) -> initialize(
                n,
                omegaSlider.getValue(),
                sigmaSlider.getValue(),
                kSlider.getValue(),
                dtSlider.getValue()));

        resetBtn.setOnAction(e -> initialize(
                potentialBox.getValue(),
                omegaSlider.getValue(),
                sigmaSlider.getValue(),
                kSlider.getValue(),
                dtSlider.getValue()));

        pauseBtn.setOnAction(e -> {
            paused = !paused;
            pauseBtn.setText(paused ? "Resume" : "Pause");
        });

        omegaLabel.setText("ω = %.3f".formatted(omegaSlider.getValue()));
        kLabel.setText("k = %.2f".formatted(kSlider.getValue()));
        sigmaLabel.setText("σ = %.2f".formatted(sigmaSlider.getValue()));
        dtLabel.setText("Δt = %.6f".formatted(dtSlider.getValue()));

        VBox root = new VBox(
                canvas,
                row(new Label("Potential"), potentialBox),
                row(omegaLabel, omegaSlider),
                row(kLabel, kSlider),
                row(sigmaLabel, sigmaSlider),
                row(dtLabel, dtSlider),
                row(resetBtn, pauseBtn));

        Scene scene = new Scene(root, WIDTH, HEIGHT + 260);

        stage.setTitle("Schrödinger Simulator");
        stage.setScene(scene);
        stage.show();

        new AnimationTimer() {
            int frame = 0;

            @Override
            public void handle(long now) {
                if (paused)
                    return;

                solver.step();
                renderer.draw(gc, WIDTH, HEIGHT);

                if (++frame % 100 == 0)
                    System.out.println(
                            "P = " + wf.totalProbability() +
                                    " | <x> = " + wf.expectationX() +
                                    " | E = " + wf.expectationEnergy(hamiltonian));
            }
        }.start();
    }

    private Slider slider(double min, double max, double value) {
        Slider s = new Slider(min, max, value);
        s.setShowTickMarks(true);
        s.setShowTickLabels(true);
        return s;
    }

    private HBox row(javafx.scene.Node... nodes) {
        HBox box = new HBox(10, nodes);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private void initialize(String type, double omega, double sigma, double k, double dt) {
        wf.initializeGaussian(-15, sigma, k);

        if (type.equals("Harmonic"))
            potential.createHarmonic(omega, wf.getX());
        else
            potential.createBarrier(480, 520, 5);

        hamiltonian = new Hamiltonian(wf, potential);
        solver = new Solver(hamiltonian, wf);
        solver.setDt(dt);
    }

    private void rebuild(String type, double omega, double dt) {
        if (type.equals("Harmonic"))
            potential.createHarmonic(omega, wf.getX());
        else
            potential.createBarrier(480, 520, 5);

        hamiltonian = new Hamiltonian(wf, potential);
        solver = new Solver(hamiltonian, wf);
        solver.setDt(dt);
    }

    public static void main(String[] args) {
        launch(args);
    }
}