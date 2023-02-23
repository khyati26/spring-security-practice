package com.example.springsecuritydemo2.util;

import com.example.springsecuritydemo2.model.PersistentLogins;
import com.example.springsecuritydemo2.repository.PersistentLoginsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.util.Date;

public class CustomPersistentTokenRepository implements PersistentTokenRepository {

    @Autowired
    PersistentLoginsRepository persistentLoginsRepository;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {

        PersistentLogins persistentLogins = new PersistentLogins(token.getUsername(), token.getSeries(), token.getTokenValue(), token.getDate());
        persistentLoginsRepository.save(persistentLogins);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        PersistentLogins current = persistentLoginsRepository.findById(series).get();
        current.setToken(tokenValue);
        current.setLast_used(lastUsed);
        persistentLoginsRepository.save(current);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        try {
            PersistentLogins current = persistentLoginsRepository.findById(seriesId).get();
            return new PersistentRememberMeToken(current.getUsername(), current.getSeries(), current.getToken(), current.getLast_used());

        } catch (EmptyResultDataAccessException ex) {
            System.err.println("Querying token for series " + seriesId + " returned no results.");
        } catch (IncorrectResultSizeDataAccessException ex) {
            System.err.println(
                    "Querying token for series " + seriesId + " returned more than one value. Series" + " should be unique");
        } catch (DataAccessException ex) {
            System.err.println("Failed to load token for series " + seriesId);
        }
        return null;
    }

    @Override
    public void removeUserTokens(String username) {
        persistentLoginsRepository.deleteByUsername(username);
    }
}
