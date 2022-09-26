package backend_package;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Properties;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;

import lib.Libary;



public class Configuration {
	
	private String language;
	private String theme;
	private int numOfTables;
	private LinkedList<String> categories;
	// Add more fields if necessary.
	
	public Configuration(Properties props) {
		this.language = props.getProperty("language");
		this.theme = props.getProperty("theme");
		this.numOfTables = Integer.parseInt(props.getProperty("numOfTables"));
		this.categories = new LinkedList<>();
		
		String[] categoriesArray = props.getProperty("categories").split(", ");
		for(int i=0; i<categoriesArray.length; i++) {
			this.categories.add(categoriesArray[i]);
		}
	}
	
	// ----------------------------------------------------------------------	
	
	public String toString() {
		return "----- CONFIGURATION -----" + "\n" +
				"language: " + language + "\n" +
				"theme: " + theme + "\n" +
				"numOfTables: " + numOfTables + "\n" +
				"categories: " + categories + "\n" +
				"------------------------";
	}

	public static void createConfig() {
		File configFile = new File(Libary.getConfigPath());
		 
		try {
		    Properties props = new Properties();
		    props.setProperty("language", "Srpski"); // TODO: Implement language pack.
		    props.setProperty("theme", "lightTheme");
		    props.setProperty("numOfTables", "1");
		    props.setProperty("categories", "");
		    
		    FileOutputStream out = new FileOutputStream(configFile);
		    props.store(out, null);
		    out.close();
		    
		} catch (FileNotFoundException ex) {
			Libary.errorFileNotFoundException("config.properties", Libary.getMethodName());
		} catch (IOException ex) {
			Libary.errorIOException("config.properties", Libary.getMethodName());
		}
	}
	public static Properties readConfig(File configFile) {
			try {
				FileInputStream fileIn;
				fileIn = new FileInputStream(configFile);
				InputStreamReader in;
				in = new InputStreamReader(fileIn, "UTF-8");
				Properties props = new Properties();
			    props.load(in);
			    
			    in.close();
			    fileIn.close();
			    
			    return props;
			} catch (FileNotFoundException e) {
				Libary.errorFileNotFoundException(configFile.getName(), Libary.getMethodName());
			} catch (IOException e) {
				Libary.errorIOException(configFile.getName(), Libary.getMethodName());
			}
			
			return null;
	}
	public static String readConfigAttribute(File configFile, String propertyName) {
		if(checkIfAttributeIsPresent(configFile, propertyName) == false) {
			return null;
		}
		
		try {
			FileInputStream file = new FileInputStream(configFile);
			InputStreamReader in = new InputStreamReader(file, "UTF-8");
			Properties props = new Properties();
		    props.load(in);
		    
		    file.close();
		    in.close();
		    
		    return props.getProperty(propertyName);
		} catch (IOException e) {
			Libary.errorFileNotFoundException(configFile.getName(), Libary.getMethodName());
		}
		
		return null;
	}
	public static void updateAttribute(File configFile, String propertyName, String newValue, boolean overwrite, boolean createIfNotPresent) {
		try {
			FileInputStream fileIn = new FileInputStream(configFile);
			InputStreamReader in = new InputStreamReader(fileIn, "UTF-8");
		    Properties props = new Properties();
		    props.load(in);
		    
		    String currentPropertyValue = props.getProperty(propertyName);
		    
		    if(currentPropertyValue == null || currentPropertyValue.isEmpty()) {
		    	System.out.println("Property \"" + propertyName + "\" not found.");
		    	if(createIfNotPresent == true) {
		    		System.out.println("Creating new property \"" + propertyName + "\" with value \"" + newValue + "\".");

				    FileOutputStream fileOut = new FileOutputStream(configFile);
				    OutputStreamWriter out = new OutputStreamWriter(fileOut, "UTF-8");
				    props.setProperty(propertyName, newValue);
			     	props.store(out, null);

					fileOut.close();
					out.close();
		    	} else {
		    		return;
		    	}
		    } else {
		    	FileOutputStream fileOut = new FileOutputStream(configFile);
			    OutputStreamWriter out = new OutputStreamWriter(fileOut, "UTF-8");
			    
		    	if(overwrite == true) {
		    		System.out.println("Updating property \"" + propertyName + "\", value from \"" + currentPropertyValue + "\" to \"" + newValue + "\".");
		    		props.setProperty(propertyName, newValue);
		    	} else {
		    		String oldAttributes = props.getProperty(propertyName);
		    		System.out.println("Old attributes: " + oldAttributes);
		    		newValue = oldAttributes + ", " + newValue;
		    		props.setProperty(propertyName, newValue);
		    		System.out.println("Updating property \"" + propertyName + "\", value from \"" + currentPropertyValue + "\" to \"" + newValue + "\".");
		    	}
		    	props.store(out, null);
		    	
		    	fileOut.close();
	    		out.close();
		    }
		    		
		    fileIn.close();
		    in.close();
		} catch (FileNotFoundException ex) {
		    Libary.errorFileNotFoundException("config.properties", Libary.getMethodName());
		} catch (IOException ex) {
		    Libary.errorIOException("config.properties", Libary.getMethodName());
		}
	}
	public static boolean checkIfAttributeIsPresent(File configFile, String propertyName) {
		// returns false even if property doesn't exist
		try {
		    FileInputStream in = new FileInputStream(configFile);
		    Properties props = new Properties();
		    props.load(in);
		    in.close();
		    
		    String currentPropertyValue = props.getProperty(propertyName);
		    
		    if(currentPropertyValue == null) {
		    	System.out.println("Property \"" + propertyName + "\" not found.");
		    } else if (currentPropertyValue.isEmpty()) {
		    	System.out.println("Property \"" + propertyName + "\" has no defined value.");
		    } else {
		    	return true;
		    }
		} catch (FileNotFoundException ex) {
		    Libary.errorFileNotFoundException("config.properties", Libary.getMethodName());
		} catch (IOException ex) {
		    Libary.errorIOException("config.properties", Libary.getMethodName());
		}

		
		return false;
	}

	// ----------------------------------------------------------------------	
	
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		if(language == null || language.isEmpty()) {
			System.err.println("ERROR: Entered language is NOT VALID! (" + Libary.getMethodName() + ")");
		}
		
		File configFile = new File(Libary.getConfigPath());
		updateAttribute(configFile, "language", language, true, true);
		this.language = language;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		if(theme == null || theme.isEmpty()) {
			System.err.println("ERROR: Input theme is NOT VALID! (" + Libary.getMethodName() + ")");
			return;
		}
		
		File configFile = new File(Libary.getConfigPath());
		updateAttribute(configFile, "theme", theme, true, true);
		this.theme = theme;
	}	
	public int getNumOfTables() {
		return numOfTables;
	}
	public void setNumOfTables(int numOfTables) {
		if(numOfTables < 1 || numOfTables > 10) {
			System.err.println("ERROR: Entered number of tables is NOT VALID! (" + Libary.getMethodName() + ")");
			return;
		}
		
		File configFile = new File(Libary.getConfigPath());
		updateAttribute(configFile, "numOfTables", Integer.toString(numOfTables), true, true);
		this.numOfTables = numOfTables;
	}
	public LinkedList<String> getCategories() {
		return categories;
	}
	public void setCategories(LinkedList<String> categories) {
		File configFile = new File(Libary.getConfigPath());
		updateAttribute(configFile, "categories", Libary.linkedListToString(categories), true, true);
		this.categories = categories;
	}
	public void setAccentColor(String accentColorHex) {
		if(accentColorHex == null || accentColorHex.isEmpty()) {
			System.err.println("ERROR: Entered accent color is NOT VALID! (" + Libary.getMethodName() + ")");
		}
		
		String themeName = getTheme();
		File themeFile = new File("src/lib/themes/" + themeName + ".properties");
		updateAttribute(themeFile, "@accentColor", accentColorHex, true, true);
	}
}
