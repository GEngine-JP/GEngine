package info.xiaomo.gameCore.base.tuple;

public class FourTuple<A, B, C, D> extends ThreeTuple<A, B, C>
{

    public final D fourth;

    public FourTuple(A a, B b, C c, D d)
    {
        super(a, b, c);
        fourth = d;
    }

    public String toString()
    {
        return "(" + first + ", " + second + ", " + third + ", " + fourth + ")";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((fourth == null) ? 0 : fourth.hashCode());
        return result;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!super.equals(obj))
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        FourTuple other = (FourTuple) obj;
        if (fourth == null)
        {
            if (other.fourth != null)
            {
                return false;
            }
        }
        else if (!fourth.equals(other.fourth))
        {
            return false;
        }
        return true;
    }

}
