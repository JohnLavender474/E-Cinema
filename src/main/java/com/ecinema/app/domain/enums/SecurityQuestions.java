package com.ecinema.app.domain.enums;

import java.util.List;

/**
 * The type Security questions.
 */
public class SecurityQuestions {

    /**
     * The constant SQ1.
     */
    public static final String SQ1 = "What is the name of the first pet you've ever had?";

    /**
     * The constant SQ2.
     */
    public static final String SQ2 = "What city was your mother born in?";

    /**
     * The constant SQ3.
     */
    public static final String SQ3 = "What was your nickname when you were a kid?";

    /**
     * The constant SQ4.
     */
    public static final String SQ4 = "What is your favorite movie?";

    /**
     * The constant SQ5.
     */
    public static final String SQ5 = "What is your favorite soft drink?";

    /**
     * Gets list.
     *
     * @return the list
     */
    public static List<String> getList() {
        return List.of(SQ1, SQ2, SQ3, SQ4, SQ5);
    }

}
