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
 *         2014-10-12: Created!
 *         
 */
package se.natusoft.doc.markdowndoc.editor.functions.export

import groovy.transform.CompileStatic

import javax.swing.JComponent
import javax.swing.JLabel
import java.awt.Color

/**
 * Base of all field values in PDFData
 */
@CompileStatic
public abstract class ExportDataValue {
    JLabel labelComp
    JComponent valueComp

    public ExportDataValue(String labelText) {
        this.labelComp = new JLabel("    " + labelText + " ")
    }

    public String getKey() {
        return this.labelComp.getText().trim().toLowerCase().replaceAll(" ", "-").replaceAll(":", "")
    }

    public void setBackgroundColor(Color bgColor) {
        this.valueComp.setBackground(bgColor)
    }

    public abstract String getValue()
    public abstract void setValue(String value)
}
