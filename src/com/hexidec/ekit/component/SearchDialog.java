/*
GNU Lesser General Public License

SearchDialog
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

package com.hexidec.ekit.component;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.hexidec.util.Translatrix;

/** Class for providing a dialog that lets the user specify arguments for
  * the Search Find/Replace functions
  */
public class SearchDialog extends JDialog implements PropertyChangeListener
{
	/** <code>serialVersionUID</code> */
	private static final long	serialVersionUID	= 4401639514620015244L;
	private String inputFindTerm    = null;
	private String inputReplaceTerm = null;
	private boolean bCaseSensitive  = false;
	private boolean bStartAtTop     = false;
	private boolean bReplaceAll     = false;
	JOptionPane jOptionPane;

	private boolean isReplaceDialog;
	private JTextField jtxfFindTerm;
	private JTextField jtxfReplaceTerm;
	private JCheckBox  jchkCase;
	private JCheckBox  jchkTop;
	private JCheckBox  jchkAll;
	private Object[] buttonLabels;

	/**
	 * Creates and displays a FontSelectorDialog.
	 * @param parent the parent Frame or Dialog of the FontSelectorDialog
	 * @param title the dialog title
	 * @param bModal whether the dialog should be modal
	 * @param bIsReplace whether the dialog should replace found text in the document
	 * @param bCaseSetting whether the search should look for case
	 * @param bTopSetting whether the search should start at the top of the document
	 * @return the FontSelectorDialog instance
	 */
	public static SearchDialog show(Window parent, String title, boolean bModal, boolean bIsReplace, boolean bCaseSetting, boolean bTopSetting)
	{
		SearchDialog me;
		if (parent instanceof Frame)
		{
			me = new SearchDialog((Frame) parent, title, bModal, bIsReplace, bCaseSetting, bTopSetting);
		}
		else if (parent instanceof Dialog)
		{
			me = new SearchDialog((Dialog) parent, title, bModal, bIsReplace, bCaseSetting, bTopSetting);
			me.pack();
			me.show();
			me.jtxfFindTerm.requestFocus();
		}
		else
		{
			throw new IllegalArgumentException("parent must be a Frame or a Dialog");
		}
		return me;
	}

	private SearchDialog(Dialog parent, String title, boolean bModal, boolean bIsReplace, boolean bCaseSetting, boolean bTopSetting)
	{
		super(parent, title, bModal);
		init(bIsReplace, bCaseSetting, bTopSetting);
	}

	/**
	 * @deprecated use {@link #show(Window, String, boolean, boolean, boolean, boolean)} instead
	 */
	// TODO reduce visibility to private in next release
	public SearchDialog(Frame parent, String title, boolean bModal, boolean bIsReplace, boolean bCaseSetting, boolean bTopSetting)
	{
		super(parent, title, bModal);
		init(bIsReplace, bCaseSetting, bTopSetting);
		pack();
		show();
		jtxfFindTerm.requestFocus();
	}

	private void init(boolean bIsReplace, boolean bCaseSetting, boolean bTopSetting)
	{
		isReplaceDialog = bIsReplace;
		jtxfFindTerm    = new JTextField(3);
		jtxfReplaceTerm = new JTextField(3);
		jchkCase        = new JCheckBox(Translatrix.getTranslationString("SearchCaseSensitive"), bCaseSetting);
		jchkTop         = new JCheckBox(Translatrix.getTranslationString("SearchStartAtTop"), bTopSetting);
		jchkAll         = new JCheckBox(Translatrix.getTranslationString("SearchReplaceAll"), false);
		buttonLabels    = new Object[] { Translatrix.getTranslationString("DialogAccept"), Translatrix.getTranslationString("DialogCancel") };
		if (bIsReplace)
		{
			Object[] panelContents = {
				Translatrix.getTranslationString("SearchFind"),
				jtxfFindTerm,
				Translatrix.getTranslationString("SearchReplace"),
				jtxfReplaceTerm,
				jchkAll,
				jchkCase,
				jchkTop
			};
			jOptionPane = new JOptionPane(panelContents, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, buttonLabels, buttonLabels[0]);
		}
		else
		{
			Object[] panelContents = {
				Translatrix.getTranslationString("SearchFind"),
				jtxfFindTerm,
				jchkCase,
				jchkTop
			};
			jOptionPane = new JOptionPane(panelContents, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, buttonLabels, buttonLabels[0]);
		}
		setContentPane(jOptionPane);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we)
			{
				jOptionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
			}
		});

		jOptionPane.addPropertyChangeListener(this);
	}

	/** {@inheritDoc} */
	public void propertyChange(PropertyChangeEvent e)
	{
		String prop = e.getPropertyName();
		if (isVisible() 
			&& (e.getSource() == jOptionPane)
			&& (prop.equals(JOptionPane.VALUE_PROPERTY) || prop.equals(JOptionPane.INPUT_VALUE_PROPERTY)))
		{
			Object value = jOptionPane.getValue();
			if (value == JOptionPane.UNINITIALIZED_VALUE)
			{
				return;
			}
			jOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
			if (value.equals(buttonLabels[0]))
			{
				inputFindTerm  = jtxfFindTerm.getText();
				bCaseSensitive = jchkCase.isSelected();
				bStartAtTop    = jchkTop.isSelected();
				if (isReplaceDialog)
				{
					inputReplaceTerm = jtxfReplaceTerm.getText();
					bReplaceAll      = jchkAll.isSelected();
				}
				setVisible(false);
			}
			else
			{
				inputFindTerm    = null;
				inputReplaceTerm = null;
				bCaseSensitive   = false;
				bStartAtTop      = false;
				bReplaceAll      = false;
				setVisible(false);
			}
		}
	}

	public String  getFindTerm()      { return inputFindTerm; }
	public String  getReplaceTerm()   { return inputReplaceTerm; }
	public boolean getCaseSensitive() { return bCaseSensitive; }
	public boolean getStartAtTop()    { return bStartAtTop; }
	public boolean getReplaceAll()    { return bReplaceAll; }
}

