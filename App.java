import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class App extends Application {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;
    
    @Override
    public void start(Stage stage) {

        // Create simulation objects
        int N = 500;
        double dx = 0.1;

        WaveFunction wf = new WaveFunction(N, dx);
        wf.initializeGaussian(-15, 2, 8);

        Potential potential = new Potential(N);
        potential.createBarrier(245, 255, 5);

        Hamiltonian hamiltonian = new Hamiltonian(wf, potential);

        Solver solver =  new Solver(hamiltonian, wf);

        Renderer renderer = new Renderer(wf, potential);

        // Create canvas
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Group root = new Group(canvas);

        Scene scene = new Scene(root, WIDTH, HEIGHT);

        stage.setTitle("Schrödinger Simulator");
        stage.setScene(scene);
        stage.show();

        // Animation loop
     new AnimationTimer() {
        int frameCount = 0;
        @Override
        public void handle(long now) {
            frameCount++;
                if (frameCount % 100 == 0) 
                    System.out.println("Total Probability: " + wf.totalProbability() );
    
             try {
                solver.step();
                wf.normalize();
                renderer.draw(gc, WIDTH, HEIGHT);
                } catch (Exception e) {
                    e.printStackTrace();
                    stop();
                }
            }
        }.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}