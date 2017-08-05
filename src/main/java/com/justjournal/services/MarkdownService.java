package com.justjournal.services;

import lombok.extern.slf4j.Slf4j;
import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.text.TextContentRenderer;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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
        final HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions()).build();
        return renderer.render(parse(markdown));
    }

    public String convertToText(final String markdown) {
        final TextContentRenderer renderer = TextContentRenderer.builder().extensions(extensions()).build();
        return renderer.render(parse(markdown));
    }

    public Node parse(final String markdown) {
        final Parser parser = Parser.builder().extensions(extensions()).build();
        return parser.parse(markdown);
    }

    public List<Extension> extensions() {
        List<Extension> extensions = Arrays.asList(TablesExtension.create(), AutolinkExtension.create());
        return extensions;
    }
}
