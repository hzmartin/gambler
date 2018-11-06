/**
 * @(#)ExcelService.java, 2014年1月2日.
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package gambler.quartz.service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.stereotype.Service;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@Service
public class ExcelService {

	public <T> File exportExcel(List<T> dataList) throws IOException, RowsExceededException, WriteException,
			IllegalArgumentException, IllegalAccessException {

		File file = File.createTempFile("export-", ".xls");

		WritableWorkbook book = Workbook.createWorkbook(file);
		WritableSheet sheet = book.createSheet("Sheet_1", 0);

		WritableFont wfc = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
				UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLUE);

		WritableCellFormat wcfFC = new WritableCellFormat(wfc);
		Field[] fields = dataList.get(0).getClass().getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			sheet.addCell(new Label(i, 0, field.getName(), wcfFC));
		}

		for (int i = 1; i <= dataList.size(); i++) {
			T t = dataList.get(i - 1);

			for (int j = 0; j < fields.length; j++) {
				Field field = fields[j];
				field.setAccessible(true);
				Object object = field.get(t);
				sheet.addCell(new Label(j, i, object == null ? "" : object.toString()));
			}
		}

		book.write();
		book.close();
		return file;
	}

	public File exportExcelByOneColumn(Map<String, List<String>> userList) throws Exception {
		File file = File.createTempFile("export-", ".xls");

		WritableWorkbook book = Workbook.createWorkbook(file);
		Set<Entry<String, List<String>>> entrySet = userList.entrySet();
		int sheetIndex = 0;
		for (Entry<String, List<String>> entry : entrySet) {
			WritableSheet sheet = book.createSheet(entry.getKey(), sheetIndex++);
			List<String> list = entry.getValue();
			for (int i = 0; i < list.size(); i++) {
				String t = list.get(i);
				sheet.addCell(new Label(0, i, t == null ? "" : t));
			}
			sheetIndex++;
		}

		book.write();
		book.close();
		return file;
	}

}
