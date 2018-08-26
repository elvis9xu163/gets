import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.xjd.gets.Gets;

/**
 * @author elvis.xu
 * @since 2018-08-26 15:39
 */
@Slf4j
public class GetsTest {
	public static void main(String[] args) {
		A a = new A();
		A a2 = new A();
		Add add = new Add();
		add.setFormat("HAHAHAH");
		a2.setAdd(add);
		A wrapA = Gets.wrap(a);
		A wrapA2 = Gets.wrap(a2);
		System.out.println(Gets.get(wrapA.getAdd(), "HELLO"));
		System.out.println(Gets.get(wrapA.getAdd().getCode(), "HELLO"));
		System.out.println(Gets.get(wrapA.getAdd().getAge(), 50));
		System.out.println(Gets.get(wrapA.getAdd().getInfo().getOthers(), 50));

		System.out.println(Gets.get(wrapA2.getAdd(), "HELLO"));
		System.out.println(Gets.get(wrapA2.getAdd().getFormat(), "HELLO"));
		System.out.println(Gets.get(wrapA2.getAdd().getInfo().getOthers(), "HELLO"));

		System.out.println(Gets.get(wrapA2.getAdd().getInfo().getList().get(0), "HELLO"));
		if (Gets.get(wrapA2.getAdd().getInfo().getList()) == null) {
			System.out.println("list is null");
		}
	}

	@Getter
	@Setter
	public static class A {
		String name;
		Boolean big;
		Add add;


	}

	@Getter
	@Setter
	public static class Add {
		String format;
		String code;
		Info info;


		public Integer getAge() {
			return -1;
		}
	}

	@Getter
	@Setter
	public static class Info {
		String others;
		List<String> list;
	}
}
