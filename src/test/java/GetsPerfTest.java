import java.util.ArrayList;

import lombok.extern.slf4j.Slf4j;

import com.xjd.gets.Gets;

/**
 * @author elvis.xu
 * @since 2018-08-26 15:39
 */
@Slf4j
public class GetsPerfTest {
	public static void main(String[] args) throws InterruptedException {

		GetsTest.A a = newA();


		int times = 1;
		for (int j = 0; j < 1; j++) {
			System.out.println("=========");

			{
				Thread.sleep(1000L);
				long start = System.currentTimeMillis();
				for (int i = 0; i < times; i++) {
					if (a != null && a.getAdd() != null && a.getAdd().getInfo() != null && a.getAdd().getInfo().getList() != null) {
//						a.getAdd().getInfo().getList().size();
					}
				}
				System.out.println("cost: " + (System.currentTimeMillis() - start));
			}
			{
				Thread.sleep(1000L);
				long start = System.currentTimeMillis();
				for (int i = 0; i < times; i++) {
					if (Gets.get(Gets.wrap(a).getAdd().getInfo().getList()) != null) {
//						a.getAdd().getInfo().getList().size();
					}
				}
				System.out.println("cost: " + (System.currentTimeMillis() - start));
			}
		}

		times = 100;
		for (int j = 0; j < 10; j++) {
			System.out.println("=========");

			{
				Thread.sleep(1000L);
				long start = System.currentTimeMillis();
				for (int i = 0; i < times; i++) {
					if (a != null && a.getAdd() != null && a.getAdd().getInfo() != null && a.getAdd().getInfo().getList() != null) {
//						a.getAdd().getInfo().getList().size();
					}
				}
				System.out.println("cost: " + (System.currentTimeMillis() - start));
			}
			{
				Thread.sleep(1000L);
				long start = System.currentTimeMillis();
				for (int i = 0; i < times; i++) {
					if (Gets.get(Gets.wrap(a).getAdd().getInfo().getList()) != null) {
//						a.getAdd().getInfo().getList().size();
					}
				}
				System.out.println("cost: " + (System.currentTimeMillis() - start));
			}
		}

	}

	public static GetsTest.A newA() {
		GetsTest.A a = new GetsTest.A();
		GetsTest.Add add = new GetsTest.Add();
		GetsTest.Info info = new GetsTest.Info();
		info.setList(new ArrayList<>());
		add.setInfo(info);
		a.setAdd(add);
		return a;
	}
}
