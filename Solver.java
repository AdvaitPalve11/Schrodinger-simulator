public class Solver {

    private Hamiltonian hamiltonian;
    private WaveFunction waveFunction;
    private double dt = 0.0001;

    private Complex[][] A;
    private Complex[][] B;

    public Solver(Hamiltonian hamiltonian, WaveFunction waveFunction) {
        this.hamiltonian = hamiltonian;
        this.waveFunction = waveFunction;

        int N = waveFunction.getSize();
        Complex[][] H = hamiltonian.getMatrix();

        A = new Complex[N][N];
        B = new Complex[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Complex term = H[i][j].scale(dt / 2.0);
                Complex imagTerm = new Complex(0, 1).multiply(term);

                A[i][j] = imagTerm;
                B[i][j] = imagTerm.scale(-1);

                if (i == j) {
                    A[i][j] = A[i][j].add(new Complex(1, 0));
                    B[i][j] = B[i][j].add(new Complex(1, 0));
                }
            }
        }
    }

    public void step() {
        Complex[] psi = waveFunction.getPsi();
        int N = waveFunction.getSize();
        Complex[] rhs = new Complex[N];

        for (int i = 0; i < N; i++) {
            rhs[i] = new Complex(0, 0);
            for (int j = 0; j < N; j++) {
                rhs[i] = rhs[i].add(B[i][j].multiply(psi[j]));
            }
        }
    
        Complex[] newPsi = solveLinearSystem(A, rhs);

        for (int i = 0; i < N; i++){
            psi[i] = newPsi[i];
        }
        psi[0] = new Complex(0, 0);
        psi[N - 1] = new Complex(0, 0);
    }

    private Complex[] solveLinearSystem(Complex[][] A, Complex[] b) {
        int N = b.length;

        Complex[] lower = new Complex[N];
        Complex[] diag = new Complex[N];
        Complex[] upper = new Complex[N];

        Complex[] cPrime = new Complex[N];
        Complex[] dPrime = new Complex[N];
        Complex[] x = new Complex[N];

        for (int i = 0; i < N; i++) {
            diag[i] = A[i][i];
            lower[i] = (i > 0) ? A[i][i - 1] : new Complex(0, 0);
            upper[i] = (i < N - 1) ? A[i][i + 1] : new Complex(0, 0);
        }

        cPrime[0] = upper[0].divide(diag[0]);
        dPrime[0] = b[0].divide(diag[0]);

        for (int i = 1; i < N; i++) {
            Complex denom = diag[i].subtract(lower[i].multiply(cPrime[i - 1]));
            if (i < N - 1) cPrime[i] = upper[i].divide(denom);
            dPrime[i] = b[i].subtract(lower[i].multiply(dPrime[i - 1])).divide(denom);
        }

        x[N - 1] = dPrime[N - 1];

        for (int i = N - 2; i >= 0; i--){
             x[i] = dPrime[i].subtract(cPrime[i].multiply(x[i + 1]));
            }
        return x;
    }
}