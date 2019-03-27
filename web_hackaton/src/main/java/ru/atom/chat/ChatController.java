package ru.atom.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Controller
@RequestMapping("chat")
public class ChatController {
    private Queue<String> messages = new ConcurrentLinkedQueue<>();
    private Map<String, String> usersOnline = new ConcurrentHashMap<>();

    /**
     * curl -X POST -i localhost:8080/chat/login -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = "login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> login(@RequestParam("name") String name) {
        if (name.length() < 1) {
            return ResponseEntity.badRequest().body("Too short name, sorry :(");
        }
        if (name.length() > 20) {
            return ResponseEntity.badRequest().body("Too long name, sorry :(");
        }
        if (usersOnline.containsKey(name)) {
            return ResponseEntity.badRequest().body("Already logged in:(");
        }
        usersOnline.put(name, name);
        messages.add("[" + name + "] logged in");
        return ResponseEntity.ok().build();
    }

    /**
     * curl -i localhost:8080/chat/online
     */
    @RequestMapping(
            path = "online",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity online() {
        String responseBody = String.join("\n", usersOnline.keySet().stream().sorted().collect(Collectors.toList()));
        return ResponseEntity.ok(responseBody);
    }

    /**
     * curl -X POST -i localhost:8080/chat/logout -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = "logout",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> logout(@RequestParam("name") String name) {
        if (usersOnline.containsKey(name)) {
            usersOnline.remove(name, name);
            messages.add("[" + name + "] logged out");
            return ResponseEntity.ok().build();
        } else

            return ResponseEntity.badRequest().body("Already not logged in:(");
    }

    /**
     * curl -X POST -i localhost:8080/chat/say -d "name=I_AM_STUPID&msg=Hello everyone in this chat"
     */
    @RequestMapping(
            path = "say",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> say(@RequestParam("name") String name, @RequestParam("msg") String msg) {
        if (usersOnline.containsKey(name)) {
            messages.add("[" + name + "]: " + msg);
            return ResponseEntity.ok().build();
        } else
            return ResponseEntity.badRequest().body("Allready not logged in");
    }

    /**
     * curl -i localhost:8080/chat/chat
     */
    @RequestMapping(
            path = "chat",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity chat() {
        String responseBody = String.join("\n", messages.stream().collect(Collectors.toList()));
        return ResponseEntity.ok(responseBody);
    }


    @RequestMapping(
            path = "clear",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> clear(@RequestParam("name") String name) {
        if (usersOnline.containsKey(name)) {
            messages.clear();
            messages.add("[" + name + "] clicked clear");
            return ResponseEntity.ok().build();
        } else
            return ResponseEntity.badRequest().body("Allready not logged in");
    }


    @RequestMapping(
            path = "rename",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> rename(@RequestParam("oldname") String oldname,
                                         @RequestParam("newname") String newname) {
        if (usersOnline.containsKey(oldname))
            if (usersOnline.containsKey(newname))
                return ResponseEntity.badRequest().body("Name is reserved");
            else {
                usersOnline.remove(oldname);
                usersOnline.put(newname, newname);
                messages.add("[" + oldname + "] renamed by " + newname);
                return ResponseEntity.ok().build();
            }
        else
            return ResponseEntity.badRequest().body("Allready not logged in");
    }

}