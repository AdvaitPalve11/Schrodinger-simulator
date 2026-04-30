public class Complex{

    public double re;
    public double im;

    public Complex(double R , double I){
        re = R;
        im = I;
    }

    public Complex add(Complex num){
        return new Complex(this.re + num.re , this.im + num.im);
    }

    public Complex subtract(Complex num){
        return new Complex(this.re - num.re , this.im - num.im);
    }

    public Complex multiply(Complex num){
        return new Complex(this.re * num.re - this.im * num.im , this.re * num.im + this.im * num.re);
    } 

        public Complex scale(double scalar) {
        return new Complex( this.re * scalar, this.im * scalar);
    }

     public double magnitudeSquared() {
        return re * re + im * im;
    }

    public String toString() {
        return re + " + " + im + "i";
    }
    
}