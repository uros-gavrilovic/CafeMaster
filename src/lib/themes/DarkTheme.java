package lib.themes;

import com.formdev.flatlaf.FlatDarkLaf;

public class DarkTheme
	extends FlatDarkLaf
{
	public static final String NAME = "darkTheme";

	public static boolean setup() {
		return setup( new DarkTheme() );
	}

	public static void installLafInfo() {
		installLafInfo( NAME, DarkTheme.class );
	}

	@Override
	public String getName() {
		return NAME;
	}
}
