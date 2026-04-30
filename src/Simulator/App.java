package simulator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application {

    private static final int WIDTH = 1100, HEIGHT = 650;

    private WaveFunction wf;
    private Potential potential;
    private Hamiltonian hamiltonian;
    private Solver solver;
    private Renderer renderer;
    private boolean paused;

    @Override
    public void start(Stage stage) {
        wf = new WaveFunction(1000, 0.05);
        potential = new Potential(1000);

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Slider omega = slider(0.01, 1, 0.05), k = slider(-20, 20, 4),
               sigma = slider(0.5, 10, 2), dt = slider(0.00001, 0.001, 0.0001),
               barrier = slider(1, 20, 5);

        ComboBox<String> type = new ComboBox<>();
        type.getItems().addAll("Harmonic", "Barrier");
        type.setValue("Harmonic");

        Label omegaL = new Label(), kL = new Label(), sigmaL = new Label(),
              dtL = new Label(), barrierL = new Label();

        Button reset = button("Reset"), pause = button("Pause");

        initialize(type.getValue(), omega.getValue(), sigma.getValue(), k.getValue(), dt.getValue(), barrier.getValue());
        renderer = new Renderer(wf, potential);

        omega.valueProperty().addListener((o, a, b) -> {
            omegaL.setText("ω = %.3f".formatted(b.doubleValue()));
            rebuild(type.getValue(), b.doubleValue(), dt.getValue(), barrier.getValue());
        });

        k.valueProperty().addListener((o, a, b) -> kL.setText("k = %.2f".formatted(b.doubleValue())));
        sigma.valueProperty().addListener((o, a, b) -> sigmaL.setText("σ = %.2f".formatted(b.doubleValue())));

        dt.valueProperty().addListener((o, a, b) -> {
            dtL.setText("Δt = %.6f".formatted(b.doubleValue()));
            solver.setDt(b.doubleValue());
        });

        barrier.valueProperty().addListener((o, a, b) -> {
            barrierL.setText("Barrier = %.2f".formatted(b.doubleValue()));
            rebuild(type.getValue(), omega.getValue(), dt.getValue(), b.doubleValue());
        });

        type.valueProperty().addListener((o, a, b) -> {
            omega.setDisable(b.equals("Barrier"));
            barrier.setDisable(b.equals("Harmonic"));
            initialize(b, omega.getValue(), sigma.getValue(), k.getValue(), dt.getValue(), barrier.getValue());
        });

        reset.setOnAction(e -> initialize(type.getValue(), omega.getValue(), sigma.getValue(), k.getValue(), dt.getValue(), barrier.getValue()));

        pause.setOnAction(e -> {
            paused = !paused;
            pause.setText(paused ? "Resume" : "Pause");
        });

        omegaL.setText("ω = %.3f".formatted(omega.getValue()));
        kL.setText("k = %.2f".formatted(k.getValue()));
        sigmaL.setText("σ = %.2f".formatted(sigma.getValue()));
        dtL.setText("Δt = %.6f".formatted(dt.getValue()));
        barrierL.setText("Barrier = %.2f".formatted(barrier.getValue()));

        VBox controls = new VBox(15,
            row(new Label("Potential"), type),
            row(omegaL, omega),
            row(kL, k),
            row(sigmaL, sigma),
            row(dtL, dt),
            row(barrierL, barrier),
            row(reset, pause)
        );

        controls.setPadding(new Insets(20));
        controls.setAlignment(Pos.CENTER);
        controls.setStyle("-fx-background-color: rgba(30,30,30,0.95); -fx-background-radius: 20;");

        StackPane root = new StackPane(canvas, controls);
        StackPane.setAlignment(controls, Pos.BOTTOM_CENTER);
        StackPane.setMargin(controls, new Insets(20));

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getRoot().setStyle("-fx-background-color:#121212; -fx-font-size:14px;");

        stage.setTitle("Schrödinger Simulator");
        stage.setScene(scene);
        stage.show();

        new AnimationTimer() {
            int frame;

            @Override
            public void handle(long now) {
                if (paused) return;
                solver.step();
                renderer.draw(gc, WIDTH, HEIGHT);

                if (++frame % 100 == 0)
                    System.out.println("P = " + wf.totalProbability()
                            + " | <x> = " + wf.expectationX()
                            + " | E = " + wf.expectationEnergy(hamiltonian));
            }
        }.start();
    }

    private Slider slider(double min, double max, double val) {
        Slider s = new Slider(min, max, val);
        s.setPrefWidth(250);
        s.setShowTickLabels(true);
        s.setShowTickMarks(true);
        return s;
    }

    private HBox row(javafx.scene.Node... nodes) {
        HBox box = new HBox(15, nodes);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private Button button(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:#2d89ef; -fx-text-fill:white; -fx-background-radius:12;");
        return b;
    }

    private void initialize(String type, double omega, double sigma, double k, double dt, double barrierHeight) {
        wf.initializeGaussian(-15, sigma, k);

        if (type.equals("Harmonic")) potential.createHarmonic(omega, wf.getX());
        else potential.createBarrier(480, 520, barrierHeight);

        hamiltonian = new Hamiltonian(wf, potential);
        solver = new Solver(hamiltonian, wf);
        solver.setDt(dt);
    }

    private void rebuild(String type, double omega, double dt, double barrierHeight) {
        if (type.equals("Harmonic")) potential.createHarmonic(omega, wf.getX());
        else potential.createBarrier(480, 520, barrierHeight);

        hamiltonian = new Hamiltonian(wf, potential);
        solver = new Solver(hamiltonian, wf);
        solver.setDt(dt);
    }

    public static void main(String[] args) {
        launch(args);
    }
}