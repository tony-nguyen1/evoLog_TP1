package dummy;

public class C {
	int i;int j;
	
	public C() {
		i = 0;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	@Override
	public String toString() {
		return "C [i=" + i + ", getI()=" + getI() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
	
}
