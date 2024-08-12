package com.skinclear.skinclearbackend.service;

import com.skinclear.skinclearbackend.entity.Rating;
import com.skinclear.skinclearbackend.entity.User;
import com.skinclear.skinclearbackend.repository.RatingRepository;
import com.skinclear.skinclearbackend.repository.UserRepository;
import com.skinclear.skinclearbackend.resource.RatingResource;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;


    public RatingService(RatingRepository ratingRepository, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
    }

    public void rate(Principal principal, int rating) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        LocalDate date = LocalDate.now();
        ratingRepository.findByUserIdAndDate(user.getId(), date)
                .ifPresentOrElse(
                        r -> {
                            r.setRating(rating);
                            ratingRepository.save(r);
                        },
                        () -> {
                            Rating newRating = new Rating();
                            newRating.setUserId(user.getId());
                            newRating.setRating(rating);
                            newRating.setDate(date);
                            ratingRepository.save(newRating);
                        }
                );
    }

    public List<RatingResource> getRating(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Retrieve the ratings for the last 7 days
        List<Rating> ratings = ratingRepository.findLastSevenDaysByUserId(user.getId());

        Map<DayOfWeek, RatingResource> ratingMap = ratings.stream()
                .collect(Collectors.toMap(
                        r -> r.getDate().getDayOfWeek(),
                        r -> {
                            RatingResource ratingResource = new RatingResource();
                            ratingResource.setDay(r.getDate().getDayOfWeek().name());
                            ratingResource.setRating(r.getRating());
                            return ratingResource;
                        }
                ));

        // Create a list of RatingResource for the last 7 days, filling in missing days with a rating of 0
        List<RatingResource> ratingResources = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = LocalDate.now().minusDays(i);
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            RatingResource ratingResource = ratingMap.getOrDefault(dayOfWeek, new RatingResource());
            if (ratingResource.getDay() == null) {
                ratingResource.setDay(dayOfWeek.name());
                ratingResource.setRating(0);
            }

            ratingResources.add(ratingResource);
        }
        Collections.reverse(ratingResources);
        return ratingResources;
    }

}
