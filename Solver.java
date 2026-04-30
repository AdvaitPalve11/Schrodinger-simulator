public class Solver {
    private Hamiltonian hamiltonian;
    private WaveFunction waveFunction;
    private double dt = 0.001;

    public Solver(Hamiltonian hamiltonian, WaveFunction waveFunction) {
        this.hamiltonian = hamiltonian;
        this.waveFunction = waveFunction;
    }

    public void step() {
        Complex[][] H = hamiltonian.getMatrix();
        Complex[] psi = waveFunction.getPsi(); // Get Current wavefunction Ψ(t)
        int N = waveFunction.getSize();
        Complex[] newPsi = new Complex[N];

        for (int i = 0; i < N; i++) {
            newPsi[i] = new Complex(0, 0);

            for (int j = 0; j < N; j++) {
                Complex factor = new Complex(0, -dt).multiply(H[i][j]); // factor = -iHΔt
              // U ≈ I - iHΔt    
                    if (i == j) {
                        // If i == j:
                        // add 1 to diagonal -> Adds I to diagonal 
                        factor = factor.add(new Complex(1, 0));
                    }
                        // newPsi[i] += U[i][j] * psi[j]
                    newPsi[i] = newPsi[i].add( factor.multiply(psi[j])); // 
                }
        }
        // 𝜓(𝑡+Δ𝑡)= 𝑒^(−𝑖𝐻Δ𝑡𝜓(𝑡)) * ψ(t)
        for (int i = 0; i < N; i++) {
            psi[i] = newPsi[i];  // ψ(t) → ψ(t+Δt)
        }

        
    }
}