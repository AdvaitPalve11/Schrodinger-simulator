package src.Simulator;
import javafx.scene.canvas.GraphicsContext;

public class Renderer {

    private WaveFunction waveFunction;
    private Potential potential;

    public Renderer(
            WaveFunction waveFunction,
            Potential potential
    ) {
        this.waveFunction = waveFunction;
        this.potential = potential;
    }

    public void draw(GraphicsContext gc, int width, int height) {

        // Clear screen
        gc.clearRect(0, 0, width, height);

        Complex[] psi = waveFunction.getPsi();
        double[] V = potential.getV();

        int N = waveFunction.getSize();

        // Draw wavefunction probability
        for (int i = 0; i < N - 1; i++) {

            double p1 = psi[i].magnitudeSquared();
            double p2 = psi[i + 1].magnitudeSquared();

            double x1 = (double) i / N * width;
            double x2 = (double) (i + 1) / N * width;

            double y1 = height - p1 * 300;
            double y2 = height - p2 * 300;

            gc.strokeLine(x1, y1, x2, y2);
        }

        // Draw potential barrier
        for (int i = 0; i < N; i++) {
            if (V[i] > 0) {
                double x = (double) i / N * width;
                double barrierHeight = Math.min(V[i] * 2, height * 0.5);
                gc.strokeLine(x, height, x,height - barrierHeight );
            }
        }
    }
}