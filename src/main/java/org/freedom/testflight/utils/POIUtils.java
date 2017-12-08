package org.freedom.testflight.utils;


import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class POIUtils {
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";

    /**
     * 读入excel文件，解析后返回
     */
    public static List<String[]> readExcel(String fileName) throws IOException {
        //检查文件
        checkFile(fileName);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(fileName);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<String[]> list = new ArrayList<String[]>();
        if (workbook != null) {
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                //获得当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if (sheet == null) {
                    continue;
                }
                //获得当前sheet的开始行
                int firstRowNum = sheet.getFirstRowNum();
                //获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                //循环除了第一行的所有行
                for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                    //获得当前行
                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }
                    //获得当前行的开始列
                    int firstCellNum = row.getFirstCellNum();
                    //获得当前行的列数
                    int lastCellNum = row.getPhysicalNumberOfCells();
                    String[] cells = new String[row.getPhysicalNumberOfCells()];
                    //循环当前行
                    for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        cells[cellNum] = getCellValue(cell);
                    }
                    list.add(cells);
                }
            }
            workbook.close();
        }
        return list;
    }

    private static void checkFile(String fileName) throws IOException {
        //判断文件是否存在
        if (StringUtils.isBlank(fileName) || !new File(fileName).exists()) {
            throw new FileNotFoundException("文件不存在！");
        }
        //判断文件是否是excel文件
        if (!fileName.endsWith(xls) && !fileName.endsWith(xlsx)) {
            throw new IOException(fileName + "不是excel文件");
        }
    }

    private static Workbook getWorkBook(String fileName) {
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = new FileInputStream(fileName);
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileName.endsWith(xls)) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith(xlsx)) {
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            throw new RuntimeException("get excel file io stream exception");
        }
        return workbook;
    }

    private static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        //把数字当成String来读，避免出现1读成1.0的情况
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        //判断数据的类型
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: //数字
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK: //空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }

    public static void writeExcel(String fileName, List<String[]> content) throws IOException {
        createFileIfNotExist(fileName);
        Workbook workbook = createWorkBook(fileName);
        if (workbook == null) {
            throw new RuntimeException("workbook null exception");
        }
        Sheet sheet = workbook.createSheet();
        for (int i = 0; i < content.size(); i++) {
            Row row = sheet.createRow(i);
            String[] rowContent = content.get(i);
            for (int j = 0; j < rowContent.length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(rowContent[j]);
            }
        }
        workbook.write(new FileOutputStream(fileName));
        workbook.close();
    }

    private static void createFileIfNotExist(String fileName) throws IOException {
        //判断文件是否存在
        if (StringUtils.isBlank(fileName)) {
            throw new FileNotFoundException("文件名不能为空！");
        }
        //判断文件是否是excel文件
        if (!fileName.endsWith(xls) && !fileName.endsWith(xlsx)) {
            throw new IOException(fileName + "不是excel文件");
        }

        File file = new File(fileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
    }

    private static Workbook createWorkBook(String fileName) {
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
        if (fileName.endsWith(xls)) {
            //2003
            workbook = new HSSFWorkbook();
        } else if (fileName.endsWith(xlsx)) {
            //2007
            workbook = new XSSFWorkbook();
        }
        return workbook;
    }

    public static void main(String[] args)throws IOException{
        List<String[]> content=new ArrayList<>();
        String[] rowContent=new String[]{"a","A"};
        String[] rowContent2=new String[]{"b","B"};
        content.add(rowContent);
        content.add(rowContent2);
        String fileName="/Users/chaolinye/Downloads/test/A.xlsx";
        writeExcel(fileName,content);
    }
}
