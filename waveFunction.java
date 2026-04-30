public class waveFunction {
    private complex[] psi; // complex psi function values
    private double[] x ; // Stores position of particle in 1D
    private int N; // Number of grid points
    private double dx; //Distance between points


    public waveFunction(int grids , double dx){
        N = grids;
        this.dx = dx;

        psi = new complex[N];
        x = new double[N];

         for (int i = 0; i < N; i++) {
            x[i] = (i - N / 2.0) * dx; // creates symmetric space centered at zero
            psi[i] = new complex(0, 0); // Set psi valuse both re = 0 , Im = 0
        }
    }

    public complex[] getPsi(){  // Return All Psi function values
        return psi;
    }

    public double[] getX(){
        return x;       // Returns All positions
    }

     public int getSize() {
        return N;       // Returns GridSize
    }

    public double getDx() {
        return dx;      // Returns distance between two Adjacent points
    }


    public void initializeGaussian(double x0, double sigma, double k) {
 // x0 intial center position of particle , sigma = width of packet , k = wave number
    for (int i = 0; i < N; i++) {
        double gaussian = Math.exp(-Math.pow(x[i] - x0, 2) / (2 * sigma * sigma) );

        double real = gaussian * Math.cos(k * x[i]);
        double imag = gaussian * Math.sin(k * x[i]);

        psi[i] = new complex(real, imag);
    }
}

}
