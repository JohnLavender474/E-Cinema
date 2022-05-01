package com.ecinema.app;

import org.springframework.core.io.Resource;
import reactor.core.publisher.Mono;

/**
 * The interface Video service.
 */
public interface VideoService {

    /**
     * Gets video.
     *
     * @param src the src
     * @return the video
     */
    Mono<Resource> getVideo(String src);

}
