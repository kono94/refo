package core;

public class DiscreteAction<A extends Enum> implements Action{
    private A action;

    public DiscreteAction(A action){
        this.action = action;
    }

    public A getValue(){
        return action;
    }

    @Override
    public int getIndex(){
       return action.ordinal();
    }

   @Override
    public String toString(){
        return action.toString();
    }

    @Override
    public int hashCode() {
        return getIndex();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DiscreteAction){
            return getIndex() == ((DiscreteAction) obj).getIndex();
        }
        return super.equals(obj);
    }
}
