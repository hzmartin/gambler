package gambler.tools.service;

import java.util.Locale;

import gambler.commons.util.time.TimeUtils;
import gambler.tools.cli.bean.Gann4;

public class GannService extends AbstractService {

	public void printGann4_SIZE13(double startprice, String starttime, double pricestep, String timeunit,
			String output) {
		TimeUtils.parseDate(starttime);
		Gann4 GANN4 = new Gann4();
		int[][] gann4 = GANN4.Gann4_SIZE13();
		int celllen = 8;
		int size = 13;
		StringBuffer sb = new StringBuffer();
		char fillChar = '-';
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < celllen+1; j++) {
				sb.append(fillChar);
			}
		}
		sb.append(fillChar);
		for (int i = 0; i < size; i++) {
			System.out.println(sb.toString());
			for (int j = 0; j < size; j++) {
				System.out.format("|%-" + celllen + "d", gann4[i][j]);
			}
			System.out.println("|");
		}
	}

	public static void main(String[] args) {
		new GannService().printGann4_SIZE13(0, "2000-01-01", 1, "d", "o.txt");
	}

	public void formatprint() {
		int year = 2020;
		// 总长度，左对齐，补0，千位分隔符，小数点位数，本地化表达

		// 直接打印数字
		System.out.format("%d%n", year);
		// 总长度是8,默认右对齐
		System.out.format("%8d%n", year);
		// 总长度是8,左对齐
		System.out.format("%-8d%n", year);
		// 总长度是8,不够补0
		System.out.format("%08d%n", year);
		// 千位分隔符
		System.out.format("%,8d%n", year * 10000);

		// 小数点位数
		System.out.format("%.2f%n", Math.PI);

		// 不同国家的千位分隔符
		System.out.format(Locale.FRANCE, "%,.2f%n", Math.PI * 10000);
		System.out.format(Locale.US, "%,.2f%n", Math.PI * 10000);
		System.out.format(Locale.UK, "%,.2f%n", Math.PI * 10000);

	}

}
