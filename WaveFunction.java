public class WaveFunction {
    private Complex[] psi; // complex psi function values
    private double[] x ; // spatial grid positions
    private int N; // Number of grid points
    private double dx; //Distance between points


    public WaveFunction(int grids , double dx){
        this.N = grids;
        this.dx = dx;

        psi = new Complex[N];
        x = new double[N];

         for (int i = 0; i < N; i++) {
            x[i] = (i - N / 2.0) * dx; // creates symmetric space centered at zero
            psi[i] = new Complex(0, 0); // Set psi valuse both re = 0 , Im = 0
        }
    }

    public Complex[] getPsi(){  // Return All Psi function values
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

    public void normalize() {
    double total = totalProbability();
    double factor = Math.sqrt(total);

    for (int i = 0; i < N; i++) {
        psi[i] = psi[i].scale(1.0 / factor);
    }
}

    public void initializeGaussian(double x0, double sigma, double k) {
 // x0 intial center position of particle , sigma = width of packet , k = wave number
    for (int i = 0; i < N; i++) {
        double gaussian = Math.exp(-Math.pow(x[i] - x0, 2) / (2 * sigma * sigma) );

        double real = gaussian * Math.cos(k * x[i]);
        double imag = gaussian * Math.sin(k * x[i]);

        psi[i] = new Complex(real, imag);
        normalize();
    }
}
public double totalProbability() {
    double sum = 0;

    for (int i = 0; i < N; i++) {
        sum += psi[i].magnitudeSquared();
    }

    return sum * dx;
}

public double expectationX() {
    double sum = 0;
    for (int i = 0; i < N; i++) sum += x[i] * psi[i].magnitudeSquared();
    return sum * dx;
}



}
