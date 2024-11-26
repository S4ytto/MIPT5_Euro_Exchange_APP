package com.example.mipt5asyc;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DataLoader {

    public interface Callback {
        void onDataLoaded(List<Currency> data);
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Callback callback;

    public DataLoader(Callback callback) {
        this.callback = callback;
    }

    public void fetchData(String urlString) {
        executor.execute(() -> {
            List<Currency> result = new ArrayList<>();
            try {
                Log.d("DataLoader", "Starting data fetch from URL: " + urlString);

                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                Log.d("DataLoader", "Connected to URL. Response Code: " + connection.getResponseCode());

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(connection.getInputStream());
                NodeList cubeNodes = doc.getElementsByTagName("Cube");

                for (int i = 0; i < cubeNodes.getLength(); i++) {
                    Element element = (Element) cubeNodes.item(i);
                    if (element.hasAttribute("currency") && element.hasAttribute("rate")) {
                        String currency = element.getAttribute("currency");
                        String rate = element.getAttribute("rate");
                        result.add(new Currency(currency, rate));
                        Log.d("DataLoader", "Parsed currency: " + currency + " with rate: " + rate);
                    }
                }
            } catch (Exception e) {
                Log.e("DataLoader", "Error during XML parsing or fetching", e);
            }

            Log.d("DataLoader", "Data fetch complete. Total currencies: " + result.size());
            callback.onDataLoaded(result);
        });
    }
}
