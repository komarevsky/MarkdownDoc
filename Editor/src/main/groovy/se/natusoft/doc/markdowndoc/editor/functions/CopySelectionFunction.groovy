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
package se.natusoft.doc.markdowndoc.editor.functions

import groovy.transform.CompileStatic
import se.natusoft.doc.markdowndoc.editor.api.Editor
import se.natusoft.doc.markdowndoc.editor.api.EditorFunction
import se.natusoft.doc.markdowndoc.editor.config.KeyboardKey
import se.natusoft.doc.markdowndoc.editor.exceptions.FunctionException

import javax.swing.*

/**
 * This provides a function that copies the currently selected text.
 */
@CompileStatic
public class CopySelectionFunction implements EditorFunction {
    //
    // Private Members
    //

    Editor editor

    //
    // Methods
    //

    @Override
    public String getGroup() {
        return null
    }

    @Override
    public String getName() {
        return "Copy"
    }

    @Override
    public JComponent getToolBarButton() {
        return null
    }

    @Override
    public KeyboardKey getKeyboardShortcut() {
        return new KeyboardKey("Meta+C")
    }

    @Override
    public void perform() throws FunctionException {
        this.editor.copy()
    }

    /**
     * Cleanup and unregister any configs.
     */
    public void close() {}
}
