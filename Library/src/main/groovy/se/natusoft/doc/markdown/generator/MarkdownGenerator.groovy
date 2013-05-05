/* 
 * 
 * PROJECT
 *     Name
 *         MarkdownDoc Library
 *     
 *     Code Version
 *         1.2.6
 *     
 *     Description
 *         Parses markdown and generates HTML and PDF.
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
 *         2012-11-16: Created!
 *         
 */
package se.natusoft.doc.markdown.generator

import se.natusoft.doc.markdown.api.Generator
import se.natusoft.doc.markdown.api.Options
import se.natusoft.doc.markdown.exception.GenerateException
import se.natusoft.doc.markdown.generator.options.MarkdownGeneratorOptions
import se.natusoft.doc.markdown.model.*

/**
 * This is a generator that generates Markdown from a document model.
 */
class MarkdownGenerator implements Generator {
    //
    // Private Members
    //

    private MarkdownGeneratorOptions options

    private File rootDir

    //
    // Methods
    //

    /**
     * Returns the options class required for this generator.
     */
    @Override
    public Class getOptionsClass() {
        return MarkdownGeneratorOptions.class
    }

    /**
     * @return The name of this generator.
     */
    @Override
    String getName() {
        return "md"
    }

    /**
     * The main API for the generator. This does the job!
     *
     * @param document The document model to generate from.
     * @param opts The options.
     * @param rootDir The optional root to prefix condfigured path with.
     */
    @Override
    public void generate(Doc document, Options opts, File rootDir) throws IOException, GenerateException {
        this.options = (MarkdownGeneratorOptions)opts
        this.rootDir = rootDir

        def writer
        if (rootDir != null) {
            writer = new FileWriter(rootDir.path + File.separator + this.options.resultFile)
        }
        else {
            writer = new FileWriter(this.options.resultFile)
        }
        try {
            doGenerate(document, this.options, writer)
        }
        finally {
            writer.close()
        }
    }

    /**
     * The main API for the generator. This does the job!
     *
     * @param document The document model to generate from.
     * @param options The options.
     */
    private void doGenerate(Doc document, MarkdownGeneratorOptions options, Writer writer) throws IOException, GenerateException {

        PrintWriter pw = new PrintWriter(writer)

        for (DocItem docItem : document.items) {
            switch (docItem.format) {
                case DocFormat.Comment:
                    pw.println("<!--")
                    pw.println("  " + ((Comment)docItem).text)
                    pw.println("-->")
                    break

                case DocFormat.Paragraph:
                    writeParagraph((Paragraph)docItem, pw)
                    break

                case DocFormat.Header:
                    writeHeader((Header)docItem, pw)
                    break

                case DocFormat.BlockQuote:
                    writeBlockQuote((BlockQuote)docItem, pw)
                    break;

                case DocFormat.CodeBlock:
                    writeCodeBlock((CodeBlock)docItem, pw)
                    break

                case DocFormat.HorizontalRule:
                    writeHorizontalRule((HorizontalRule)docItem, pw)
                    break

                case DocFormat.List:
                    writeList((List)docItem, pw)
                    break

                default:
                    throw new GenerateException(message: "Unknown format model in Doc! [" + docItem.getClass().getName() + "]")
            }
        }
    }

    private void writeHeader(Header header, PrintWriter pw) {
        for (int i = 0; i < header.level.level; i++) {
            pw.print("#")
        }
        pw.print(" " + header.text)
        pw.println()
        pw.println()
    }

    private void writeBlockQuote(BlockQuote blockQuote, PrintWriter pw) {
        pw.print("> ")
        writeParagraph(blockQuote, pw)
    }

    private void writeCodeBlock(CodeBlock codeBlock, PrintWriter pw) {
        for (DocItem item : codeBlock.items) {
            pw.print("    " + item.toString())
            pw.println()
        }
        pw.println()
    }

    private void writeHorizontalRule(HorizontalRule horizontalRule, PrintWriter pw) {
        pw.println("----")
        pw.println()
    }

    private void writeList(List list, PrintWriter pw) {
        writeList(list, pw, "")
    }

    private void writeList(List list, PrintWriter pw, String indent) {
        int itemNo = 1;

        list.items.each { li ->
            if (li instanceof List) {
                writeList((List)li, pw, indent + "   ")
            }
            else {
                if (list.ordered) {
                    pw.print(indent)
                    pw.print("" + itemNo++ + ". ")
                }
                else {
                    pw.print(indent + "* ")
                }
                li.items.each { pg ->
                    writeParagraph((Paragraph)pg, pw)
                }
            }
        }
    }

    private void writeParagraph(Paragraph paragraph, PrintWriter pw) throws GenerateException {
        boolean first = true
        for (DocItem docItem : paragraph.items) {
            if (docItem.renderPrefixedSpace && !first) {
                pw.print(" ")
            }
            first = false

            switch (docItem.format) {

                case DocFormat.Code:
                    writeCode((Code)docItem, pw)
                    break

                case DocFormat.Emphasis:
                    writeEmphasis((Emphasis)docItem, pw)
                    break

                case DocFormat.Strong:
                    writeStrong((Strong)docItem, pw)
                    break

                case DocFormat.Image:
                    writeImage((Image)docItem, pw)
                    break

                case DocFormat.Link:
                    writeLink((Link)docItem, pw)
                    break
                case DocFormat.AutoLink:
                    writeLink((AutoLink)docItem, pw)
                    break

                case DocFormat.Space:
                    pw.print("&nbsp;")
                    break;

                case DocFormat.PlainText:
                    pw.print(((PlainText)docItem).text)
                    break

                default:
                    throw new GenerateException(message: "Unknown format model in Doc! [" + docItem.getClass().getName() + "]")
            }
        }
        pw.println()
        pw.println()
    }

    private void writeCode(Code code, PrintWriter pw) {
        pw.print("`" + code.text.trim() + "`")
    }

    private void writeEmphasis(Emphasis emphasis, PrintWriter pw) {
        pw.print("_" + emphasis.text.trim() + "_")
    }

    private void writeStrong(Strong strong, PrintWriter pw) {
        pw.print("__" + strong.text.trim() + "__")
    }

    private void writeImage(Image image, PrintWriter pw) {
        pw.print("![" + image.text + "](" + resolveUrl(image.url, image.parseFile))
        if (image.title != null && image.title.trim().length() > 0) {
            pw.print(" " + image.title)
        }
        pw.print(")")
    }

    private void writeLink(Link link, PrintWriter pw) {
        pw.print("[" + link.text + "](" + link.url)
        if (link.title != null && link.title.trim().length() > 0) {
            pw.print(" " + link.title)
        }
        pw.print(")")
    }

    /**
     * - Adds file: if no protocol is specified.
     * - If file: then resolved to full path if not found with relative path.
     *
     * @param url The DocItem item provided url.
     * @param parseFile The source file of the DocItem item.
     */
    private String resolveUrl(String url, File parseFile) {
        String resolvedUrl = url
        if (!resolvedUrl.startsWith("file:") && !resolvedUrl.startsWith("http:")) {
            resolvedUrl = "file:" + resolvedUrl
        }
        if (resolvedUrl.startsWith("file:")) {
            String path = resolvedUrl.substring(5)
            File testFile = new File(path)

            if (!testFile.exists()) {
                // Try relative to parseFile first.
                int ix = parseFile.canonicalPath.lastIndexOf(File.separator)
                if (ix >= 0) {
                    String path1 = parseFile.canonicalPath.substring(0, ix + 1) + path
                    if (this.rootDir != null) {
                        // The result file is relative to the root dir!
                        resolvedUrl = "file:" + possiblyMakeRelative(this.rootDir.canonicalPath + File.separator + path1)
                        testFile = new File(this.rootDir.canonicalPath + File.separator + path1)
                    }
                    else {
                        resolvedUrl = "file:" + possiblyMakeRelative(path1)
                        testFile = new File(path1)
                    }
                }
                if (!testFile.exists()) {
                    // Try relative to result file.
                    ix = this.options.resultFile.lastIndexOf(File.separator)
                    if (ix >= 0) {
                        String path2 = this.options.resultFile.substring(0, ix + 1) + path
                        if (this.rootDir != null) {
                            // The result file is relative to the root dir!
                            resolvedUrl = "file:" + possiblyMakeRelative(this.rootDir.canonicalPath + File.separator + path2)
                        }
                        else {
                            resolvedUrl = "file:" + possiblyMakeRelative(path2)
                        }
                    }
                }
            }
        }

        return resolvedUrl
    }

    /**
     * Checks of a relative path is wanted and if so checks if the specified path can be made relative to the configured
     * relative path. If so it is made relative.
     *
     * @param path The original path to check and convert.
     *
     * @return A possibly relative path.
     */
    private String possiblyMakeRelative(String path) {
        String resultPath = path

        if (this.options.makeFileLinksRelativeTo != null && this.options.makeFileLinksRelativeTo.trim().length() > 0) {
            String[] relativeToParts = this.options.makeFileLinksRelativeTo.split("\\+")
            File relFilePath = new File(relativeToParts[0])
            String expandedRelativePath = relFilePath.canonicalPath
            if (resultPath.startsWith(expandedRelativePath)) {
                resultPath = resultPath.substring(expandedRelativePath.length() + 1)
                if (relativeToParts.length > 1) {
                    resultPath = relativeToParts[1] + resultPath
                }
            }
        }

        return resultPath
    }

}
