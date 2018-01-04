package gambler.tools.service;

import java.util.Date;
import java.util.Locale;

import gambler.commons.advmap.XMLMap;
import gambler.tools.cli.CLISystem;

public class GannService {

	private static final XMLMap sysConfig = CLISystem.SYSCONFIG;

	public void printGann4(double startprice, Date starttime, double pricestep, String timeunit, int size,
			String output) {

	}
	
	public static void main(String[] args){
	}
	

	public void test() {
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
