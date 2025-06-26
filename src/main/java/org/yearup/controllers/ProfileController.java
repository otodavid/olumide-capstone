package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
@CrossOrigin
public class ProfileController {

    private final ProfileDao profileDao;
    private final UserDao userDao;

    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao) {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    @GetMapping()
    public Profile getProfileByUserId(Principal principal){
        Profile profile;

        try {
            // get the currently logged username

            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            profile = profileDao.getProfileByUserId(userId);
            if (profile == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "profile not found");
            }
        } catch (ResponseStatusException e) {
            throw e;
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting profile ", e);
        }
        return profile;

    }

    @PutMapping()
    @PreAuthorize("hasRole('ROLE_USER')")
    public void updateProfile(@RequestBody Profile profile, Principal principal){
//        if (profile.getFirstName() == null || profile.getFirstName().isBlank()){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First name is required");
//        }
        String userName = principal.getName();
        // find database user by userId
        User user = userDao.getByUserName(userName);
        int userId = user.getId();

        boolean success = profileDao.updateProfile(userId, profile);
        if(! success){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile UPDATE not found");
        }

    }
}
