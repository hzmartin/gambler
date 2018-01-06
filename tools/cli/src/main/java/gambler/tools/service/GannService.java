package gambler.tools.service;

import java.util.Date;
import java.util.Locale;

import gambler.commons.util.time.TimeUtils;
import gambler.tools.cli.bean.Gann4;

public class GannService extends AbstractService {

	public void printGann4Time(String starttime, String timeunit, int size) {
		printGann4Time(starttime, timeunit, size, 10);
	}

	public void printGann4Time(String starttime, String timeunit, int size, int celllen) {
		int matrix[][] = Gann4.gann4matrix(size);
		String format = "|%-" + celllen + "s";
		Date start = TimeUtils.parseDate(starttime);
		if ("d".equalsIgnoreCase(timeunit)) {
			for (int i = 0; i < size; i++) {
				fill(celllen, size, '-');
				for (int j = 0; j < size; j++) {
					System.out.format(format,
							TimeUtils.format(
									start.getTime() + (matrix[i][j] - 1) * TimeUtils.ONE_DAY_IN_MILLIS,
									"yyyy-MM-dd"));
				}
				System.out.println("|");
			}
			fill(celllen, size, '-');
		} else if ("w".equalsIgnoreCase(timeunit)) {
			for (int i = 0; i < size; i++) {
				fill(celllen, size, '-');
				for (int j = 0; j < size; j++) {
					int startsecs = TimeUtils.getMondayOfThisWeek(start);
					System.out.format(format,
							TimeUtils.format(
									startsecs * 1000L
											+ (matrix[i][j] - 1) * TimeUtils.ONE_DAY_IN_MILLIS * 7,
									"yyyy-MM-dd"));
				}
				System.out.println("|");
			}
			fill(celllen, size, '-');
		} else if ("m".equalsIgnoreCase(timeunit)) {
			for (int i = 0; i < size; i++) {
				fill(celllen, size, '-');
				for (int j = 0; j < size; j++) {
					System.out.format(format, TimeUtils.format(
							TimeUtils.getFirstDayOfMonth(start, matrix[i][j] - 1) * 1000L, "yyyy-MM-dd"));
				}
				System.out.println("|");
			}
			fill(celllen, size, '-');
		} else {
			throw new IllegalArgumentException("illegal timeunit");
		}
	}

	public void printGann4Price(int startprice, int pricestep, int size) {
		printGann4Price(startprice, pricestep, size, 8);
	}

	public void printGann4Price(int startprice, int pricestep, int size, int celllen) {
		int matrix[][] = Gann4.gann4matrix(size);
		String format = "|%-" + celllen + "d";
		for (int i = 0; i < size; i++) {
			fill(celllen, size, '-');
			for (int j = 0; j < size; j++) {
				System.out.format(format, startprice + (matrix[i][j] - 1) * pricestep);
			}
			System.out.println("|");
		}
		fill(celllen, size, '-');
	}

	private void fill(int celllen, int size, char fillChar) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < celllen + 1; j++) {
				sb.append(fillChar);
			}
		}
		sb.append(fillChar);
		System.out.println(sb.toString());
	}

	public static void main(String[] args) {
		// HSI INDEX
		int startprice = 6890;
		String starttime = "2017-12-02";
		int pricestep = 100;
		String timeunit = "m";
		GannService g = new GannService();
		int size = 13;
		g.printGann4Price(startprice, pricestep, size, 6);
		g.printGann4Time(starttime, timeunit, size, 10);
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
