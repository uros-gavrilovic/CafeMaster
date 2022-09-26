package backend_package;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;

import lib.Libary;
import lib.themes.DarkTheme;
import lib.themes.LightTheme;

public class Theme {
	
	private String themeName;
	private String accentColor;
	
	// ----------------------------------------------------------------------
	
	public Theme() {
		this.themeName = Configuration.readConfigAttribute(new File(Libary.getConfigPath()), "theme");
		File themeFile = new File(Libary.getLibPath() + "/themes/" + themeName + ".properties"); 
		
		this.accentColor = Configuration.readConfigAttribute(themeFile, "@accentColor");
	}
	public String toString() {
		return "----- THEME -----" + "\n" +
				"themeName: " + themeName + "\n" +
				"accentColor: " + accentColor + "\n" +
				"----------------";
	}
	
	// ----------------------------------------------------------------------

	public static void createEmptyTheme(String themeName) {
		if(themeName == null || themeName.isEmpty()) {
			System.err.println("ERROR: Theme name is NOT VALID.");
			return;
		}
				
		File themeFile = new File(Libary.getThemesPath() + "//" + themeName + ".properties");
		if(!themeFile.exists()) {
			try {
				if(themeFile.createNewFile()) System.out.println("File \"CafeMaster\\lib\\themes\\" + themeName + ".properties\" is created!");
					
				FileWriter fileWriter = new FileWriter(themeFile);
				PrintWriter printWriter = new PrintWriter(fileWriter);
				printWriter.println("@accentColor = \\#FF0000");
				printWriter.println("@baseTheme = light");
				
				printWriter.close();
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    
		}
	}
	public void setActiveTheme(String themeName) {
		// TODO: Add abillity to let users implement their own themes.
		switch(themeName) {
			default:
			case "lightTheme":
				LightTheme.setup();
				break;
			case "darkTheme":
				DarkTheme.setup();
				break;
		}
		
//		setCorrespondingAccentColorToActiveTheme(); // BUG: Kad se ukljuci, on ne prebaci boju za temu ali ako se restartuje program on i dalje cuva sve tacno za svaku temu odvojeno
		Configuration.updateAttribute(new File(Libary.getConfigPath()), "theme", themeName, true, true);
		this.themeName = themeName;
	}
	public void setCorrespondingAccentColorToActiveTheme(){
		// Each theme has it's own theme color.
		
		File themeFile = new File(Libary.getThemesPath() + "\\" + getThemeName() + ".properties");
		String accentColor = Configuration.readConfigAttribute(themeFile, "accentColor");
		if(accentColor == null || accentColor.isBlank()) accentColor = "#FF0000";
		
		setAccentColor(accentColor);
	}
	public static void createDefaultThemes() {
		File lightThemeFile = new File(Libary.getThemesPath() + "//lightTheme" + ".properties");
		File darkThemeFile = new File(Libary.getThemesPath() + "//darkTheme" + ".properties");
		
		if(!lightThemeFile.exists()) {
			try {
				if(lightThemeFile.createNewFile()) System.out.println("File \"CafeMaster\\lib\\themes\\lightTheme.properties\" is created!");
					
				FileWriter fileWriter = new FileWriter(lightThemeFile);
				PrintWriter printWriter = new PrintWriter(fileWriter);
				printWriter.println("@accentColor = \\#FF0000");
				printWriter.println("@baseTheme = light");
				
				printWriter.close();
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(!darkThemeFile.exists()) {
			try {
				if(darkThemeFile.createNewFile()) System.out.println("File \"CafeMaster\\lib\\themes\\darkTheme.properties\" is created!");
					
				FileWriter fileWriter = new FileWriter(darkThemeFile);
				PrintWriter printWriter = new PrintWriter(fileWriter);
				printWriter.println("@accentColor = \\#0000FF");
				printWriter.println("@baseTheme = dark");
				
				printWriter.close();
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// ----------------------------------------------------------------------
	
	public String getThemeName() {
		return themeName;
	}
	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}
	public String getAccentColor() {
		return accentColor;
	}
	public void setAccentColor(String accentColor) {
		this.accentColor = accentColor;
		FlatLaf.setGlobalExtraDefaults( Collections.singletonMap( "@accentColor", this.getAccentColor()) );
		FlatIntelliJLaf.setup();
		Configuration.updateAttribute(new File(Libary.getThemesPath() + "\\" + getThemeName() + ".properties"), "@accentColor", accentColor, true, true);
		
	}
	
	
}
