package com.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


@RestController
public class TwitterController {

    @Autowired
    private TwitterDao twitterDao;

    @RequestMapping(value = "/createRiddle", method = {RequestMethod.POST,RequestMethod.GET})
    public Riddles createRiddle(@RequestBody Riddles riddle) {
        Riddles newRiddle = new Riddles(0,riddle.getRiddle(),riddle.getAnswer());

        twitterDao.add(newRiddle);
        return newRiddle;
    }

    @RequestMapping(value = "/getRiddle/{id}", method = RequestMethod.GET)
    public Riddles getRiddle(@PathVariable("id") int id) {
        return twitterDao.getById(id);
    }

    @RequestMapping(value = "/updateRiddle", method = RequestMethod.PUT)
    public void updateRiddle(@RequestBody Riddles r) {
        twitterDao.update(r);
    }

    @RequestMapping(value = "/deleteRiddle/{id}", method = RequestMethod.DELETE)
    public void deleteRiddle(@PathVariable("id") int id) {
        twitterDao.delete(id);
    }

}
