package simulator;
// The Hamiltonian energy operator: H = T+V
// T = Kinetic ,  V = Potential

public class Hamiltonian {
    private Complex[][] H;
    private int N;

    public Hamiltonian(WaveFunction wf, Potential potential) {
        N = wf.getSize();
        double dx = wf.getDx();

        H = new Complex[N][N]; // Creates NxN Hamiltonian matrix

        double coeff = -1.0 / (2.0 * dx * dx); // Kinetic Energy Discretization
        double[] V = potential.getV();

        // Initialize All points to Zero
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                H[i][j] = new Complex(0, 0);
            }
        }
        
        // Build Tri-Diagonal Matrix
        for (int i = 1; i < N - 1; i++) {
            H[i][i] = new Complex(-2 * coeff + V[i], 0); // Main Diagonal
            H[i][i - 1] = new Complex(coeff, 0);
            H[i][i + 1] = new Complex(coeff, 0);
        }
    }

    public Complex[][] getMatrix() {
        return H;
    }
}