package org.pokerino.backend.adapter.in;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/table")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class TableController {

    // POST /host (Hosts a new table, just possible if you are not already in a table)

    // POST /join (Joins a table using a code, just possible if you are not already in a table)

    // POST /leave (Leaves a table, if present)

    // GET /current-table -> Who moves next, who is in the game, everything for displaying the current table

    // GET /public-tables -> Public tables available to join (Just Basic Information)

}
