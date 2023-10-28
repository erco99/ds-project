package fe.app.util;

/*
 * A standard generic Pair<X,Y>, with getters, hashCode, equals, and toString well implemented. 
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Pair<X,Y> {

	@Expose
	@SerializedName("c")
	private final X x;

	@Expose
	@SerializedName("y")
	private final Y y;
	
	public Pair(X x, Y y) {
		super();
		this.x = x;
		this.y = y;
	}

	public X getX() {
		return x;
	}

	public Y getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (y == null) {
			if (other.y != null)
				return false;
		} else if (!y.equals(other.y))
			return false;
		return true;
	}

	public static Pair<Integer,Integer> toInteger(Pair<Double,Double> pair) {
		return new Pair<>(pair.getX().intValue(),pair.getY().intValue());
	}

	public static Pair<Double,Double> toDouble(Pair<Integer,Integer> pair) {
		return new Pair<>(pair.getX().doubleValue(),pair.getY().doubleValue());
	}

	public static boolean equals(Pair<Integer,Integer> firstPair,Pair<Integer,Integer> secondPair) {
		return Objects.equals(firstPair.getX(), secondPair.getX())
				&& Objects.equals(firstPair.getY(), secondPair.getY());
	}
	@Override
	public String toString() {
		return "Pair [x=" + x + ", y=" + y + "]";
	}

}
