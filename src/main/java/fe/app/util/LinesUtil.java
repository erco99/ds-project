package fe.app.util;

public class LinesUtil {

    public static Pair<Integer,Integer> lineLineIntersection(
            Pair<Integer,Integer> A, Pair<Integer,Integer> B, Pair<Integer,Integer> C, Pair<Integer,Integer> D)
    {
        // Line AB represented as a1x + b1y = c1
        Integer a1 = B.getY() - A.getY();
        Integer b1 = A.getX() - B.getX();
        Integer c1 = a1*(A.getX()) + b1*(A.getY());

        // Line CD represented as a2x + b2y = c2
        Integer a2 = D.getY() - C.getY();
        Integer b2 = C.getX() - D.getX();
        Integer c2 = a2*(C.getX())+ b2*(C.getY());

        int determinant = a1*b2 - a2*b1;

        if (determinant == 0)
        {
            // The lines are parallel. This is simplified
            // by returning a pair of FLT_MAX
            return new Pair<>(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }
        else
        {
            Integer x = (b2*c1 - b1*c2)/determinant;
            Integer y = (a1*c2 - a2*c1)/determinant;
            return new Pair<>(x, y);
        }
    }

}
