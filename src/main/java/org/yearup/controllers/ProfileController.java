package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/profile")
@CrossOrigin
public class ProfileController {

    private UserDao userDao;
    private ProfileDao profileDao;

    @Autowired
    public ProfileController(UserDao userDao, ProfileDao profileDao) {
        this.userDao = userDao;
        this.profileDao = profileDao;
    }

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Profile> getByUserId(Principal principal){

        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        int id = user.getId();

        Optional<Profile> optionalProfile = Optional.of(profileDao.getByUserId(id));
        if (optionalProfile.isEmpty())
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(optionalProfile.get());
    }

    @PutMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Profile> updateProfile(@RequestBody Profile profile, Principal principal){

        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        int id = user.getId();

        Optional<Profile> optionalProfile = Optional.of(profileDao.getByUserId(id));
        if (optionalProfile.isEmpty())
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(profileDao.update(id, profile));
    }
}
