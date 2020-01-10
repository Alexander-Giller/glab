package g.tools.file;

public class FinalExample {

    public static class A {
        public static final int a = B.b + 1;
    }

    public static class B {
        public static final int b = A.a + 1;
    }

    public static void main(String[] args) {
        System.out.println(B.b);
        System.out.println(A.a);
    }

}
