# Command Line

## General

MarkdownDoc can be run using `java -jar markdowndoc-cmd-line-n.n[.n]-exec.jar`. If you just run it
without any arguments you get the following:

    Usage: java -jar markdowndoc-cmd-line-n.n[.n].exec.jar <generator> --help
           or
           java -jar markdowndoc-cmd-line-n.n[.n].exec.jar <generator> <fileSpec> --<generator option> ...
           or
           java -jar markdowndoc-cmd-line-n.n[.n].exec.jar <generator> <fileSpec> parserOptions:<parserOptions> —<generator option> ...
           or
           java -jar markdowndoc-cmd-line-n.n[.n].exec.jar <path to a .mddoc file>

The last usage example requires an _.mddoc_ file. See _’The _mddoc_ file type’_ (section 5) for more information on this file type.

What the generator options are depends on the specified generator.

The markdowndoc-cmd-line-n.n[.n]-exec.jar is a jar generated to contain all dependencies in the same jar,
making it easy to execute with java -jar.

__generator__

This should be either _pdf_, _html_, or _md_.

__fileSpec__

This is a comma separated list of paths relative to the current directory. The filename
part of the path can contain regular expressions and the directory part of the path can
specify .../**/... to mean any levels of subdirectories. 

Example: root/**/docs/.*.md

__parserOptions__

These are in the format ”option=value,...,option=value” with no spaces. Currently there is only one options for the JavadocParser: _markdownJavadoc=true_. When this is specified and java files are part of the input fileSpec then class and method texts are passed to the MarkdownParser instead of being added as text. This can be used in conjunction with a markdown doclet for javadoc.

__generatorOptions__

These depends on the generator being run. 

## PDF Generator

__--resultFile text (Required)__
    Where to write the result.

__--pageSize text__
    The pagesize name like LETTER or A4.

__--title text__
    The title of the document

__--subject text__
    The subject of the document. 

__--keywords text__
    Meta keywords 

__--author text__
    The author of the document. 

__--version text__
    The version to put on the title page. Must be specified 
    to be rendered! 

__--copyright text__
    The copyright message to put on the title page. Must be 
    specified to be rendered! 

__--hideLinks true/false__
    If true then links are not rendered as link the link text 
    will be rendered as plain text. 

__--unorderedListItemPrefix text__
    What item marking to use for unuredered lists. Default is 
    '- '. 

__--firstLineParagraphIndent true/false__
    If true then the first line of each paragraph is indented. 
    Default is false. 

__--backgroundColor text__
    The background color of the document in "R:G:B" format where 
    each R, G, and B are number 0 - 255. 

__--blockQuoteColor text__
    The blockquote color to use in this document in "R:G:B" 
    format where each R, G, and B are number 0 - 255. 

__--codeColor text__
    The code color to use in this document in "R:G:B" format 
    where each R, G, and B are number 0 - 255. 

__--generateTOC true/false__
    This generates table of contents. Default is false! 

__--generateTitlePage true/false__
    This will generate one first page with title, version, author, 
    and copyright. Default is false. 


## HTML Generator

__--resultFile text (Required)__
    Where to write the result. 

__--inlineCSS true/false__
    If true then the css will be included in the generated HTML.     

__--css text__
    The path to CSS file. 
 
__—makeFileLinksRelativeTo__
    ”path[+prefix_]”.  This affects links and images. When specified the resulting file: URLs in the result will be relative to the path specified by ”path” if the absulute path of the URL starts with the specified path. If a plus sign (+) and a prefix path is specified it will be prefixed to the final URL.  
 
## Markdown Generator

__--resultFile text (Required)__
    Where to write the result. 

__—makeFileLinksRelativeTo__
    ”path[+prefix_]”.  This affects links and images. When specified the resulting file: URLs in the result will be relative to the path specified by ”path” if the absulute path of the URL starts with the specified path. If a plus sign (+) and a prefix path is specified it will be prefixed to the final URL.  
 

