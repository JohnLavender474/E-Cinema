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
     * Populates the {@link Model} with a List<List<T>> instance and maps it to nameOfLists.
     *
     * @param <T>         the type parameter
     * @param model       the model
     * @param nameOfLists the name of the list of lists
     * @param sizePerList the size per list
     * @param iterable    the iterable
     */
    public static <T> void addListOfListsAttribute(Model model, String nameOfLists, int sizePerList,
                                                   Iterable<T> iterable) {
        List<List<T>> listOfLists = convertToListOfList(sizePerList, iterable);
        model.addAttribute(nameOfLists, listOfLists);
    }

    /**
     * Convert to list of lists.
     *
     * @param <T>         the type parameter
     * @param sizePerList the size per list
     * @param iterable    the iterable
     * @return the list
     */
    public static <T> List<List<T>> convertToListOfList(int sizePerList, Iterable<T> iterable) {
        List<List<T>> listOfLists = new ArrayList<>();
        List<T> list = new ArrayList<>();
        int i = 0;
        for (T t : iterable) {
            list.add(t);
            if (i < sizePerList - 1) {
                i++;
            } else {
                logger.info("Adding temp list to list of lists: " + list);
                listOfLists.add(new ArrayList<>(list));
                list.clear();
                i = 0;
            }
            logger.info("T: " + t);
            logger.info("i: " + i);
        }
        if (!list.isEmpty()) {
            listOfLists.add(list);
        }
        logger.info("List of lists: " + listOfLists);
        return listOfLists;
    }

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
