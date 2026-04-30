public class solver {
    private Hamiltonian hamiltonian;
    private waveFunction waveFunction;
    private double dt = 0.001;

    public solver(Hamiltonian hamiltonian, waveFunction waveFunction) {
        this.hamiltonian = hamiltonian;
        this.waveFunction = waveFunction;
    }

    public void step() {
        complex[][] H = hamiltonian.getMatrix();
        complex[] psi = waveFunction.getPsi(); // Get Current wavefunction Ψ(t)
        int N = waveFunction.getSize();
        complex[] newPsi = new complex[N];

        for (int i = 0; i < N; i++) {
            newPsi[i] = new complex(0, 0);

            for (int j = 0; j < N; j++) {
                complex factor = new complex(0, -dt).multiply(H[i][j]); // factor = -iHΔt
              // U ≈ I - iHΔt    
                    if (i == j) {
                        // If i == j:
                        // add 1 to diagonal -> Adds I to diagonal 
                        factor = factor.add(new complex(1, 0));
                    }
                        // newPsi[i] += U[i][j] * psi[j]
                    newPsi[i] = newPsi[i].add( factor.multiply(psi[j])); // 
                }
        }
        
        for (int i = 0; i < N; i++) {
            psi[i] = newPsi[i];  // ψ(t) → ψ(t+Δt)
        }
    }

    // 𝜓(𝑡+Δ𝑡)= 𝑒^(−𝑖𝐻Δ𝑡𝜓(𝑡)) * ψ(t)
}