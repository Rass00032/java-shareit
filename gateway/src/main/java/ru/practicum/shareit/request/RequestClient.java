package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.Map;

@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getRequests(long userId, Integer from, Integer size) {
        Map<String, Object> parameters = getParametersMap(from, size);
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getOwnerRequests(long userId, Integer from, Integer size) {
        Map<String, Object> parameters = getParametersMap(from, size);
        return get("/owner?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getRequest(long userId, Long requestId) {
        return get("/" + requestId, userId);
    }

    public ResponseEntity<Object> createRequest(Long userId, RequestDto requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> getRequesterAll(Long userId, int from, int size) {
        return get("/all?from={from}&size={size}", userId, getParametersMap(from, size));
    }

    private static Map<String, Object> getParametersMap(Integer from, Integer size) {
        return Map.of(
                "from", from,
                "size", size
        );
    }
}
