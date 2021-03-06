/* 
 * 
 * PROJECT
 *     Name
 *         MarkdownDocEditor
 *     
 *     Code Version
 *         1.3.5
 *     
 *     Description
 *         An editor that supports editing markdown with formatting preview.
 *         
 * COPYRIGHTS
 *     Copyright (C) 2012 by Natusoft AB All rights reserved.
 *     
 * LICENSE
 *     Apache 2.0 (Open Source)
 *     
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *     
 *       http://www.apache.org/licenses/LICENSE-2.0
 *     
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *     
 * AUTHORS
 *     Tommy Svensson (tommy@natusoft.se)
 *         Changes:
 *         2013-05-27: Created!
 *         
 */
package se.natusoft.doc.markdowndoc.editor.filters

import groovy.transform.CompileStatic
import se.natusoft.doc.markdowndoc.editor.api.*
import se.natusoft.doc.markdowndoc.editor.config.BooleanConfigEntry
import se.natusoft.doc.markdowndoc.editor.config.ConfigChanged
import se.natusoft.doc.markdowndoc.editor.config.ConfigEntry

import javax.swing.text.BadLocationException
import java.awt.event.KeyEvent

import static se.natusoft.doc.markdowndoc.editor.config.Constants.CONFIG_GROUP_EDITING

/**
 * This filter provides context help in the editorPane.
 */
@CompileStatic
public class ContextFormatFilter implements EditorInputFilter, Configurable {
    //
    // Private Members
    //

    private Editor editor

    //
    // Config
    //

    /** True for double spaced bullets. */
    private boolean doubleSpacedBullets = false

    /** Config entry used in SettingsWindow to edit config. */
    private static BooleanConfigEntry doubleSpacedBulletsConfig =
            new BooleanConfigEntry("editor.contextformatfilter", "Double space bullets", false, CONFIG_GROUP_EDITING)

    /**
     * Configuration callback for markdown formatting while editing.
     */
    private Closure doubleSpacedBulletsConfigChanged = { ConfigEntry ce ->
        this.doubleSpacedBullets = Boolean.valueOf(ce.getValue())
    }

    /**
     * Register configurations.
     *
     * @param configProvider The config provider to register with.
     */
    @Override
    public void registerConfigs(ConfigProvider configProvider) {
        configProvider.registerConfig(doubleSpacedBulletsConfig, doubleSpacedBulletsConfigChanged)
    }

    /**
     * Unregister configurations.
     *
     * @param configProvider The config provider to unregister with.
     */
    @Override
    public void unregisterConfigs(ConfigProvider configProvider) {
        configProvider.unregisterConfig(doubleSpacedBulletsConfig, doubleSpacedBulletsConfigChanged)
    }

    //
    // Methods
    //

    @Override
    public void setEditor(Editor editor) {
        this.editor = editor
    }

    private boolean isBulletChar(char bulletChar) {
        return bulletChar == '+' || bulletChar == '-' || bulletChar == '*'
    }

    private boolean isBulletChar(String bulletChar) {
        return bulletChar.length() > 0 && isBulletChar(bulletChar.charAt(0))
    }

    private boolean isBulletStart(String line) {
        return line.length() > 1 && isBulletChar(line.charAt(0)) && line.charAt(1) == ' '
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        try {
            // Catch new lines
            if (keyEvent.getKeyChar() == '\n') {
                Line currentLine = this.editor.getCurrentLine()

                if (currentLine != null) {
                    String trimmedLine = currentLine.getText().trim()

                    // -- Handle preformatted --

                    if ((currentLine.getText().length() > 0 && currentLine.getText().charAt(0) == '\t') ||
                            currentLine.getText().startsWith("    ")) {
                        if (currentLine.getText().charAt(0) == '\t') {
                            currentLine.getNextLine().setText("\t")
                            this.editor.moveCaretForward(1)
                        }
                        else {
                            currentLine.getNextLine().setText("    ")
                            this.editor.moveCaretForward(4)
                        }
                    }

                    // -- Handle list bullets --

                    // If the previous line only contains a list bullet and no text, blank the line.
                    else if (trimmedLine.length() == 1 && isBulletChar(trimmedLine)) {
                        currentLine.setText("")
                    }
                    // Otherwise start the new line with a new list bullet.
                    else if (isBulletStart(trimmedLine)) {
                        // Since the user just pressed return whatever was after the cursor will be on
                        // the next line. If return was pressed at the end of the line the next line
                        // will be empty (with the exception of possible whitespace). In this case we
                        // add a new bullet. If it is not empty we don't to anything.
                        if (currentLine.getNextLine().getText().trim().length() == 0) {
                            int indentPos = currentLine.getText().indexOf('*')
                            if (this.doubleSpacedBullets) {
                                this.editor.addBlankLine()
                                currentLine = currentLine.getNextLine()
                            }
                            StringBuilder newLine = new StringBuilder()
                            while (indentPos > 0) {
                                newLine.append(' ')
                                --indentPos
                            }
                            newLine.append(trimmedLine.substring(0, 2))
                            Line nextLine = currentLine.getNextLine()
                            if (nextLine != null) {
                                nextLine.setText(newLine.toString())
                                this.editor.moveCaretForward(newLine.length())
                            }
                        }
                    }

                    // -- Handle quotes --

                    // If the previous line only contains a quote (>) char and no text, blank the line
                    else if (trimmedLine.equals(">")) {
                        currentLine.setText("")
                    }
                    // Otherwise start the new line with a quote (>) character.
                    else if (trimmedLine.startsWith("> ")) {
                        int indentPos = currentLine.getText().indexOf('>')
                        StringBuilder newLine = new StringBuilder()
                        while (indentPos > 0) {
                            newLine.append(' ')
                            --indentPos
                        }
                        newLine.append("> ")
                        Line nextLine = currentLine.getNextLine()
                        if (nextLine != null) {
                            nextLine.setText(newLine.toString())
                            this.editor.moveCaretForward(newLine.length())
                        }
                    }
                }
            }
            else if (keyEvent.getKeyChar() == '\t') {
                Line line = this.editor.getCurrentLine()
                boolean isLastLine = line.isLastLine()
                if (keyEvent.isShiftDown()) {
                    // JEditorPane does something weird on shift-tab
                    if (isLastLine) {
                        line = line.getPreviousLine()
                    }
                }
                if (line.getText().trim().startsWith("*")) {
                    if (keyEvent.isShiftDown()) {
                        if (line.getText().startsWith("   ")) {
                            line.setText(line.getText().substring(3))
                            if (!isLastLine) {
                                this.editor.moveCaretBack(3)
                            }
                        }
                    }
                    else {
                        int startPos = line.getLineStartPost()
                        int caretLoc = this.editor.getCaretDot()
                        int tabIx = line.getText().indexOf("\t")
                        int moveChars = 3

                        if ((startPos + tabIx) <= caretLoc) {
                            moveChars = 2
                        }

                        line.setText("   " + line.getText().replace("\t", ""))
                        this.editor.moveCaretForward(moveChars)
                    }
                }
            }
        }
        catch (BadLocationException ble) {
            ble.printStackTrace(System.err)
        }

        this.editor.requestEditorFocus()
    }

    /**
     * Cleanup and unregister any configs.
     */
    public void close() {}

}
