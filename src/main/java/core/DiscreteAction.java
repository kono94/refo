package core;

public class DiscreteAction<A extends Enum> implements Action{
    private A action;

    public DiscreteAction(A action){
        this.action = action;
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
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        return getIndex() == ((DiscreteAction) obj).getIndex();
    }
}
