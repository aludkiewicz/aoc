package com.aoc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileReader {

    public List<String> read(String filename) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("resources/" + filename);

        try {
            return Files.readAllLines(Paths.get(resource.toURI()));
        } catch (Exception e) {
            System.out.println("Derp");
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

}
