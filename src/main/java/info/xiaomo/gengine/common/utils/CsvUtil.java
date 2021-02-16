package info.xiaomo.gengine.common.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 * @author xiaomo
 */
public class CsvUtil {

    /**
     * 网络地址
     * @param str str
     * @param skipLine skipLine
	 * @return CsvData
	 */
	public static CsvData readConfigDataFromUrl(String str, int skipLine) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(str).openStream()))) {
			String tempString;
			List<String> lines = new ArrayList<>();
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				lines.add(tempString);
			}
			reader.close();
			return new CsvData(lines, skipLine);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

    /**
     * 本地文件
     * @param filepath filepath
     * @param skipLine skipLine
	 * @return CsvData
	 */
	public static CsvData read(String filepath, int skipLine) {
		BufferedReader br = null;
		try {

			br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8"));

			String line;
			ArrayList<String> lines = new ArrayList<>();
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}

			return new CsvData(lines, skipLine);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public static class CsvData {

		private String[] tableHead;

		public List<Map<String, String>> tableRows;

		private final int skipLine;


		public CsvData(List<String> lines, int skipLine) {

			this.skipLine = skipLine;

			// 跳过其他注释行
			skip(lines);

			// 读表头
			readTH(lines);

			// 读表行
			readTR(lines);
		}


		public void skip(List<String> lines){
			for(int i = 0; i <  skipLine; i++) {
				if(lines.isEmpty()){
					break;
				}
				lines.remove(0);
			}
		}

		public void readTH(List<String> lines) {
			String line = lines.remove(0);
			tableHead = line.trim().split(SymbolUtil.DOUHAO);
		}

		public void readTR(List<String> lines) {
			// 读数据
			tableRows = new ArrayList<>();
			String line;
			String[] lineArray;
			for (String line1 : lines) {
				Map<String, String> tr = new HashMap<>(10);
				line = line1;
				lineArray = line.split(SymbolUtil.DOUHAO);
				for (int j = 0; j < lineArray.length; j++) {
					if (j >= tableHead.length) {
						continue;
					}
					String col = lineArray[j];
					tr.put(tableHead[j], col);
				}
				tableRows.add(tr);
			}
		}

		@Override
		public String toString() {
			return "ListConfigData:" + Arrays.toString(this.tableHead) + "=>" + tableRows.toString();
		}
	}

	public static void main(String[] args) {
		CsvData csvData = readConfigDataFromUrl("http://xiaomo-app.oss-ap-northeast-1.aliyuncs.com/cfg_test.csv", 3);
		if (csvData == null) {
			return;
		}
		List<Map<String, String>> tableRows = csvData.tableRows;
		for (Map<String, String> tableRow : tableRows) {
			System.out.println(tableRow.toString());
		}
	}
}
