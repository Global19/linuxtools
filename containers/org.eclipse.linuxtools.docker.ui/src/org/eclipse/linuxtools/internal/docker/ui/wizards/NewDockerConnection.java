/*******************************************************************************
 * Copyright (c) 2014, 2018 Red Hat.
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - Initial Contribution
 *******************************************************************************/

package org.eclipse.linuxtools.internal.docker.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.linuxtools.docker.core.DockerConnectionManager;
import org.eclipse.linuxtools.docker.core.IDockerConnection;

/**
 * Wizard to add a Docker connection
 * 
 * @author xcoulon
 *
 */
public class NewDockerConnection extends Wizard {
	
	private NewDockerConnectionPage wizardPage;
	private IDockerConnection dockerConnection;
	
	/**
	 * Constructor.
	 */
	public NewDockerConnection() {
		super();
		setNeedsProgressMonitor(true);
		setWindowTitle(WizardMessages.getString("NewDockerConnection.title")); //$NON-NLS-1$
	}

	@Override
	public void addPages() {
		wizardPage = new NewDockerConnectionPage();
		addPage(wizardPage);
	}

	@Override
	public boolean performFinish() {
		dockerConnection = wizardPage.getDockerConnection();
		// add the connection and notify the listeners, so that the Docker
		// Explorer view can set the selection on
		// this new entry
		DockerConnectionManager.getInstance().addConnection(dockerConnection,
				true);
		return true;
	}
	
	/**
	 * @return the {@link IDockerConnection} that was configured
	 */
	public IDockerConnection getDockerConnection() {
		return dockerConnection;
	}
}
