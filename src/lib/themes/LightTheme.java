package lib.themes;

import com.formdev.flatlaf.FlatLightLaf;


public class LightTheme
	extends FlatLightLaf
{
	public static final String NAME = "lightTheme";

	public static boolean setup() {
		return setup( new LightTheme() );
	}

	public static void installLafInfo() {
		installLafInfo( NAME, LightTheme.class );
	}

	@Override
	public String getName() {
		return NAME;
	}
}
