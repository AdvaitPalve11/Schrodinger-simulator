public class complex{

    public double re;
    public double im;

    public complex(double R , double I){
        re = R;
        im = I;
    }

    public complex add(complex num){
        return new complex(this.re + num.re , this.im + num.im);
    }

    public complex subtract(complex num){
        return new complex(this.re - num.re , this.im - num.im);
    }

    public complex multiply(complex num){
        return new complex(this.re * num.re - this.im * num.im , this.re * num.im + this.im * num.re);
    } 

        public complex scale(double scalar) {
        return new complex( this.re * scalar, this.im * scalar);
    }

     public double magnitudeSquared() {
        return re * re + im * im;
    }

    public String toString() {
        return re + " + " + im + "i";
    }
    
}