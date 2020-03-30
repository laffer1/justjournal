package com.justjournal.services;

import com.justjournal.repository.TrackbackRepository;
import lombok.extern.slf4j.Slf4j;
import org.owasp.esapi.ESAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * @author Lucas Holt
 */
@Slf4j
@Service
public class TrackbackService {
    private static final String XML_HEADER =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    private static final String RESPONSE = "<response>";
    private static final String END_RESPONSE = "</response>";
    private static final String ERROR = "<error>";
    private static final String END_ERROR = "</error>";
    private static final String MESSAGE = "<message>";
    private static final String END_MESSAGE = "</message>";

    private final TrackbackRepository trackbackDao;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public TrackbackService(final TrackbackRepository trackbackDao) {
        this.trackbackDao = trackbackDao;
    }
    
    public boolean send(String pingUrl, String blogName, String permalink, String title, String excerpt) {

        try {
            final String cleanTitle = ESAPI.encoder().encodeForURL(title);
            final String cleanUrl = ESAPI.encoder().encodeForURL(pingUrl);
            final String cleanPermanentBlogEntryUrl = ESAPI.encoder().encodeForURL(permalink);
            final String cleanBlogName = ESAPI.encoder().encodeForURL(blogName);
            final String cleanExcerpt = ESAPI.encoder().encodeForURL(excerpt);

            final URI uri = new URI(cleanUrl + "?title=" + cleanTitle + "&url=" + cleanPermanentBlogEntryUrl
                    + "&blog_name=" + cleanBlogName + "&excerpt=" + cleanExcerpt);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(Collections.singletonList(MediaType.TEXT_XML));
            headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
            HttpEntity<String> entity = new HttpEntity<String>(null, headers);
            ResponseEntity<String> result = restTemplate.postForEntity(uri, entity, String.class);

            if (result.getBody() != null && result.getBody().contains(ERROR + "0" + END_ERROR))
                return true;
            else
                return false;
        } catch (final Exception me) {
            log.error("Failed to perform trackback ping", me);
            return false;
        }

    }
}
