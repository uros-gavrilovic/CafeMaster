package lib;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputFilter.Config;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Properties;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import backend_package.*;
import java.text.SimpleDateFormat;
public class Libary {
	
	public static String getUsername() {
		return System.getProperty("user.name");
	}
	public static String getDirectoryPath() {
		return System.getProperty("user.home") + "\\Documents\\CafeMaster";
	}
	public static String getConfigPath() {
		return getDirectoryPath() + "\\config.properties";
		
	}
	public static String getInventoryPath() {
		return getDirectoryPath() + "\\inventory.xlsx";
	}
	public static String getUsersPath() {
		return getDirectoryPath() + "\\users.txt";
	}
	public static String getLibPath() {
		return getDirectoryPath() + "\\lib";
	}
	public static String getThemesPath() {
		return getLibPath() + "\\themes";
	}
	public static String getReportsPath() {
		
		
		
		return getDirectoryPath() + "\\reports";
	}
	
	
	public static void errorException(String methodName) {
		System.err.println("GRESKA: Exception! (" + methodName + ")");
	}
	public static void errorIOException(String fileName, String methodName) {
		System.err.println("GRESKA: Nije moguce upisivati u fajl \"" + fileName + "\"! " + "(" + methodName +")");
	}
	public static void errorFileNotFoundException(String fileName, String methodName) {
		System.err.println("GRESKA: Nije moguce pristupiti fajlu \"" + fileName + "\"! " + "(" + methodName +")");
	}
	public static void errorNumberFormatException(String varName, String typeName, String methodName) {
		System.err.println("GRESKA: Nije moguce formatirati podatak \"" + varName + "\" u tip \"" + typeName + "\"! " + "(" + methodName + ")");
	}
	public static void errorInterruptedException(String methodName){
		System.err.println("GRESKA: Greska pri zaustavljanju niti. (" + methodName + ")");
	}	
	public static String getMethodName(){                   
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}
	
	
	public static LinkedList<Product> loadAllProductsOfCategory(String categoryName, LinkedList<String> categories, LinkedList<Product> products){
		if(categoryName == null || categoryName.isEmpty() == true) {
			System.err.println("GRESKA: Uneta kategorija NIJE validna! \"loadAllProductsOfCategory\"");
		}
		
		LinkedList<Product> productsOfCategory = new LinkedList<>();
		Iterator<Product> it = products.iterator();
		
		while(it.hasNext()) {
			Product iteratorProduct = it.next();
			if(iteratorProduct.getCategory().matches(categoryName)) productsOfCategory.add(iteratorProduct);
		}
		
		return productsOfCategory;
	}
	public static LinkedList<Product> loadAllProducts(){
		LinkedList<Product> products = new LinkedList<>();;
	
		try {
			FileInputStream fis = new FileInputStream(getInventoryPath());
			XSSFWorkbook workbook = new XSSFWorkbook(getInventoryPath());
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			int rows = sheet.getLastRowNum();
			
			for(int i=1; i<=rows; i++) {
				XSSFRow row = sheet.getRow(i);
				
				String category = row.getCell(1).getStringCellValue().toString();
				String name = row.getCell(2).getStringCellValue().toString();
				Double price = row.getCell(3).getNumericCellValue();
				Double expenses = row.getCell(4).getNumericCellValue();
				
				Product Product = new Product(category, name, price, expenses);
				products.add(Product);
			}
			fis.close();
			workbook.close();
		} catch (FileNotFoundException e) {
			errorFileNotFoundException("inventory.xlsx", "loadAllProducts");
		} catch (IOException e) {
			errorFileNotFoundException("inventory.xlsx", "loadAllProducts");
		}
		return products;
	}
	public static LinkedList<String> loadAllCategories(){
		LinkedList<String> categories = new LinkedList<>();
		String stringOfCategories = Configuration.readConfigAttribute(new File(getConfigPath()), "categories");
		
		if(stringOfCategories == null) { // attribute of categories property is empty
			return categories;
		}
		
		String[] arrayOfCategories = stringOfCategories.split(", ");
		
		for(int i=0; i<arrayOfCategories.length; i++) {
			categories.add(arrayOfCategories[i]);
		}
		
		return categories;
		
	}
	public static int loadNumberOfTables() {
		return Integer.parseInt(Configuration.readConfigAttribute(new File(getConfigPath()), "numOfTables")) ;
	}
	public static LinkedList<User> loadAllUsers(){
		LinkedList<User> users = new LinkedList<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(getUsersPath()));
			String readUser;
			User user;
			
			while((readUser = br.readLine()) != null) {
				user = stringToUser(readUser);
				users.add(user);
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			errorFileNotFoundException("users.txt", "loadAllUsers");
		} catch (IOException e) {
			errorIOException("users.txt", "loadAllUsers");
		}
		
		if(users.isEmpty()) {
			System.err.println("Nije moguce procitati korisnike! (" + getMethodName() + ")");
			return null;
		}
		return users;
	}
	public static Properties loadAllProperties(){
		File configFile = new File(getConfigPath());
		Properties props = new Properties();
		
		try {
			FileInputStream file = new FileInputStream(configFile);
			InputStreamReader in;
			in = new InputStreamReader(file, "UTF-8");
		    props.load(in);
		} catch (IOException e) {
			errorIOException(configFile.getName(), getMethodName());
		}
	    
	    return props;
	}
	
	public static void updateInventory(LinkedList<String> categories, LinkedList<Product> products) {
		insertAllCategories(categories);
		insertAllProducts(products);
	}
	
	public static void removeCategory(String categoryName, LinkedList<String> categories, LinkedList<Product> products) {
		if(categoryName == null) return;
		
		LinkedList<Product> productsOfCategory = loadAllProductsOfCategory(categoryName, categories, products);
		
		if(!productsOfCategory.isEmpty()) {
			Iterator<Product> it = products.iterator();
			int i = 0;
			LinkedList<Integer> indicesToDelete = new LinkedList<>();
			while(it.hasNext()) {
				Product productIterator = it.next();
				if(productIterator.getCategory().compareTo(categoryName) == 0) {
					indicesToDelete.add(i);
				}
				i++;
			}
			
			Iterator<Integer> iterator = indicesToDelete.iterator();
			while(iterator.hasNext()) {
				int indexIterator = iterator.next();
				System.out.println("Deleting item \"" + products.get(indexIterator).toString() + "\" at index: " + indexIterator);
				products.remove(indexIterator);
			}
			
		}
		
		categories.remove(categoryName);		
		
//		insertAllCategories(categories);
//		insertAllProducts(products);
	}
	public static void removeProduct(String productName, LinkedList<String> categories, LinkedList<Product> products) {
		if(productName == null) return;
		
		Iterator<Product> it = products.iterator();
		int i = 0;
		int indexToDelete = Integer.MIN_VALUE;
		
		while(it.hasNext()) {
			Product productIterator = it.next();
			if(productIterator.toString().matches(productName)) {
				indexToDelete = i;
				break;
			}
			i++;
		}
		
		if(indexToDelete != Integer.MIN_VALUE) {
			System.out.println("Deleting item: " + products.get(indexToDelete).toString() + " at index: " + indexToDelete);
			products.remove(indexToDelete);
		}
		
		
	}
	
	public static void insertProduct(Product productToInsert) {
		LinkedList<Product> products = loadAllProducts();
		Iterator<Product> it = products.iterator();
		int index = 0; // index na kom se ubacuje novi element, ubacujemo na poslednji index date kategorije
		boolean categoryFound = false; // koristi se da pokaze da li da ubacimo na kraj kategorije, ili ako je nema da ubacimo na kraj liste
		Product productIteratorHelper;
		
		while(it.hasNext()) {	
			Product productIterator = it.next();
			index++;
			
			if(categoryFound == true && !productIterator.getCategory().matches(productToInsert.getCategory())) {
				break; // ako sledeci proizvod ne pripada toj kategoriji, stavice na kraj kategorije koje pripada
			}
			if(productIterator.getCategory().matches(productToInsert.getCategory())) {
				categoryFound = true;
			}
		}
		
		if(categoryFound == true) {
			products.add(index, productToInsert);
		} else {
			products.add(productToInsert);
		}
		if(insertAllProducts(products) == true) {
			System.out.println("Proizvod \"" + productToInsert + "\" je uspesno upisan!");
		} else {
			System.err.println("GRESKA: Proizvod \"" + productToInsert + "\" nije upisan! (insertProduct)");
		}
	}
	public static boolean insertAllProducts(LinkedList<Product> products) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("INVENTORY");
			
			  	CellStyle headerCellStyle = sheet.getWorkbook().createCellStyle();
			  	headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
			  	headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			  	//headerCellStyle.setFont(new Font("Times New Roman", Font.PLAIN, 12));
					  	
			int columns = 5; // redni broj, kategorija, naziv, cena, brojPonavljanja
			String[] header = {"R.B.", "KATEGORIJA", "NAZIV", "CENA", "TROŠAK"};
			XSSFRow headerRow = sheet.createRow(0);
			for(int j=0; j<columns; j++) {
				XSSFCell cell = headerRow.createCell(j);
				cell.setCellValue(header[j]);
				cell.setCellStyle(headerCellStyle);
				sheet.setColumnWidth(j, 5000);
				//sheet.autoSizeColumn(i);
			}
			

			Iterator<Product> it = products.iterator();
			for(int i=1; i<= products.size(); i++) {
				XSSFRow row = sheet.createRow(i);
				Product productIterator = it.next();
				
				row.createCell(0).setCellValue(i);
				row.createCell(1).setCellValue(productIterator.getCategory());
				row.createCell(2).setCellValue(productIterator.getName());
				row.createCell(3).setCellValue(productIterator.getPrice());
				row.createCell(4).setCellValue(productIterator.getExpenses());
			}
			
			FileOutputStream fos = new FileOutputStream(Libary.getInventoryPath());
			workbook.write(fos);
			fos.close();
			workbook.close();
		} catch (IOException e) {
			errorIOException("inventory.xlsx", "insertAllProducts");
		}
		return true;
	}
	public static LinkedList<String> insertCategory(String categoryToInsert, LinkedList<String> categories) {
		// Adds a new category to already existing list of categories alphabetically.
		Iterator<String> it = categories.iterator();
		String categoryIterator;
		int index = 0;
		
		while(it.hasNext()) {
			categoryIterator = it.next();
			if(categoryToInsert.compareTo(categoryIterator) > 0) {
				index++;
			}
		}
		
		categories.add(index, categoryToInsert);
		System.out.println("Category \"" + categoryToInsert + "\" has been successfully added.");
		return categories;
	}
	public static void insertAllCategories(LinkedList<String> categories) {
		File configFile = new File(getConfigPath());
		Configuration.updateAttribute(configFile, "categories", linkedListToString(categories), true, true);
	}
	public static boolean checkForCategoryDuplicates(String newCategory) {
		LinkedList<String> categories = loadAllCategories();
		Iterator<String> it = categories.iterator();
		String categoryIterator;
		
		while(it.hasNext()) {
			categoryIterator = it.next();
			if(newCategory.equals(categoryIterator)) return true;
		}
		
		return false;
	}
	public static boolean checkForProductDuplicates(Product newProduct) {
		LinkedList<Product> products = loadAllProducts();
		Iterator<Product> it = products.iterator();
		Product productIterator;
		
		while(it.hasNext()) {
			productIterator = it.next();
			
//			System.out.println("Comparing \"" + newProduct + "\" to \"" + productIterator + "\".\t\tRESULT: " + newProduct.equals(productIterator));
			
			if(newProduct.equals(productIterator)) return true;
		}
		
		return false;
	}
	
	public static boolean createEmptyInventory() {
		try {
			File inventory = new File(Libary.getInventoryPath());
//			if(inventory.createNewFile()) System.out.println("Fajl \"inventory.xlsx\" je napravljen!");

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("INVENTORY");
			
			CellStyle headerCellStyle = sheet.getWorkbook().createCellStyle();
			headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
			headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//			headerCellStyle.setFont(new Font("Times New Roman", Font.PLAIN, 12));
					  	
			int columns = 5; // redni broj, kategorija, naziv, cena, brojPonavljanja
			String[] header = {"R.B.", "KATEGORIJA", "NAZIV", "CENA", "TROŠAK"};
			XSSFRow headerRow = sheet.createRow(0);
			for(int i=0; i<columns; i++) {
				XSSFCell cell = headerRow.createCell(i);
				cell.setCellValue(header[i]);
				cell.setCellStyle(headerCellStyle);
				sheet.setColumnWidth(i, 5000);
				//sheet.autoSizeColumn(i);
			}
			FileOutputStream fos = new FileOutputStream(Libary.getInventoryPath());
			workbook.write(fos);
			fos.close();
			workbook.close();
			return true;
		} catch (IOException e) {
			Libary.errorIOException("inventory.xlsx", "createEmptyInventory");
		}
		return false;
	}
	public static String createReportName() {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		return sdf.format(now.getTime()) + ".xlsx";
	}

	
	public static Product stringToProduct(String string) {
		String[] productParts = string.split("-");
		return new Product(productParts[0], productParts[1], Double.parseDouble(productParts[2]), Double.parseDouble(productParts[3]));
	}
	public static String[] productToArray(Product product) {
		String[] productParts = new String[4];
		productParts[0] = product.getCategory();
		productParts[1] = product.getName();
			// BUG! Moguci bug pri lokalizaciji Excela za nase podrucje, tj. ne prepoznaje . kao zamenu za , pri ispisu decimalnih mesta.
			// Zbog toga zameniti po potrebi!
		productParts[2] = Double.toString(product.getPrice()); // ako je ukljucen, US Standard
		//deloviProducta[2] = Double.toString(Product.getCena()).replace(".", ","); // ako je ukljucen, Srpski format
		productParts[3] = Integer.toString(product.getQuantity());
		
		return productParts;
	}
	public static User stringToUser(String readUser) {
		String[] userParts = readUser.split("-");
		return new User(Integer.parseInt(userParts[0]), userParts[1], userParts[2], Boolean.parseBoolean(userParts[3]), Double.parseDouble(userParts[4]));
	}	

	public static Product returnProductFromList(LinkedList<Product> products, Product product) {
		Iterator<Product> it = products.iterator();
		while(it.hasNext()) {
			Product iteratorniProduct = it.next();
			if(iteratorniProduct.equals(product)) return iteratorniProduct;
		}
		return null;
	}
	public static String linkedListToString(LinkedList<String> list) {
		// used to return list elements without [ and ].
		// .toString returns [el1, el2, ...]
		
		StringBuilder str = new StringBuilder();
		ListIterator<String> it = list.listIterator();
		while(it.hasNext()) {
			if(str.toString().isEmpty()) {
				str.append(it.next());
			} else {
				str.append(", " + it.next());
			}
		}
		
		return str.toString();
	}
	public static boolean isValidDouble(String s) {
		if(s == null || s.isEmpty()) return false;
		
		try {
			Double.parseDouble(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public static boolean isValidInteger(String string) {
		try{
			Integer.parseInt(string);
			return true;
		} catch(NumberFormatException e){
			errorNumberFormatException(string, "Integer", "isValidInteger");
			return false;
		}
	}
	public static boolean isNumber(char c) {
		if(c >= 48 && c <= 57) return true;
		return false;
	}
	public static boolean compareProducts(Product productIterator, Product product) {
		return productIterator.equals(product);
	}

	public static Color lighter(Color color, float ratio) {
        return mergeColors(Color.WHITE, ratio, color, 1 - ratio);
    }
	public static Color darker(Color color, float ratio) {
		return mergeColors(Color.BLACK, ratio, color, 1 - ratio);
	}
	public static Color mergeColors(Color a, float fa, Color b, float fb) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        return new Color((fa * a.getRed() + fb * b.getRed()) / (fa + fb) / 255f,
                (fa * a.getGreen() + fb * b.getGreen()) / (fa + fb) / 255f,
                (fa * a.getBlue() + fb * b.getBlue()) / (fa + fb) / 255f);
    }
	
}