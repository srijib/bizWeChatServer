package cn.yikuair.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.log4j.Logger;

import cn.yikuair.base.Log;

public class ExcelUtil {
	private static Logger logger = Log.getLogger(ExcelUtil.class);
	public static List<Map<String,String>> getXlsUserInfoData(String path){
		try{
			File xlsFile = new File(path);
			Workbook book = Workbook.getWorkbook(xlsFile);
			Sheet []sheet = book.getSheets();
			if(sheet.length>0){
				Sheet page = sheet[0];
				int columns = page.getColumns();
				System.out.println(columns);
				int rows = page.getRows();
				System.out.println(page.getName());
				if(rows<2) return null;
				
				int columnsCount = 0;
				
				List <String>titleList = new ArrayList<String>();
				for(int i=0;i<columns;i++){
					//列    行
					Cell cell = page.getCell(i,0);
					String key = cell.getContents();
					if(key==null||key.equals("")){
						continue;
					}
					columnsCount++;
					logger.debug(key);
					titleList.add(key);
				}
				columns = columnsCount;
				List <Map<String,String>>dataList = new ArrayList<Map<String,String>>();
				for(int i=1;i<rows;i++){
					logger.info("ii:"+i);
					Map <String,String> dataMap = new HashMap<String,String>();
					for(int j=0;j<columns;j++){
						String key = titleList.get(j);
						String value = page.getCell(j, i).getContents();
						dataMap.put(key, value);
						logger.debug(key+"  "+value);
					}
					dataList.add(dataMap);
				}
				logger.debug("xls length:"+dataList.size());
				return dataList;
			}
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Map<String,String>> list = getXlsUserInfoData("/Users/yikuair/Documents/workspace/yikuairAPI/WebContent/static/excel/employee_list.xls");
		System.out.println(list.size());
	}

}
