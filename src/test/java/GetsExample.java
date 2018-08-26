import java.util.List;

import lombok.Getter;

import com.xjd.gets.Gets;

/**
 * @author elvis.xu
 * @since 2018-08-26 21:08
 */
public class GetsExample {
	public static void main(String[] args) {
		A a = null;

		// ... some operations

		// traditional way
		if (a != null && a.getB() != null && a.getB().getC() != null
				&& a.getB().getC().getD() != null && a.getB().getC().getD().getList() != null) {
			for (Object item : a.getB().getC().getD().getList()) {
				// ... do some business
			}
		}

		// use Gets way
		if (Gets.isNotNull(Gets.wrap(a, A.class).getB().getC().getD().getList())) {
			// ... do 'for' business
		}

		// or use Gets as
		if (Gets.get(Gets.wrap(a, A.class).getB().getC().getD().getList()) != null) {
			// ... do 'for' business
		}

		// some other use
		String key = Gets.get(Gets.wrap(a, A.class).getB().getC().getD().getKey(), "NO_KEY");
		System.out.println(key); // key = "NO_KEY";

	}


	@Getter
	public static class A {
		private B b;
	}

	@Getter
	public static class B {
		private C c;
	}

	@Getter
	public static class C {
		private D d;
	}

	@Getter
	public static class D {
		private String key;
		private List list;
	}
}
