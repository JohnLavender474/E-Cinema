package com.ecinema.app.configs;

import com.ecinema.app.entities.*;
import com.ecinema.app.services.*;
import com.ecinema.app.utils.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;

/**
 * Initializes persistence on app start.
 */
@Component
public class InitializationConfig {

    private final UserService userService;
    private final MovieService movieService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Map<UserRole, UserRoleDefService<? extends UserRoleDef>> userRoleDefServices =
            new EnumMap<>(UserRole.class);

    /**
     * Instantiates a new Startup config.
     *
     * @param userService                the user service
     * @param adminRoleDefService        the admin role def service
     * @param adminTraineeRoleDefService the admin trainee role def service
     * @param customerRoleDefService     the customer role def service
     * @param moderatorRoleDefService    the moderator role def service
     * @param passwordEncoder            the password encoder
     */
    public InitializationConfig(UserService userService,
                                AdminRoleDefService adminRoleDefService,
                                AdminTraineeRoleDefService adminTraineeRoleDefService,
                                CustomerRoleDefService customerRoleDefService,
                                ModeratorRoleDefService moderatorRoleDefService,
                                MovieService movieService,
                                BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.movieService = movieService;
        this.passwordEncoder = passwordEncoder;
        userRoleDefServices.put(UserRole.ADMIN, adminRoleDefService);
        userRoleDefServices.put(UserRole.ADMIN_TRAINEE, adminTraineeRoleDefService);
        userRoleDefServices.put(UserRole.CUSTOMER, customerRoleDefService);
        userRoleDefServices.put(UserRole.MODERATOR, moderatorRoleDefService);
    }

    /**
     * Called on app ready.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void appReady() {
        defineUsers();
        defineMovies();
    }

    private void defineUsers() {
        userService.deleteAll();
        // Root admin
        User root = new User();
        root.setUsername("ROOT");
        root.setEmail("ecinema.app.474@gmail.com");
        String encodedPassword = passwordEncoder.encode("password123!");
        root.setPassword(encodedPassword);
        root.setConfirmPassword(encodedPassword);
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
        AdminRoleDef adminRoleDef = new AdminRoleDef();
        adminRoleDef.setUser(root);
        adminRoleDef.setIsRoleValid(true);
        root.getUserRoleDefs().put(UserRole.ADMIN, adminRoleDef);
        AdminRoleDefService adminRoleDefService =
                (AdminRoleDefService) userRoleDefServices.get(UserRole.ADMIN);
        adminRoleDefService.save(adminRoleDef);
        ModeratorRoleDef moderatorRoleDef = new ModeratorRoleDef();
        moderatorRoleDef.setUser(root);
        moderatorRoleDef.setIsRoleValid(true);
        root.getUserRoleDefs().put(UserRole.MODERATOR, moderatorRoleDef);
        ModeratorRoleDefService moderatorRoleDefService =
                (ModeratorRoleDefService) userRoleDefServices.get(UserRole.MODERATOR);
        moderatorRoleDefService.save(moderatorRoleDef);
    }

    private void defineMovies() {
        movieService.deleteAll();
        // Aliens
        Movie aliens = new Movie();
        aliens.setTitle("Aliens");
        aliens.setDirector("James Cameron");
        aliens.setImage("/img/posters/aliensPoster.jpeg");
        aliens.setTrailer("/videos/AliensTrailer.mp4");
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
        aliens.setDuration(new Duration(2, 17));
        aliens.setReleaseDate(LocalDate.of(1986, Month.JULY, 20));
        aliens.setMsrbRating(MsrbRating.R);
        aliens.setCast(new HashSet<>() {{
            add("Sigourney Weaver");
            add("Michael Biehn");
            add("Carrie Henn");
        }});
        aliens.setWriters(new HashSet<>() {{
            add("James Cameron");
            add("David Giler");
            add("Walter Hill");
        }});
        aliens.setMovieCategories(EnumSet.of(MovieCategory.ACTION,
                                             MovieCategory.ADVENTURE,
                                             MovieCategory.SCI_FI,
                                             MovieCategory.HORROR,
                                             MovieCategory.CLASSIC));
        movieService.save(aliens);
        // Darkest Hour
        Movie darkestHour = new Movie();
        darkestHour.setTitle("Darkest Hour");
        darkestHour.setDirector("Joe Wright");
        darkestHour.setImage("/img/posters/darkestHourPoster.jpeg");
        darkestHour.setTrailer("/videos/DarkestHourTrailer.mp4");
        darkestHour.setSynopsis("During World War II, as Adolf Hitler's powerful Wehrmacht rampages across Europe, " +
                                        "the Prime Minister of the United Kingdom, Neville Chamberlain, is forced to " +
                                        "resign, recommending Winston Churchill as his replacement. But even in his " +
                                        "early days as the country's leader, Churchill is under pressure to commence " +
                                        "peace negotiations with Hitler or to fight head-on the seemingly " +
                                        "invincible Nazi regime, whatever the cost. However difficult and dangerous " +
                                        "his decision may be, Churchill has no choice but to shine in the " +
                                        "country's darkest hour.");
        darkestHour.setDuration(new Duration(2, 5));
        darkestHour.setReleaseDate(LocalDate.of(2017, Month.NOVEMBER, 26));
        darkestHour.setMsrbRating(MsrbRating.PG13);
        darkestHour.setCast(new HashSet<>() {{
            add("Gary Oldman");
            add("Lily James");
            add("Kristin Scott Thomas");
        }});
        darkestHour.setWriters(new HashSet<>() {{
            add("Anthony McCarten");
        }});
        darkestHour.setMovieCategories(EnumSet.of(MovieCategory.BIOGRAPHY,
                                                  MovieCategory.DRAMA,
                                                  MovieCategory.WAR));
        movieService.save(darkestHour);
        // Dune
        Movie dune = new Movie();
        dune.setTitle("Dune");
        dune.setDirector("Denis Villeneuve");
        dune.setImage("/img/posters/dunePoster.jpeg");
        dune.setTrailer("/videos/DuneTrailer.mp4");
        dune.setSynopsis("A mythic and emotionally charged hero's journey, \"Dune\" tells the story of Paul " +
                                 "Atreides, a brilliant and gifted young man born into a great destiny beyond his " +
                                 "understanding, who must travel to the most dangerous planet in the universe to " +
                                 "ensure the future of his family and his people. As malevolent forces explode into " +
                                 "conflict over the planet's exclusive supply of the most precious resource in " +
                                 "existence-a commodity capable of unlocking humanity's greatest potential-only " +
                                 "those who can conquer their fear will survive.");
        dune.setDuration(new Duration(2, 35));
        dune.setReleaseDate(LocalDate.of(2021, Month.OCTOBER, 22));
        dune.setMsrbRating(MsrbRating.PG13);
        dune.setCast(new HashSet<>() {{
            add("Timothee Chalamet");
            add("Rebecca Ferguson");
            add("Zendaya");
        }});
        dune.setWriters(new HashSet<>() {{
            add("Jon Spaihts");
            add("Denis Villeneuve");
            add("Eric Roth");
        }});
        dune.setMovieCategories(EnumSet.of(MovieCategory.SCI_FI,
                                           MovieCategory.DRAMA,
                                           MovieCategory.ACTION,
                                           MovieCategory.ADVENTURE));
        movieService.save(dune);
        // Empire Strikes Back
        Movie empireStrikesBack = new Movie();
        empireStrikesBack.setTitle("Star Wars: The Empire Strikes Back");
        empireStrikesBack.setImage("/img/posters/empireStrikesBackPoster.jpeg");
        empireStrikesBack.setTrailer("/videos/EmpireStrikesBackTrailer.mp4");
        empireStrikesBack.setSynopsis("Luke Skywalker, Han Solo, Princess Leia and Chewbacca face attack by the " +
                                              "Imperial forces and its AT-AT walkers on the ice planet Hoth. While " +
                                              "Han and Leia escape in the Millennium Falcon, Luke travels to Dagobah " +
                                              "in search of Yoda. Only with the Jedi Master's help will Luke survive " +
                                              "when the Dark Side of the Force beckons him into the ultimate duel " +
                                              "with Darth Vader.");
        empireStrikesBack.setDuration(new Duration(2, 4));
        empireStrikesBack.setReleaseDate(LocalDate.of(1980, Month.MAY, 25));
        empireStrikesBack.setMsrbRating(MsrbRating.PG);
        empireStrikesBack.setCast(new HashSet<>() {{
            add("Mark Hamill");
            add("Harrison Ford");
            add("Carrie Fisher");
            add("James Earl Jones");
        }});
        empireStrikesBack.setWriters(new HashSet<>() {{
            add("Leigh Brackett");
            add("Lawrence Kasdan");
            add("George Lucas");
        }});
        empireStrikesBack.setDirector("Irvin Kershner");
        empireStrikesBack.setMovieCategories(EnumSet.of(MovieCategory.ACTION,
                                                        MovieCategory.ADVENTURE,
                                                        MovieCategory.FANTASY,
                                                        MovieCategory.CLASSIC));
        movieService.save(empireStrikesBack);
        // Interstellar
        Movie interstellar = new Movie();
        interstellar.setTitle("Interstellar");
        interstellar.setDirector("Christopher Nolan");
        interstellar.setImage("/img/posters/interstellarPoster.jpeg");
        interstellar.setTrailer("/videos/InterstellarTrailer.mp4");
        interstellar.setSynopsis("Earth's future has been riddled by disasters, famines, and droughts. There is " +
                                         "only one way to ensure mankind's survival: Interstellar travel. A newly " +
                                         "discovered wormhole in the far reaches of our solar system allows a team " +
                                         "of astronauts to go where no man has gone before, a planet that may have " +
                                         "the right environment to sustain human life.");
        interstellar.setDuration(new Duration(2, 49));
        interstellar.setReleaseDate(LocalDate.of(2014, Month.NOVEMBER, 9));
        interstellar.setMsrbRating(MsrbRating.PG13);
        interstellar.setCast(new HashSet<>() {{
            add("Matthew McConaughey");
            add("Anne Hathaway");
            add("Jessica Chastain");
        }});
        interstellar.setWriters(new HashSet<>() {{
            add("Jonathan Nolan");
            add("Christopher Nolan");
        }});
        interstellar.setMovieCategories(EnumSet.of(MovieCategory.ADVENTURE,
                                                   MovieCategory.DRAMA,
                                                   MovieCategory.SCI_FI));
        movieService.save(interstellar);
        // 007: No Time to Die
        Movie noTimeToDie = new Movie();
        noTimeToDie.setTitle("007: No Time To Die");
        noTimeToDie.setImage("/img/posters/noTimeToDiePoster.jpeg");
        noTimeToDie.setTrailer("/videos/NoTimeToDieTrailer.mp4");
        noTimeToDie.setSynopsis("Bond has left active service and is enjoying a tranquil life in Jamaica. His peace " +
                                        "is short-lived when his old friend Felix Leiter from the CIA turns up " +
                                        "asking for help. The mission to rescue a kidnapped scientist turns out to " +
                                        "be far more treacherous than expected, leading Bond onto the trail of a " +
                                        "mysterious villain armed with dangerous new technology");
        noTimeToDie.setDuration(new Duration(2, 43));
        noTimeToDie.setReleaseDate(LocalDate.of(2021, Month.OCTOBER, 8));
        noTimeToDie.setMsrbRating(MsrbRating.PG13);
        noTimeToDie.setDirector("Cary Joji Fukunaga");
        noTimeToDie.setCast(new HashSet<>() {{
            add("Daniel Craig");
            add("Lea Seydoux");
            add("Rami Malek");
        }});
        noTimeToDie.setWriters(new HashSet<>() {{
            add("Neil Purvis");
            add("Robert Wade");
            add("Cary Joji Fukunaga");
        }});
        noTimeToDie.setDirector("Cary Joji Fukunaga");
        noTimeToDie.setMovieCategories(EnumSet.of(MovieCategory.ACTION,
                                                  MovieCategory.ADVENTURE,
                                                  MovieCategory.THRILLER));
        movieService.save(noTimeToDie);
        // Pig
        Movie pig = new Movie();
        pig.setTitle("Pig");
        pig.setDirector("Michael Sarnoski");
        pig.setImage("/img/posters/pigPoster.jpeg");
        pig.setTrailer("/videos/PigTrailer.mp4");
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
        pig.setDuration(new Duration(1, 32));
        pig.setReleaseDate(LocalDate.of(2021, Month.JULY, 18));
        pig.setMsrbRating(MsrbRating.R);
        pig.setCast(new HashSet<>() {{
            add("Nicolas Cage");
            add("Alex Wolff");
            add("Adam Arkin");
        }});
        pig.setWriters(new HashSet<>() {{
            add("Vanessa Block");
            add("Michael Sarnoski");
        }});
        pig.setMovieCategories(EnumSet.of(MovieCategory.DRAMA,
                                          MovieCategory.DARK,
                                          MovieCategory.MYSTERY,
                                          MovieCategory.THRILLER));
        movieService.save(pig);
        // The Batman
        Movie batman = new Movie();
        batman.setTitle("The Batman");
        batman.setDirector("Matt Reeves");
        batman.setImage("/img/posters/theBatmanPoster.jpeg");
        batman.setTrailer("/videos/TheBatmanTrailer.mp4");
        batman.setSynopsis("When the Riddler, a sadistic serial killer, begins murdering key political figures " +
                                   "in Gotham, Batman is forced to investigate the city's hidden corruption " +
                                   "and question his family's involvement.");
        batman.setDuration(new Duration(2, 56));
        batman.setReleaseDate(LocalDate.of(2022, Month.MARCH, 4));
        batman.setMsrbRating(MsrbRating.PG13);
        batman.setCast(new HashSet<>() {{
            add("Robert Pattinson");
            add("Zoe Kravitz");
            add("Jeffrey Wright");
        }});
        batman.setWriters(new HashSet<>() {{
            add("Matt Reeves");
            add("Peter Craig");
            add("Bill Finger");
        }});
        batman.setMovieCategories(EnumSet.of(MovieCategory.ACTION,
                                             MovieCategory.CRIME,
                                             MovieCategory.DRAMA));
        movieService.save(batman);
        // The Good, The Bad, And The Ugly
        Movie goodBadUgly = new Movie();
        goodBadUgly.setTitle("The Good, The Bad, and The Ugly");
        goodBadUgly.setDirector("Sergio Leone");
        goodBadUgly.setImage("/img/posters/theGoodTheBadTheUglyPoster.jpeg");
        goodBadUgly.setTrailer("/videos/TheGoodTheBadTheUglyTrailer.mp4");
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
        goodBadUgly.setDuration(new Duration(2, 58));
        goodBadUgly.setReleaseDate(LocalDate.of(1967, Month.DECEMBER, 29));
        goodBadUgly.setMsrbRating(MsrbRating.R);
        goodBadUgly.setCast(new HashSet<>() {{
            add("Clint Eastwood");
            add("Eli Wallach");
            add("Lee Van Cleef");
        }});
        goodBadUgly.setWriters(new HashSet<>() {{
            add("Luciano Vincenzoni");
            add("Sergio Leone");
            add("Agenore Incrocci");
        }});
        goodBadUgly.setMovieCategories(EnumSet.of(MovieCategory.ADVENTURE,
                                                  MovieCategory.WESTERN));
        movieService.save(goodBadUgly);
        // The Northman
        Movie theNorthman = new Movie();
        theNorthman.setTitle("The Northman");
        theNorthman.setDirector("Robert Eggers");
        theNorthman.setImage("/img/posters/theNorthManPoster.jpeg");
        theNorthman.setTrailer("/videos/TheNorthmanTrailer.mp4");
        theNorthman.setSynopsis("From visionary director Robert Eggers comes The Northman, an action-filled " +
                                        "epic that follows a young Viking prince on his quest to avenge his " +
                                        "father's murder.");
        theNorthman.setDuration(new Duration(2, 17));
        theNorthman.setReleaseDate(LocalDate.of(2022, Month.APRIL, 24));
        theNorthman.setMsrbRating(MsrbRating.R);
        theNorthman.setCast(new HashSet<>() {{
            add("Alexander Skarsgard");
            add("Nicole Kidman");
            add("Claes Bang");
        }});
        theNorthman.setWriters(new HashSet<>() {{
            add("Sjon");
            add("Robert Eggers");
        }});
        theNorthman.setMovieCategories(EnumSet.of(MovieCategory.ACTION,
                                                  MovieCategory.ADVENTURE,
                                                  MovieCategory.DRAMA));
        movieService.save(theNorthman);
    }

}
