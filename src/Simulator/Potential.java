package src.Simulator;
public class Potential {
    private double[] v;
    private int N;

    public Potential(int n){
       this.N = n;
        v = new double[N];
    }

    public void createBarrier(int start , int end , double height){

        for(int i = start ; i <end ; i++)
            v[i] = height;
    }

    public double[] getV(){
        return v;
    }

    public void createHarmonic(double omega, double[] x) {
    for (int i = 0; i < N; i++) {
        v[i] = 0.5 * omega * omega * x[i] * x[i];
    }
}
}
