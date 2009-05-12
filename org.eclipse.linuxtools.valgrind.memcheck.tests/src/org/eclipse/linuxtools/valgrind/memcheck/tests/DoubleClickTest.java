/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Elliott Baron <ebaron@redhat.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.linuxtools.valgrind.memcheck.tests;

import java.io.File;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.linuxtools.valgrind.core.IValgrindMessage;
import org.eclipse.linuxtools.valgrind.core.ValgrindStackFrame;
import org.eclipse.linuxtools.valgrind.ui.CoreMessagesViewer;
import org.eclipse.linuxtools.valgrind.ui.ValgrindUIPlugin;
import org.eclipse.linuxtools.valgrind.ui.ValgrindViewPart;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

public class DoubleClickTest extends AbstractMemcheckTest {
	private ValgrindStackFrame frame;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		proj = createProjectAndBuild("basicTest"); //$NON-NLS-1$
	}

	private void doDoubleClick() throws Exception {
		ValgrindViewPart view = ValgrindUIPlugin.getDefault().getView();
		CoreMessagesViewer viewer = view.getMessagesViewer();

		// get first leaf
		IValgrindMessage[] elements = (IValgrindMessage[]) viewer.getInput();
		IValgrindMessage element = elements[0];
		TreePath path = new TreePath(new Object[] { element });
		frame = null;
		while (element.getChildren().length > 0) {
			element = element.getChildren()[0];
			path = path.createChildPath(element);
			if (element instanceof ValgrindStackFrame) {
				frame = (ValgrindStackFrame) element;
			}
		}
		assertNotNull(frame);

		viewer.expandToLevel(frame, TreeViewer.ALL_LEVELS);
		TreeSelection selection = new TreeSelection(path);

		// do double click
		IDoubleClickListener listener = viewer.getDoubleClickListener();
		listener.doubleClick(new DoubleClickEvent(viewer, selection));
	}

	@Override
	protected void tearDown() throws Exception {
		deleteProject(proj);
		super.tearDown();
	}

	public void testDoubleClickFile() throws Exception {
		ILaunchConfiguration config = createConfiguration(proj.getProject());
		doLaunch(config, "testDoubleClickFile"); //$NON-NLS-1$

		doDoubleClick();
		
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		IEditorInput input = editor.getEditorInput();
		if (input instanceof IFileEditorInput) {
			IFileEditorInput fileInput = (IFileEditorInput) input;
			File expectedFile = new File(proj.getProject().getLocation().toOSString(), frame.getFile());
			File actualFile = fileInput.getFile().getLocation().toFile();
			
			assertEquals(expectedFile.getCanonicalPath(), actualFile.getCanonicalPath());
		}
		else {
			fail();
		}
	}
	
	public void testDoubleClickLine() throws Exception {
		ILaunchConfiguration config = createConfiguration(proj.getProject());
		doLaunch(config, "testDoubleClickLine"); //$NON-NLS-1$

		doDoubleClick();
		
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (editor instanceof ITextEditor) {
			ITextEditor textEditor = (ITextEditor) editor;
			
			ISelection selection = textEditor.getSelectionProvider().getSelection();
			if (selection instanceof TextSelection) {
				TextSelection textSelection = (TextSelection) selection;
				int line = textSelection.getStartLine() + 1; // zero-indexed
				
				assertEquals(frame.getLine(), line);
			}
			else {
				fail();
			}
		}
		else {
			fail();
		}
	}
}
