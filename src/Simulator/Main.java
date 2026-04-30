package src.Simulator;

public class Main {
    public static void main(String[] args) {

        // Number of spatial grid points
        int N = 500;

        // Distance between points
        double dx = 0.1;

        // Create wavefunction
        WaveFunction wf = new WaveFunction(N, dx);

        // Initial Gaussian packet
        // x0 = starting position
        // sigma = width
        // k = momentum
        wf.initializeGaussian(-15, 2, 8);

        // Create potential
        Potential potential = new Potential(N);

        // Create barrier in middle
        // from grid 240 to 260
        // height = 5
        potential.createBarrier(245, 255, 5);

        // Build Hamiltonian
        Hamiltonian hamiltonian = new Hamiltonian(wf, potential);

        // Create solver
        Solver solver = new Solver(hamiltonian, wf);

        // Run simulation
        for (int t = 0; t < 5000; t++) {
            solver.step();
            wf.normalize();

            // Print probability at center
            if (t % 100 == 0) {
                Complex[] psi = wf.getPsi();

                System.out.println(
                        "Step " + t +
                        " Probability: " +
                        psi[250].magnitudeSquared()
                );
            }
        }
        System.out.println("Total Probability: " + wf.totalProbability());
    }
}