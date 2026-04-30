public class Main {
    public static void main(String[] args) {
        Complex a = new Complex(1, 2);
        Complex b = new Complex(3, 4);

        System.out.println(a.add(b));
        System.out.println(a.multiply(b));
    }
}