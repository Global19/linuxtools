/*******************************************************************************
 * Copyright (c) 2008 Alexander Kurtakov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alexander Kurtakov - initial API and implementation
 *******************************************************************************/
package org.eclipse.linuxtools.rpm.rpmlint.resolutions;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.linuxtools.rpm.rpmlint.RpmlintLog;
import org.eclipse.linuxtools.rpm.ui.editor.SpecfileEditor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IMarkerResolution2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

/**
 * Defines the common functionality for a resolution which fix is only inserting line.
 *
 */
abstract public class AInsertLineResolution implements IMarkerResolution2 {

	protected int markerLine;
	
	/**
	 * Returns the line to be inserted for the fix.
	 * Note: If there are some whitespace requirements for this line(e.g. an empty line after it) 
	 * just append it to the real fix.
	 * 
	 * @return The entire line for the fix.
	 */
	public abstract String getLineToInsert();
	
	/**
	 * Returns the number of the line, which will succeed the inserted line.
	 * 
	 * @param editor The Specfile editor if it's needed for determining the corect place 
	 * @return 
	 */
	public abstract int getLineNumberForInsert(SpecfileEditor editor);

	/**
	 * Inserts an entire line at a given position as a resolution for a problem.
	 * 
	 * @see org.eclipse.ui.IMarkerResolution#run(org.eclipse.core.resources.IMarker)
	 */
	public void run(IMarker marker) {
		markerLine = marker.getAttribute(IMarker.LINE_NUMBER, 0);
		// Open or activate the editor.
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		IEditorPart part;
		try {
			part = IDE.openEditor(page, marker);
		} catch (PartInitException e) {
			RpmlintLog.logError(e);
			return;
		}
		// Get the editor's document.
		if (!(part instanceof SpecfileEditor)) {
			return;
		}
		SpecfileEditor editor = (SpecfileEditor) part;
		// Get the document
		IDocument doc = editor.getSpecfileSourceViewer().getDocument();
		
		try {
			int index = doc.getLineOffset(getLineNumberForInsert(editor));
			doc.replace(index, 0, getLineToInsert());
		} catch (BadLocationException e) {
			RpmlintLog.logError(e);
		}
	}

}
