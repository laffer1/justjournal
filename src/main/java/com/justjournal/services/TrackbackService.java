package com.justjournal.services;

import com.justjournal.model.Trackback;
import com.justjournal.model.api.TrackbackTo;
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
import org.thymeleaf.util.StringUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final TrackbackRepository trackbackRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public TrackbackService(final TrackbackRepository trackbackRepository) {
        this.trackbackRepository = trackbackRepository;
    }

    public boolean send(String pingUrl, String blogName, String permalink, String title, String excerpt) {

        try {
            final String cleanTitle = ESAPI.encoder().encodeForURL(title);
            final String cleanPermanentBlogEntryUrl = ESAPI.encoder().encodeForURL(permalink);
            final String cleanBlogName = ESAPI.encoder().encodeForURL(blogName);
            final String cleanExcerpt = ESAPI.encoder().encodeForURL(excerpt);

            final URI uri = new URI(pingUrl + "?title=" + cleanTitle + "&url=" + cleanPermanentBlogEntryUrl
                    + "&blog_name=" + cleanBlogName + "&excerpt=" + cleanExcerpt);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(Collections.singletonList(MediaType.TEXT_XML));
            headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
            HttpEntity<String> entity = new HttpEntity<>(null, headers);
            ResponseEntity<String> result = restTemplate.postForEntity(uri, entity, String.class);

            return result.getBody() != null && result.getBody().contains(ERROR + "0" + END_ERROR);
        } catch (final Exception me) {
            log.error("Failed to perform trackback ping", me);
            return false;
        }

    }

    public TrackbackTo save(Trackback tb) {
        final java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
        tb.setDate(now);
        return trackbackRepository.save(tb).toTrackbackTo();
    }

    protected List<Trackback> findByEntry(int entryId) {
        return trackbackRepository.findByEntryIdOrderByDate(entryId);
    }

    public List<TrackbackTo> getByEntry(int entryId) {
        return findByEntry(entryId).stream().map(Trackback::toTrackbackTo)
                .collect(Collectors.toList());
    }

    public Optional<TrackbackTo> getById(int trackbackId) {
        return trackbackRepository.findById(trackbackId).map(Trackback::toTrackbackTo);
    }

    public void deleteById(int trackbackId) {
        trackbackRepository.deleteById(trackbackId);
    }

    /**
     * A trackback response compatible with the trackback protocol.
     * @param errorCode
     * @param message
     * @return
     */
    public String generateResponse(final int errorCode, final String message) {
        final StringBuilder sb = new StringBuilder();
        sb.append(XML_HEADER);
        sb.append(RESPONSE);
        sb.append(ERROR);
        sb.append(errorCode);
        sb.append(END_ERROR);
        if (!StringUtils.isEmpty(message)) {
            sb.append(MESSAGE);
            sb.append(message);
            sb.append(END_MESSAGE);
        }
        sb.append(END_RESPONSE);
        return sb.toString();
    }
}
