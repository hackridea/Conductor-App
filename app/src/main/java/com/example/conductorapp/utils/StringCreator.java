package com.example.conductorapp.utils;

import java.util.Scanner;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class StringCreator {
    public String POST_PARAMS = "{";

    public void addParam(String key, String value) {
        if (POST_PARAMS.length() != 0) if (POST_PARAMS.equals("{")) {
            POST_PARAMS += "\"" + key + "\":\"" + value + "\"";
        } else {
            POST_PARAMS +=","+ "\"" + key + "\":\"" + value + "\"";
        }
    }

    public String getPOST_PARAMS() {
        return POST_PARAMS + "}";
    }
}
