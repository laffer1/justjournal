package com.justjournal.services;

import lombok.extern.slf4j.Slf4j;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.text.TextContentRenderer;
import org.springframework.stereotype.Service;

/**
 * Create a simple service to generate HTML from markdown code.
 *
 * @author Lucas Holt
 */
@Slf4j
@Service
public class MarkdownService {

    // TODO: figure out extensions we want https://github.com/atlassian/commonmark-java
    
    public String convertToHtml(final String markdown) {
        final HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(parse(markdown));
    }

    public String convertToText(final String markdown) {
        final TextContentRenderer renderer = TextContentRenderer.builder().build();
        return renderer.render(parse(markdown));
    }

    public Node parse(final String markdown) {
        final Parser parser = Parser.builder().build();
        return parser.parse(markdown);
    }
}
