package com.ecinema.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Util methods for {@link org.springframework.stereotype.Controller} classes.
 */
public class ControllerUtils {

    private static final Logger logger = LoggerFactory.getLogger(ControllerUtils.class);

    /**
     * Add page numbers attribute to {@link Model}.
     *
     * @param model the model
     * @param page  the page
     */
    public static void addPageNumbersAttribute(Model model, Page<?> page) {
        int totalPages = page.getTotalPages();
        logger.info("Total pages: " + totalPages);
        model.addAttribute("totalPages", totalPages);
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                                                 .boxed().collect(Collectors.toList());
            logger.info("Pages: " + pageNumbers);
            model.addAttribute("pageNumbers", pageNumbers);
        }
    }

}
