package gambler.tools.service;

import java.util.Date;

import gambler.commons.util.time.TimeUtils;
import gambler.tools.cli.util.Gann4;

public class GannService extends AbstractService {

	public void printGann4TimeByInterval(String starttime, long interval, int size, int celllen, String outputformat) {
		int matrix[][] = Gann4.gann4matrix(size);
		String format = "|%-" + celllen + "s";
		Date start = TimeUtils.parseDate(starttime);
		for (int i = 0; i < size; i++) {
			fill(celllen, size, '-');
			for (int j = 0; j < size; j++) {
				System.out.format(format,
						TimeUtils.format(start.getTime() + (matrix[i][j] - 1) * interval, outputformat));
			}
			System.out.println("|");
		}
		fill(celllen, size, '-');
	}

	public void printGann4Time(String starttime, String timeunit, int size) {
		int celllen = 10;
		if ("h".equalsIgnoreCase(timeunit)) {
			celllen = 13;
		}
		printGann4Time(starttime, timeunit, size, celllen);
	}

	public void printGann4Time(String starttime, String timeunit, int size, int celllen) {
		if ("h".equalsIgnoreCase(timeunit)) {
			//TODO: 根据不同市场每天60分钟K线数输出
			throw new IllegalArgumentException("unimplemented");
		} else if ("d".equalsIgnoreCase(timeunit)) {
			printGann4TimeByInterval(starttime, TimeUtils.ONE_DAY_IN_MILLIS, size, celllen, "yyyy-MM-dd");
		} else if ("w".equalsIgnoreCase(timeunit)) {
			Date start = TimeUtils.parseDate(starttime);
			int startsecs = TimeUtils.getMondayOfThisWeek(start);
			String monday = TimeUtils.format(startsecs * 1000L, "yyyy-MM-dd");
			printGann4TimeByInterval(monday, TimeUtils.ONE_DAY_IN_MILLIS * 7, size, celllen, "yyyy-MM-dd");
		} else if ("m".equalsIgnoreCase(timeunit)) {
			int matrix[][] = Gann4.gann4matrix(size);
			String format = "|%-" + celllen + "s";
			Date start = TimeUtils.parseDate(starttime);
			for (int i = 0; i < size; i++) {
				fill(celllen, size, '-');
				for (int j = 0; j < size; j++) {
					System.out.format(format, TimeUtils
							.format(TimeUtils.getFirstDayOfMonth(start, matrix[i][j] - 1) * 1000L, "yyyy-MM-dd"));
				}
				System.out.println("|");
			}
			fill(celllen, size, '-');
		} else {
			throw new IllegalArgumentException("illegal timeunit");
		}
	}

	public void printGann4Price(int startprice, int pricestep, int size) {
		printGann4Price(startprice, pricestep, size, 9);
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

}
