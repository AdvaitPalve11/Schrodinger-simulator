public class main {
    public static void main(String[] args) {

        // Number of spatial grid points
        int N = 500;

        // Distance between points
        double dx = 0.1;

        // Create wavefunction
        waveFunction wf = new waveFunction(N, dx);

        // Initial Gaussian packet
        //
        // x0 = starting position
        // sigma = width
        // k = momentum
        wf.initializeGaussian(-15, 2, 2);

        // Create potential
        potential potential = new potential(N);

        // Create barrier in middle
        //
        // from grid 240 to 260
        // height = 5
        potential.createBarrier(240, 260, 5);

        // Build Hamiltonian
        Hamiltonian hamiltonian = new Hamiltonian(wf, potential);

        // Create solver
        solver solver = new solver(hamiltonian, wf);

        // Run simulation
        for (int t = 0; t < 1000; t++) {
            solver.step();

            // Print probability at center
            if (t % 100 == 0) {
                complex[] psi = wf.getPsi();

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