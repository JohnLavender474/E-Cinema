package com.ecinema.app.configs;

import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.entities.User;
import com.ecinema.app.domain.forms.MovieForm;
import com.ecinema.app.domain.forms.ShowroomForm;
import com.ecinema.app.services.EmailService;
import com.ecinema.app.services.MovieService;
import com.ecinema.app.services.ShowroomService;
import com.ecinema.app.services.UserService;
import com.ecinema.app.utils.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

/**
 * Initializes persistence on app start.
 */
@Component
@RequiredArgsConstructor
public class InitializationConfig {

    private final UserService userService;
    private final MovieService movieService;
    private final ShowroomService showroomService;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(InitializationConfig.class);

    /**
     * Called on app ready.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void appReady() {
        defineUsers();
        defineMovies();
        defineShowrooms();
    }

    private void defineUsers() {
        userService.deleteAll();
        // Root admin
        User root = new User();
        root.setUsername("ROOT");
        root.setEmail(emailService.getBusinessEmail());
        String encodedPassword = passwordEncoder.encode("password123!");
        root.setPassword(encodedPassword);
        root.setFirstName("Jim");
        root.setLastName("Montgomery");
        root.setBirthDate(LocalDate.of(1998, Month.JULY, 9));
        root.setSecurityQuestion1(SecurityQuestions.SQ1);
        String answer1 = "Bowser";
        String answer1Formatted = UtilMethods.removeWhitespace(answer1.toLowerCase());
        String encodedAnswer1 = passwordEncoder.encode(answer1Formatted);
        root.setSecurityAnswer1(encodedAnswer1);
        root.setSecurityQuestion2(SecurityQuestions.SQ5);
        String answer2 = "root beer";
        String answer2Formatted = UtilMethods.removeWhitespace(answer2.toLowerCase());
        String encodedAnswer2 = passwordEncoder.encode(answer2Formatted);
        root.setSecurityAnswer2(encodedAnswer2);
        root.setCreationDateTime(LocalDateTime.now());
        root.setLastActivityDateTime(LocalDateTime.now());
        root.setIsAccountEnabled(true);
        root.setIsAccountLocked(false);
        root.setIsAccountExpired(false);
        root.setIsCredentialsExpired(false);
        userService.save(root);
        userService.addUserRoleDefToUser(
                root, UserRole.ADMIN, UserRole.CUSTOMER, UserRole.MODERATOR);
    }

    private void defineMovies() {
        movieService.deleteAll();
        // Aliens
        MovieForm aliens = new MovieForm();
        aliens.setTitle("Aliens");
        aliens.setDirector("James Cameron");
        aliens.setImage("/img/posters/aliensPoster.jpeg");
        aliens.setTrailer("https://www.youtube.com/embed/oSeQQlaCZgU");
        aliens.setSynopsis("57 years after Ellen Ripley had a close encounter with the reptilian alien creature " +
                                   "from the first movie, she is called back, this time, to help a group of highly " +
                                   "trained colonial marines fight off against the sinister extraterrestrials. " +
                                   "But this time, the aliens have taken over a space colony on the moon LV-426. " +
                                   "When the colonial marines are called upon to search the deserted space " +
                                   "colony, they later find out that they are up against more than what they " +
                                   "bargained for. Using specially modified machine guns and enough firepower, " +
                                   "it's either fight or die as the space marines battle against the aliens. As the " +
                                   "Marines do their best to defend themselves, Ripley must attempt to protect a " +
                                   "young girl who is the sole survivor of the nearly wiped out space colony");
        aliens.setHours(2);
        aliens.setMinutes(17);
        aliens.setReleaseYear(1986);
        aliens.setReleaseDay(20);
        aliens.setReleaseMonth(Month.JULY);
        aliens.setMsrbRating(MsrbRating.R);
        aliens.setCast(new ArrayList<>() {{
            add("Sigourney Weaver");
            add("Michael Biehn");
            add("Carrie Henn");
        }});
        aliens.setWriters(new ArrayList<>() {{
            add("James Cameron");
            add("David Giler");
            add("Walter Hill");
        }});
        aliens.setMovieCategories(List.of(MovieCategory.ACTION,
                                          MovieCategory.ADVENTURE,
                                          MovieCategory.SCI_FI,
                                          MovieCategory.HORROR,
                                          MovieCategory.CLASSIC));
        movieService.submitMovieForm(aliens);
        // Darkest Hour
        MovieForm darkestHour = new MovieForm();
        darkestHour.setTitle("Darkest Hour");
        darkestHour.setDirector("Joe Wright");
        darkestHour.setImage("/img/posters/darkestHourPoster.jpeg");
        darkestHour.setTrailer("https://www.youtube.com/embed/LtJ60u7SUSw");
        darkestHour.setSynopsis("During World War II, as Adolf Hitler's powerful Wehrmacht rampages across Europe, " +
                                        "the Prime Minister of the United Kingdom, Neville Chamberlain, is forced to " +
                                        "resign, recommending Winston Churchill as his replacement. But even in his " +
                                        "early days as the country's leader, Churchill is under pressure to commence " +
                                        "peace negotiations with Hitler or to fight head-on the seemingly " +
                                        "invincible Nazi regime, whatever the cost. However difficult and dangerous " +
                                        "his decision may be, Churchill has no choice but to shine in the " +
                                        "country's darkest hour.");
        darkestHour.setHours(2);
        darkestHour.setMinutes(5);
        darkestHour.setReleaseYear(2017);
        darkestHour.setReleaseDay(26);
        darkestHour.setReleaseMonth(Month.NOVEMBER);
        darkestHour.setMsrbRating(MsrbRating.PG13);
        darkestHour.setCast(new ArrayList<>() {{
            add("Gary Oldman");
            add("Lily James");
            add("Kristin Scott Thomas");
        }});
        darkestHour.setWriters(new ArrayList<>() {{
            add("Anthony McCarten");
        }});
        darkestHour.setMovieCategories(List.of(MovieCategory.BIOGRAPHY,
                                               MovieCategory.DRAMA,
                                               MovieCategory.WAR));
        movieService.submitMovieForm(darkestHour);
        // Dune
        MovieForm dune = new MovieForm();
        dune.setTitle("Dune");
        dune.setDirector("Denis Villeneuve");
        dune.setImage("/img/posters/dunePoster.jpeg");
        dune.setTrailer("https://www.youtube.com/embed/8g18jFHCLXk");
        dune.setSynopsis("A mythic and emotionally charged hero's journey, \"Dune\" tells the story of Paul " +
                                 "Atreides, a brilliant and gifted young man born into a great destiny beyond his " +
                                 "understanding, who must travel to the most dangerous planet in the universe to " +
                                 "ensure the future of his family and his people. As malevolent forces explode into " +
                                 "conflict over the planet's exclusive supply of the most precious resource in " +
                                 "existence-a commodity capable of unlocking humanity's greatest potential-only " +
                                 "those who can conquer their fear will survive.");
        dune.setHours(2);
        dune.setMinutes(35);
        dune.setReleaseYear(2021);
        dune.setReleaseDay(22);
        dune.setReleaseMonth(Month.OCTOBER);
        dune.setMsrbRating(MsrbRating.PG13);
        dune.setCast(new ArrayList<>() {{
            add("Timothee Chalamet");
            add("Rebecca Ferguson");
            add("Zendaya");
        }});
        dune.setWriters(new ArrayList<>() {{
            add("Jon Spaihts");
            add("Denis Villeneuve");
            add("Eric Roth");
        }});
        dune.setMovieCategories(List.of(MovieCategory.SCI_FI,
                                        MovieCategory.DRAMA,
                                        MovieCategory.ACTION,
                                        MovieCategory.ADVENTURE));
        movieService.submitMovieForm(dune);
        // Empire Strikes Back
        MovieForm empireStrikesBack = new MovieForm();
        empireStrikesBack.setTitle("Star Wars: The Empire Strikes Back");
        empireStrikesBack.setImage("/img/posters/empireStrikesBackPoster.jpeg");
        empireStrikesBack.setTrailer("https://www.youtube.com/embed/JNwNXF9Y6kY");
        empireStrikesBack.setSynopsis("Luke Skywalker, Han Solo, Princess Leia and Chewbacca face attack by the " +
                                              "Imperial forces and its AT-AT walkers on the ice planet Hoth. While " +
                                              "Han and Leia escape in the Millennium Falcon, Luke travels to Dagobah " +
                                              "in search of Yoda. Only with the Jedi Master's help will Luke survive " +
                                              "when the Dark Side of the Force beckons him into the ultimate duel " +
                                              "with Darth Vader.");
        empireStrikesBack.setHours(2);
        empireStrikesBack.setMinutes(4);
        empireStrikesBack.setReleaseYear(1980);
        empireStrikesBack.setReleaseDay(25);
        empireStrikesBack.setReleaseMonth(Month.MAY);
        empireStrikesBack.setMsrbRating(MsrbRating.PG);
        empireStrikesBack.setCast(new ArrayList<>() {{
            add("Mark Hamill");
            add("Harrison Ford");
            add("Carrie Fisher");
            add("James Earl Jones");
        }});
        empireStrikesBack.setWriters(new ArrayList<>() {{
            add("Leigh Brackett");
            add("Lawrence Kasdan");
            add("George Lucas");
        }});
        empireStrikesBack.setDirector("Irvin Kershner");
        empireStrikesBack.setMovieCategories(List.of(MovieCategory.ACTION,
                                                     MovieCategory.ADVENTURE,
                                                     MovieCategory.FANTASY,
                                                     MovieCategory.CLASSIC));
        movieService.submitMovieForm(empireStrikesBack);
        // Interstellar
        MovieForm interstellar = new MovieForm();
        interstellar.setTitle("Interstellar");
        interstellar.setDirector("Christopher Nolan");
        interstellar.setImage("/img/posters/interstellarPoster.jpeg");
        interstellar.setTrailer("https://www.youtube.com/embed/2LqzF5WauAw");
        interstellar.setSynopsis("Earth's future has been riddled by disasters, famines, and droughts. There is " +
                                         "only one way to ensure mankind's survival: Interstellar travel. A newly " +
                                         "discovered wormhole in the far reaches of our solar system allows a team " +
                                         "of astronauts to go where no man has gone before, a planet that may have " +
                                         "the right environment to sustain human life.");
        interstellar.setHours(2);
        interstellar.setMinutes(49);
        interstellar.setReleaseYear(2014);
        interstellar.setReleaseMonth(Month.NOVEMBER);
        interstellar.setReleaseDay(9);
        interstellar.setMsrbRating(MsrbRating.PG13);
        interstellar.setCast(new ArrayList<>() {{
            add("Matthew McConaughey");
            add("Anne Hathaway");
            add("Jessica Chastain");
        }});
        interstellar.setWriters(new ArrayList<>() {{
            add("Jonathan Nolan");
            add("Christopher Nolan");
        }});
        interstellar.setMovieCategories(List.of(MovieCategory.ADVENTURE,
                                                MovieCategory.DRAMA,
                                                MovieCategory.SCI_FI));
        movieService.submitMovieForm(interstellar);
        // 007: No Time to Die
        MovieForm noTimeToDie = new MovieForm();
        noTimeToDie.setTitle("007: No Time To Die");
        noTimeToDie.setImage("/img/posters/noTimeToDiePoster.jpeg");
        noTimeToDie.setTrailer("https://www.youtube.com/embed/N_gD9-Oa0fg");
        noTimeToDie.setSynopsis("Bond has left active service and is enjoying a tranquil life in Jamaica. His peace " +
                                        "is short-lived when his old friend Felix Leiter from the CIA turns up " +
                                        "asking for help. The mission to rescue a kidnapped scientist turns out to " +
                                        "be far more treacherous than expected, leading Bond onto the trail of a " +
                                        "mysterious villain armed with dangerous new technology");
        noTimeToDie.setHours(2);
        noTimeToDie.setMinutes(43);
        noTimeToDie.setReleaseYear(2021);
        noTimeToDie.setReleaseMonth(Month.OCTOBER);
        noTimeToDie.setReleaseDay(8);
        noTimeToDie.setMsrbRating(MsrbRating.PG13);
        noTimeToDie.setDirector("Cary Joji Fukunaga");
        noTimeToDie.setCast(new ArrayList<>() {{
            add("Daniel Craig");
            add("Lea Seydoux");
            add("Rami Malek");
        }});
        noTimeToDie.setWriters(new ArrayList<>() {{
            add("Neil Purvis");
            add("Robert Wade");
            add("Cary Joji Fukunaga");
        }});
        noTimeToDie.setDirector("Cary Joji Fukunaga");
        noTimeToDie.setMovieCategories(List.of(MovieCategory.ACTION,
                                               MovieCategory.ADVENTURE,
                                               MovieCategory.THRILLER));
        movieService.submitMovieForm(noTimeToDie);
        // Pig
        MovieForm pig = new MovieForm();
        pig.setTitle("Pig");
        pig.setDirector("Michael Sarnoski");
        pig.setImage("/img/posters/pigPoster.jpeg");
        pig.setTrailer("https://www.youtube.com/embed/gH6vhlNTLUk");
        pig.setSynopsis("With his only connection to the outside world lying in Portland businessman Amir, " +
                                "taciturn, meditative hermit Rob has found solace deep in the heart of the " +
                                "dense Oregon forests and the unique bond with his only companion: his beloved " +
                                "truffle-hunting pig. Tired of grappling with the profound sadness of prolonged " +
                                "grief, Rob relies on a simple daily routine to keep his sanity, self-respect, " +
                                "and dignity, utterly unaware that he has already caught unwanted attention. " +
                                "Now, his best friend is missing, and revenge can only make things worse. " +
                                "Indeed, strange as it sounds, inconsolable Rob only wants his pig back, and if " +
                                "he has to, he'll go to the edge of the world to find her. But first things " +
                                "first. Who has Rob's pig?");
        pig.setHours(1);
        pig.setMinutes(32);
        pig.setReleaseYear(2021);
        pig.setReleaseMonth(Month.JULY);
        pig.setReleaseDay(18);
        pig.setMsrbRating(MsrbRating.R);
        pig.setCast(new ArrayList<>() {{
            add("Nicolas Cage");
            add("Alex Wolff");
            add("Adam Arkin");
        }});
        pig.setWriters(new ArrayList<>() {{
            add("Vanessa Block");
            add("Michael Sarnoski");
        }});
        pig.setMovieCategories(List.of(MovieCategory.DRAMA,
                                       MovieCategory.DARK,
                                       MovieCategory.MYSTERY,
                                       MovieCategory.THRILLER));
        movieService.submitMovieForm(pig);
        // The Batman
        MovieForm batman = new MovieForm();
        batman.setTitle("The Batman");
        batman.setDirector("Matt Reeves");
        batman.setImage("/img/posters/theBatmanPoster.jpeg");
        batman.setTrailer("https://www.youtube.com/embed/mqqft2x_Aa4");
        batman.setSynopsis("When the Riddler, a sadistic serial killer, begins murdering key political figures " +
                                   "in Gotham, Batman is forced to investigate the city's hidden corruption " +
                                   "and question his family's involvement.");
        batman.setHours(2);
        batman.setMinutes(56);
        batman.setReleaseYear(2022);
        batman.setReleaseMonth(Month.MARCH);
        batman.setReleaseDay(4);
        batman.setMsrbRating(MsrbRating.PG13);
        batman.setCast(new ArrayList<>() {{
            add("Robert Pattinson");
            add("Zoe Kravitz");
            add("Jeffrey Wright");
        }});
        batman.setWriters(new ArrayList<>() {{
            add("Matt Reeves");
            add("Peter Craig");
            add("Bill Finger");
        }});
        batman.setMovieCategories(List.of(MovieCategory.ACTION,
                                          MovieCategory.CRIME,
                                          MovieCategory.DRAMA));
        movieService.submitMovieForm(batman);
        // The Good, The Bad, And The Ugly
        MovieForm goodBadUgly = new MovieForm();
        goodBadUgly.setTitle("The Good, The Bad, and The Ugly");
        goodBadUgly.setDirector("Sergio Leone");
        goodBadUgly.setImage("/img/posters/theGoodTheBadTheUglyPoster.jpeg");
        goodBadUgly.setTrailer("https://www.youtube.com/embed/WCN5JJY_wiA");
        goodBadUgly.setSynopsis("Blondie, The Good (Clint Eastwood), is a professional gunslinger who is out trying " +
                                        "to earn a few dollars. Angel Eyes, The Bad (Lee Van Cleef), is a hitman " +
                                        "who always commits to a task and sees it through--as long as he's paid to " +
                                        "do so. And Tuco, The Ugly (Eli Wallach), is a wanted outlaw trying to take " +
                                        "care of his own hide. Tuco and Blondie share a partnership making money off " +
                                        "of Tuco's bounty, but when Blondie unties the partnership, Tuco tries to " +
                                        "hunt down Blondie. When Blondie and Tuco come across a horse carriage " +
                                        "loaded with dead bodies, they soon learn from the only survivor, Bill " +
                                        "Carson (Antonio Casale), that he and a few other men have buried a stash " +
                                        "of gold in a cemetery. Unfortunately, Carson dies and Tuco only finds out " +
                                        "the name of the cemetery, while Blondie finds out the name on the grave. " +
                                        "Now the two must keep each other alive in order to find the gold. Angel " +
                                        "Eyes (who had been looking for Bill Carson) discovers that Tuco and " +
                                        "Blondie met with Carson and knows they know where the gold is; now he needs " +
                                        "them to lead him to it. Now The Good, the Bad, and the Ugly must all battle " +
                                        "it out to get their hands on $200,000.00 worth of gold.");
        goodBadUgly.setHours(2);
        goodBadUgly.setMinutes(58);
        goodBadUgly.setReleaseYear(1967);
        goodBadUgly.setReleaseMonth(Month.DECEMBER);
        goodBadUgly.setReleaseDay(29);
        goodBadUgly.setMsrbRating(MsrbRating.R);
        goodBadUgly.setCast(new ArrayList<>() {{
            add("Clint Eastwood");
            add("Eli Wallach");
            add("Lee Van Cleef");
        }});
        goodBadUgly.setWriters(new ArrayList<>() {{
            add("Luciano Vincenzoni");
            add("Sergio Leone");
            add("Agenore Incrocci");
        }});
        goodBadUgly.setMovieCategories(List.of(MovieCategory.ADVENTURE,
                                               MovieCategory.WESTERN));
        movieService.submitMovieForm(goodBadUgly);
        // The Northman
        MovieForm theNorthman = new MovieForm();
        theNorthman.setTitle("The Northman");
        theNorthman.setDirector("Robert Eggers");
        theNorthman.setImage("/img/posters/theNorthManPoster.jpeg");
        theNorthman.setTrailer("https://www.youtube.com/embed/oMSdFM12hOw");
        theNorthman.setSynopsis("From visionary director Robert Eggers comes The Northman, an action-filled " +
                                        "epic that follows a young Viking prince on his quest to avenge his " +
                                        "father's murder.");
        theNorthman.setHours(2);
        theNorthman.setMinutes(17);
        theNorthman.setReleaseYear(2022);
        theNorthman.setReleaseMonth(Month.APRIL);
        theNorthman.setReleaseDay(24);
        theNorthman.setMsrbRating(MsrbRating.R);
        theNorthman.setCast(new ArrayList<>() {{
            add("Alexander Skarsgard");
            add("Nicole Kidman");
            add("Claes Bang");
        }});
        theNorthman.setWriters(new ArrayList<>() {{
            add("Sjon");
            add("Robert Eggers");
        }});
        theNorthman.setMovieCategories(List.of(MovieCategory.ACTION,
                                               MovieCategory.ADVENTURE,
                                               MovieCategory.DRAMA));
        movieService.submitMovieForm(theNorthman);
        for (Movie movie : movieService.findAll()) {
            logger.info("Movie added: " + movie.getTitle());
        }
    }

    private void defineShowrooms() {
        showroomService.deleteAll();
        // Showroom A
        ShowroomForm showroomFormA = new ShowroomForm();
        showroomFormA.setShowroomLetter(Letter.A);
        showroomFormA.setNumberOfRows(4);
        showroomFormA.setNumberOfSeatsPerRow(25);
        showroomService.submitShowroomForm(showroomFormA);
        // Showroom B
        ShowroomForm showroomFormB = new ShowroomForm();
        showroomFormB.setShowroomLetter(Letter.B);
        showroomFormB.setNumberOfRows(5);
        showroomFormB.setNumberOfSeatsPerRow(20);
        showroomService.submitShowroomForm(showroomFormB);
        // Showroom C
        ShowroomForm showroomFormC = new ShowroomForm();
        showroomFormC.setShowroomLetter(Letter.C);
        showroomFormC.setNumberOfRows(4);
        showroomFormC.setNumberOfSeatsPerRow(20);
        showroomService.submitShowroomForm(showroomFormC);
        // Showroom D
        ShowroomForm showroomFormD = new ShowroomForm();
        showroomFormD.setShowroomLetter(Letter.D);
        showroomFormD.setNumberOfRows(5);
        showroomFormD.setNumberOfSeatsPerRow(25);
        showroomService.submitShowroomForm(showroomFormD);
    }

}
