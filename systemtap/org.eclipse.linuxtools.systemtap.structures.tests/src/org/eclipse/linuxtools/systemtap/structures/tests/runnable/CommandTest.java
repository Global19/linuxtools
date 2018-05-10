/*******************************************************************************
 * Copyright (c) 2006, 2018 IBM Corporation and ohters.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - Jeff Briggs, Henry Hughes, Ryan Morse
 *******************************************************************************/

package org.eclipse.linuxtools.systemtap.structures.tests.runnable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.linuxtools.systemtap.structures.runnable.Command;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class CommandTest {

    @Before
    public void setUp() {
        tc = new Command(new String[] {"ls", "/home/"});
    }

    @Test
    public void testCommand() {
        assertNotNull("Command not null", tc);

        tc.dispose();
        tc = new Command(null);
        assertNotNull("Command not null", tc);

        tc.dispose();
        tc = new Command(new String[] {});
        assertNotNull("Command not null", tc);

        tc.dispose();
        tc = new Command(new String[] {""});
        assertNotNull("Command not null", tc);

        tc.dispose();
        tc = new Command(new String[] {"a"});
        assertNotNull("Command not null", tc);

        tc.dispose();
        tc = new Command(new String[] {"ls", "/"});
        assertNotNull("Command not null", tc);
    }

    @Test
    public void testIsFinished() {
        assertTrue("Not finished", tc.isRunning());
        tc.stop();
        assertFalse("Finished", tc.isRunning());
    }

    @Test
    public void testGetReturnValue() {
        assertEquals(Integer.MAX_VALUE, tc.getReturnValue());
    }

    @Test
    public void testLoggedCommand() throws CoreException {
        tc.dispose();

        tc = new Command(new String[] {"ls", "/doesnotexist/"});
        tc.start();
        assertTrue(tc.isRunning());
        assertFalse(tc.isDisposed());
        tc.stop();
        assertFalse(tc.isRunning());
        assertFalse(tc.isDisposed());
        tc.dispose();

        tc = new Command(new String[] {"ls", "/doesnotexist/"});
        tc.start();
        assertTrue(tc.isRunning());
        assertFalse(tc.isDisposed());
        tc.stop();
        assertFalse(tc.isRunning());
        assertFalse(tc.isDisposed());
        tc.dispose();
    }

    @Test
    public void testStop() throws CoreException {
        tc.start();
        assertTrue(tc.isRunning());
        tc.stop();
        assertFalse(tc.isRunning());
    }

    @Test
    public void testDispose() {
        assertFalse(tc.isDisposed());
        tc.dispose();
        assertTrue(tc.isDisposed());
    }

    @After
    public void tearDown() {
        tc.dispose();
        assertTrue(tc.isDisposed());
    }

    Command tc;
}

