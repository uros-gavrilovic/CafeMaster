package backend_package;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import lib.Libary;

public class Report {
	
	private LinkedList<Product> products;
	
	public Report(LinkedList<Product> products) {
		this.products = products;
	}

	void readCell(XSSFCell cell) {
		switch(cell.getCellType()) {
			case STRING: 
				System.out.print(cell.getStringCellValue() + " ");
				break;
			case NUMERIC:
				System.out.print(cell.getNumericCellValue() + " ");
				break;
			default: return;
		}
	}
	

	public static boolean doLogsExist() {
		// checks if logs are created to decide whetever to append to existing Excel file or to create a new one
				File excelFile = new File(Libary.getReportsPath() + "\\" + Libary.createReportName());
				if(!excelFile.exists()) return false;
				
				try {
			        FileInputStream fip = new FileInputStream(excelFile);
			        XSSFWorkbook workbook;
					workbook = new XSSFWorkbook(fip);
					
					if(workbook.getSheetIndex("EVIDENCIJA") == -1) {
						return false;
					} else {
						return true;
					}
				} catch (IOException e) {
					Libary.errorIOException(excelFile.getName(), Libary.getMethodName());
				}
				return false;
	}
	public static boolean doesReportExist() {
		// checks if report is created to decide whetever to append to existing Excel file or to create a new one
		File excelFile = new File(Libary.getReportsPath() + "\\" + Libary.createReportName());
		if(!excelFile.exists()) return false;
		
		try {
	        FileInputStream fip = new FileInputStream(excelFile);
	        XSSFWorkbook workbook;
			workbook = new XSSFWorkbook(fip);
			
			if(workbook.getSheetIndex("IZVEŠTAJ") == -1) {
				return false;
			} else {
				return true;
			}
		} catch (IOException e) {
			Libary.errorIOException(excelFile.getName(), Libary.getMethodName());
		}
		return false;
	}
	
	
	public void createReport() {
		try {
			File izvestaj = new File(Libary.getReportsPath() + "\\" + Libary.createReportName());
			if(izvestaj.createNewFile()) System.out.println("Report \"" + Libary.createReportName() + "\" is successfully created!");
			
			@SuppressWarnings("resource")
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("IZVEŠTAJ");
			
			  	CellStyle headerCellStyle = sheet.getWorkbook().createCellStyle();
			  	headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
			  	headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//			  	headerCellStyle.setFont(new Font("Times New Roman", Font.PLAIN, 12));
			
			double totalTurnover = 0; 	
			
			int rows = products.size();
			String[] header = {"KATEGORIJA", "NAZIV", "CENA", "KOLIČINA"};
			int columns = header.length;
			Iterator<Product> it = products.iterator();
			String[] productParts = new String[4];
			
			XSSFRow headerRed = sheet.createRow(0);
			for(int i=0; i<header.length; i++) {
				XSSFCell cell = headerRed.createCell(i);
				cell.setCellValue(header[i]);
				cell.setCellStyle(headerCellStyle);
			}
			
			for(int i=1; i<=rows; i++) {
				XSSFRow row = sheet.createRow(i);
				Product productIterator = it.next();
				productParts = Libary.productToArray(productIterator);
				for(int j=0; j<columns; j++) {
					XSSFCell cell = row.createCell(j);
					if(j == 2) {
						cell.setCellValue(Double.parseDouble(productParts[j]));
						totalTurnover = totalTurnover + Double.parseDouble(productParts[j]) * productIterator.getQuantity();
					} else {
						cell.setCellValue(productParts[j]);
					}
					sheet.autoSizeColumn(j);
				}
			}
			
			// --- CALCULATING TURNOVER ---
			XSSFRow turnoverRow = sheet.createRow(rows + 2);
			XSSFCell cell = turnoverRow.createCell(0);
			cell.setCellValue("UKUPNI PAZAR: ");
			cell = turnoverRow.createCell(1);
			cell.setCellValue(totalTurnover);
			sheet.autoSizeColumn(turnoverRow.getRowNum());
			
			FileOutputStream fos = new FileOutputStream((Libary.getReportsPath() + "\\" + Libary.createReportName()));
			workbook.write(fos);
			fos.close();
			JOptionPane.showMessageDialog(null, "Izveštaj je uspešno napravljen!", "Kreiranje izveštaja", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			Libary.errorIOException(Libary.createReportName(), Libary.getMethodName());
		}
	}
	@SuppressWarnings("resource")
	public void createLogs() {
		try {
			File reportFile = new File(Libary.getReportsPath() + Libary.createReportName());
			XSSFWorkbook workbook;
			
			if(reportFile.exists() == false) {
				System.out.println("Creating reports file \"" + Libary.createReportName() + "\" for logs activity.");
				workbook = new XSSFWorkbook();
			} else {
				System.out.println("Adding logs to the existing reports file \"" + Libary.createReportName() + "\".");
				InputStream is = new FileInputStream(reportFile);
				workbook = new XSSFWorkbook(is);
				is.close();
			}	
			XSSFSheet sheet = workbook.createSheet("EVIDENCIJA");
			
			  	CellStyle headerCellStyle = sheet.getWorkbook().createCellStyle();
			  	headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
			  	headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	//		  	headerCellStyle.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		
			String[] header = {"NAZIV PROIZVODA", "VREME NARUDŽBINE", "ID KONOBARA"};
		  	int rows = products.size();
		  	int columns = header.length;
		  	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		  	
		  	Iterator<Product> it = products.iterator();
			//String[] deloviProducta = new String[4];
		
			XSSFRow headerRow = sheet.createRow(0);
			for(int i=0; i<header.length; i++) {
				XSSFCell cell = headerRow.createCell(i);
				cell.setCellValue(header[i]);
				cell.setCellStyle(headerCellStyle);
			}
			
			for(int i=1; i<=rows; i++) {
				XSSFRow row = sheet.createRow(i);
				Product productIterator = it.next();
				for(int j=0; j<columns; j++) {
					XSSFCell cell = row.createCell(j);
					switch(j) {
					case 0:
						cell.setCellValue(productIterator.toString());
						break;
					case 1:
						cell.setCellValue(sdf.format(productIterator.getOrderTime().getTime()));
						break;
					case 2:
						cell.setCellValue(productIterator.getUserId());
						break;
					}
					sheet.autoSizeColumn(j);
				}
			}
			
			FileOutputStream fos = new FileOutputStream((Libary.getReportsPath() + "\\" + Libary.createReportName()));
			workbook.write(fos);
			fos.close();
			
			System.out.println("Logs \"" + Libary.getReportsPath() + "\" are succesfully created");
			JOptionPane.showMessageDialog(null, "Evidencija je uspešno napravljena!", "Kreiranje evidencije", JOptionPane.INFORMATION_MESSAGE);
		
		} catch (IOException e) {
			Libary.errorIOException(Libary.createReportName(), Libary.getMethodName());
		}
	}

}
