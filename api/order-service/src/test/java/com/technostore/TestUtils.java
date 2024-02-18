package com.technostore;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.util.Objects;

import static java.lang.ClassLoader.getSystemResourceAsStream;

public class TestUtils {
    @SneakyThrows
    public static String getFileContent(String filename) {
        return IOUtils.toString(Objects.requireNonNull(getSystemResourceAsStream(filename)));
    }
}
