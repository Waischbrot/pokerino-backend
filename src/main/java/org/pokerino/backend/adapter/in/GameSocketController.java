package org.pokerino.backend.adapter.in;

import org.springframework.stereotype.Controller;

@Controller
public class GameSocketController {
    // Websocket endpoints for the running game

    // Next up: Security & Ratelimit using Buckets

    // Request current state of game from a certain players perspective -> Also used to reconnect
    // Make a move in the game
    // Send out results to the player

    // Todo: We have to split this class or this functionality up. This class is used for rolling out the current state of the game
    // Todo: Problem: We have to be able to send messages to specific users, but we can't do that with the current implementation

    // Todo: Main objective: Send all messages to the specific users!
}
