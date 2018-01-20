package gambler.tools.service;

import java.util.Locale;

import gambler.commons.advmap.XMLMap;
import gambler.tools.cli.CLISystem;

public class AbstractService {

	static final XMLMap sysConfig = CLISystem.SYSCONFIG;

	/**
	 * print one fill line
	 * 
	 * @param celllen
	 *            - char size in one cell
	 * @param size
	 *            - cell number in one line
	 * @param fillChar
	 *            - ex: '-'
	 */
	protected void fill(int celllen, int size, char fillChar) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < celllen + 1; j++) {
				sb.append(fillChar);
			}
		}
		sb.append(fillChar);
		System.out.println(sb.toString());
	}

	protected void exampleprint() {

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