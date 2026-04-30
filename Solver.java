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

   private Complex[] solveLinearSystem(Complex[][] A, Complex[] b) {
       
    int N = b.length;
    Complex[] lower = new Complex[N];
    Complex[] diag = new Complex[N];
    Complex[] upper = new Complex[N];

    Complex[] cPrime = new Complex[N];
    Complex[] dPrime = new Complex[N];
    Complex[] x = new Complex[N];

    // Extract tridiagonal parts
    for (int i = 0; i < N; i++) {
        diag[i] = A[i][i];

        if (i > 0) {
            lower[i] = A[i][i - 1];
        } else {
            lower[i] = new Complex(0, 0);
        }

        if (i < N - 1) {
            upper[i] = A[i][i + 1];
        } else {
            upper[i] = new Complex(0, 0);
        }
    }
    // Forward sweep
    cPrime[0] = upper[0].divide(diag[0]);
    dPrime[0] = b[0].divide(diag[0]);

    for (int i = 1; i < N; i++) {
        Complex denom = diag[i].subtract(lower[i].multiply(cPrime[i - 1]));
        cPrime[i] = upper[i].divide(denom);
        dPrime[i] = b[i].subtract(lower[i].multiply(dPrime[i - 1])).divide(denom);
    }

    // Back substitution
    x[N - 1] = dPrime[N - 1];
    for (int i = N - 2; i >= 0; i--) {
        x[i] = dPrime[i].subtract(cPrime[i].multiply(x[i + 1]));
    }
    return x;
}
}