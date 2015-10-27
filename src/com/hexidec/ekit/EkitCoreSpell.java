/*
GNU Lesser General Public License

EkitCore - Base Java Swing HTML Editor & Viewer Class (Spellcheck Version)
Copyright (C) 2000 Howard Kistler

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package com.hexidec.ekit;

import java.net.URL;

import javax.swing.text.Document;
import javax.swing.text.StyledDocument;

import com.hexidec.util.Translatrix;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.event.DocumentWordTokenizer;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.swing.JSpellDialog;

/** EkitCoreSpell
  * Extended main application class with additional spellchecking feature
  *
  * REQUIREMENTS
  * Java 2 (JDK 1.3 or 1.4)
  * Swing Library
  *
  * @authors Howard Kistler, Yaodong Liu, Gyoergy Magoss, Oliver Moser, Michael Goldberg, Cecile Rostaing, Thomas
  *          Gauweiler, Frits Jalvingh, Jerry Pommer, Ruud Noordermeer, Mindaugas Idzelis, Raymond Penners, Steve
  *          Birmingham, Rafael Cieplinski, Nico Mack, Michael Pearce, Murray Altheim, Mattias Malmgren, Maciej
  *          Kubicki, Elisabeth Novotny, Christoph Wei&szlig;enborn
  * @version 1.4
  */

public class EkitCoreSpell extends EkitCore implements SpellCheckListener
{
	/** <code>serialVersionUID</code> */
	private static final long	serialVersionUID	= -2547940985905996124L;

	/* Spell Checker Settings */
	private static String dictFile;
	private SpellChecker spellCheck = null;
	private JSpellDialog spellDialog;

	/** Master Constructor
	  * @param sDocument         [String]  A text or HTML document to load in the editor upon startup.
	  * @param sStyleSheet       [String]  A CSS stylesheet to load in the editor upon startup.
	  * @param sRawDocument      [String]  A document encoded as a String to load in the editor upon startup.
	  * @param sdocSource        [StyledDocument] Optional document specification, using javax.swing.text.StyledDocument.
	  * @param urlStyleSheet     [URL]     A URL reference to the CSS style sheet.
	  * @param includeToolBar    [boolean] Specifies whether the app should include the toolbar.
	  * @param showViewSource    [boolean] Specifies whether or not to show the View Source window on startup.
	  * @param showMenuIcons     [boolean] Specifies whether or not to show icon pictures in menus.
	  * @param editModeExclusive [boolean] Specifies whether or not to use exclusive edit mode (recommended on).
	  * @param sLanguage         [String]  The language portion of the Internationalization Locale to run Ekit in.
	  * @param sCountry          [String]  The country portion of the Internationalization Locale to run Ekit in.
	  * @param base64            [boolean] Specifies whether the raw document is Base64 encoded or not.
	  * @param debugMode         [boolean] Specifies whether to show the Debug menu or not.
	  * @param multiBar          [boolean] Specifies whether to use multiple toolbars or one big toolbar.
	  */
	public EkitCoreSpell(String sDocument, String sStyleSheet, String sRawDocument, StyledDocument sdocSource, URL urlStyleSheet, boolean includeToolBar, boolean showViewSource, boolean showMenuIcons, boolean editModeExclusive, String sLanguage, String sCountry, boolean base64, boolean debugMode, boolean useSpellChecker, boolean multiBar, String toolbarSeq)
	{
		super(null, sDocument, sStyleSheet, sRawDocument, sdocSource, urlStyleSheet, includeToolBar, showViewSource, showMenuIcons, editModeExclusive, sLanguage, sCountry, base64, debugMode, true, multiBar, toolbarSeq);

		/* Create spell checker */
		try
		{
			dictFile = Translatrix.getTranslationString("DictionaryFile");
			SpellDictionary dictionary = new SpellDictionary(dictFile); // uses my custom loader in SpellDictionary
			spellCheck = new SpellChecker(dictionary);
			spellCheck.addSpellCheckListener(this);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		spellDialog = JSpellDialog.create(getParentWindow(), Translatrix.getTranslationString("ToolSpellcheckDialog"), true);
	}

	/** Raw/Base64 Document & Style Sheet URL Constructor (Ideal for EkitApplet)
	  * @param sRawDocument      [String]  A document encoded as a String to load in the editor upon startup.
	  * @param urlStyleSheet     [URL]     An URL for the stylesheet to load at startup.
	  * @param includeToolBar    [boolean] Specifies whether the app should include the toolbar.
	  * @param showViewSource    [boolean] Specifies whether or not to show the View Source window on startup.
	  * @param showMenuIcons     [boolean] Specifies whether or not to show icon pictures in menus.
	  * @param editModeExclusive [boolean] Specifies whether or not to use exclusive edit mode (recommended on).
	  * @param sLanguage         [String]  The language portion of the Internationalization Locale to run Ekit in.
	  * @param sCountry          [String]  The country portion of the Internationalization Locale to run Ekit in.
	  */
	public EkitCoreSpell(String sRawDocument, URL urlStyleSheet, boolean includeToolBar, boolean showViewSource, boolean showMenuIcons, boolean editModeExclusive, String sLanguage, String sCountry, boolean base64, boolean multiBar, String toolbarSeq)
	{
		this(null, null, sRawDocument, null, urlStyleSheet, includeToolBar, showViewSource, showMenuIcons, editModeExclusive, sLanguage, sCountry, base64, false, true, multiBar, toolbarSeq);
	}

	/** Empty Constructor
	  */
	public EkitCoreSpell()
	{
		this(null, null, null, null, null, true, false, true, true, null, null, false, false, true, false, EkitCore.TOOLBAR_DEFAULT_SINGLE);
	}

	/* SpellCheckListener methods */
	public void spellingError(SpellCheckEvent event)
	{
		spellDialog.show(event);
	}

	/* Spell checking method (overrides empty method in basic core) */
	public void checkDocumentSpelling(Document doc)
	{
		spellCheck.checkSpelling(new DocumentWordTokenizer(doc));
	}

}
