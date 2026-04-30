public class potential {
    private double[] v;
    private int N;

    public potential(int n){
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
}
