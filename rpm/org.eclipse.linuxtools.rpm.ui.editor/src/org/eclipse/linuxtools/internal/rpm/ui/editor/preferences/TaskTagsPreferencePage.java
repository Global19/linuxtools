/*******************************************************************************
 * Copyright (c) 2008, 2018 Red Hat, Inc.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.linuxtools.internal.rpm.ui.editor.preferences;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.linuxtools.internal.rpm.ui.editor.Activator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class TaskTagsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public TaskTagsPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected void createFieldEditors() {
		addField(new TasksListEditor(PreferenceConstants.P_TASK_TAGS, Messages.TaskTagsPreferencePage_0,
				getFieldEditorParent()));
	}

	static class TasksListEditor extends ListEditor {

		public TasksListEditor(String name, String labelText, Composite parent) {
			super(name, labelText, parent);
		}

		@Override
		protected String createList(String[] items) {
			StringBuilder itemsString = new StringBuilder();
			for (String item : items) {
				itemsString.append(item).append(';');
			}
			return itemsString.toString();
		}

		@Override
		protected String getNewInputObject() {
			String result = null;
			// open an input dialog so that the user can enter a new task tag
			InputDialog inputDialog = new InputDialog(getShell(), Messages.TaskTagsPreferencePage_1,
					Messages.TaskTagsPreferencePage_2, "", null); //$NON-NLS-1$
			int returnCode = inputDialog.open();

			if (returnCode == Window.OK) {
				result = inputDialog.getValue();
			}

			return result;

		}

		@Override
		protected String[] parseString(String stringList) {
			return stringList.split(";"); //$NON-NLS-1$
		}

	}

}
