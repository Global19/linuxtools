/*******************************************************************************
 * Copyright (c) 2008, 2018 Phil Muldoon and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Phil Muldoon <pkmuldoon@picobot.org> - initial API and implementation.
 *******************************************************************************/
package org.eclipse.linuxtools.internal.systemtap.ui.ide.editors.stp;


import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.ui.PlatformUI;

public class STPElementScanner extends BufferedRuleBasedScanner {

    private String[] keywordList = { "probe", "for", "else", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
            "foreach", "exit", "printf", "in", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
            "return", "break", "global", "next", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
            "while", "if", "delete", "#include", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
            "function", "do", "print", "error", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
            "log", "printd", "printdln", "println", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
			"sprint", "sprintf", "system", "warn", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"continue", "try", "catch" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    /**
     *
     * Build Element scanner for Syntax Highlighting for Systemtap Editor
     *
     */
    public STPElementScanner() {
		ColorRegistry colorRegistry = PlatformUI.getWorkbench().getThemeManager().getCurrentTheme().getColorRegistry();
		IToken defaultToken = new Token(new TextAttribute(colorRegistry.get(STPColorConstants.DEFAULT)));

		IToken keywordToken = new Token(new TextAttribute(colorRegistry.get(STPColorConstants.KEYWORD), null, SWT.BOLD));

		IToken commentToken = new Token(new TextAttribute(colorRegistry.get(STPColorConstants.COMMENT)));

		IToken stringToken = new Token(new TextAttribute(colorRegistry.get(STPColorConstants.STRING)));


        // Build keyword scanner
        WordRule keywordsRule = new WordRule(new IWordDetector() {

            @Override
            public boolean isWordStart(char c) {
                // probe kernel.function("schedule") is a valid name in
                // Systemtap, but we do not want to highlight the function
                // here as a keyword. Same with foo.return and so on.
                if (c == '.') {
                    return true;
                }

                return Character.isJavaIdentifierStart(c);
            }

            @Override
            public boolean isWordPart(char c) {
                // Set isWordStart for . rule.
                if (c == '.') {
                    return true;
                }

                return  Character.isJavaIdentifierPart(c);
            }

        }, defaultToken, true);

        for (int i=0; i<keywordList.length; i++)
            keywordsRule.addWord(keywordList[i], keywordToken);

        setRules(new IRule[] { new MultiLineRule("/*", "*/", commentToken), //$NON-NLS-1$//$NON-NLS-2$
                new EndOfLineRule("/*", commentToken), //$NON-NLS-1$
                new EndOfLineRule("#", commentToken), //$NON-NLS-1$
                new EndOfLineRule("//", commentToken), //$NON-NLS-1$
                new EndOfLineRule("#if", defaultToken), //$NON-NLS-1$
                new EndOfLineRule("#else", defaultToken), //$NON-NLS-1$
                new EndOfLineRule("#endif", defaultToken), //$NON-NLS-1$
                new EndOfLineRule("#define", defaultToken), //$NON-NLS-1$
                new SingleLineRule("\"", "\"", stringToken, '\\'), //$NON-NLS-1$ //$NON-NLS-2$
                new SingleLineRule("'", "'", stringToken, '\\'), //$NON-NLS-1$//$NON-NLS-2$
                keywordsRule, new WhitespaceRule(c -> Character.isWhitespace(c)), });
    }
}
