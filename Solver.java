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
        Complex[] psi = waveFunction.getPsi();
        int N = waveFunction.getSize();

        Complex[][] A = new Complex[N][N];
        Complex[][] B = new Complex[N][N];
        Complex[] rhs = new Complex[N];

        // Build A and B
        for (int i = 0; i < N; i++) {
            rhs[i] = new Complex(0, 0);

            for (int j = 0; j < N; j++) {
                Complex term = H[i][j].scale(dt / 2.0);
                Complex imagTerm =new Complex(-term.im, term.re);

                A[i][j] = imagTerm;
                B[i][j] = imagTerm.scale(-1);

                if (i == j) {
                    A[i][j] = A[i][j].add(new Complex(1, 0));
                    B[i][j] = B[i][j].add(new Complex(1, 0));
                }
            }
        }

        // Compute rhs = Bψ
        for (int i = 0; i < N; i++) {
            rhs[i] = new Complex(0, 0);

            for (int j = 0; j < N; j++) {
                rhs[i] = rhs[i].add(B[i][j].multiply(psi[j]));
            }
        }

        // Solve Aψ_new = rhs
        Complex[] newPsi = solveLinearSystem(A, rhs);

        // Update ψ
        for (int i = 0; i < N; i++) {
            psi[i] = newPsi[i];
        }
    }

   private Complex[] solveLinearSystem(
        Complex[][] A,
        Complex[] b
) {
    int N = b.length;

    Complex[][] M = new Complex[N][N];
    Complex[] x = new Complex[N];
    Complex[] rhs = new Complex[N];

    // Copy matrix and rhs
    for (int i = 0; i < N; i++) {
        rhs[i] = b[i];

        for (int j = 0; j < N; j++) {
            M[i][j] = A[i][j];
        }
    }

    // Forward elimination
    for (int k = 0; k < N - 1; k++) {

        for (int i = k + 1; i < N; i++) {

            Complex factor =
                    M[i][k].divide(M[k][k]);

            for (int j = k; j < N; j++) {
                M[i][j] =
                        M[i][j].subtract(
                                factor.multiply(M[k][j])
                        );
            }

            rhs[i] =
                    rhs[i].subtract(
                            factor.multiply(rhs[k])
                    );
        }
    }

    // Back substitution
    for (int i = N - 1; i >= 0; i--) {
        x[i] = rhs[i];

        for (int j = i + 1; j < N; j++) {
            x[i] =
                    x[i].subtract(
                            M[i][j].multiply(x[j])
                    );
        }

        x[i] = x[i].divide(M[i][i]);
    }

    return x;
}
}