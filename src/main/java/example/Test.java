package example;

public class Test {
    interface Drawable{
        void draw();
    }
    interface State{
        int getInt();
    }

    static class A implements  Drawable, State{
        private int k;
        public A(int a){
            k = a;
        }
        @Override
        public void draw() {
            System.out.println("draw " + k);
        }

        @Override
        public int getInt() {
            System.out.println("getInt" + k);
            return k;
        }
    }

    static class B  implements State{
        @Override
        public int getInt() {
            return 0;
        }
    }

    public static void main(String[] args) {
        State state = new A(24);
        State state2 = new B();
        state.getInt();

        System.out.println(state2 instanceof Drawable);
        drawState(state2);
    }

    static void drawState(State s){
        if(s instanceof Drawable){
            Drawable d = (Drawable) s;
            d.draw();
        }else{
            System.out.println("invalid");
        }
    }
}
