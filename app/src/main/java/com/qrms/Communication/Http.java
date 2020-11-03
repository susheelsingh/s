package com.qrms.Communication;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by abc on 26-Jun-18.
 */

public class Http {
    public Http(){
    }

//    public String post(String url, HashMap<String, String> map){
//
//    }
    public String getAuthToken(String url, HashMap<String, String> map, String mainToken){
        try{
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(15000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization",mainToken);

            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            writer.write(getPostDataHashMapString(map));
            writer.flush();
            writer.close();
            outputStream.close();

            int requestCode = connection.getResponseCode();
            if (requestCode == HttpURLConnection.HTTP_OK){
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer buffer = new StringBuffer();
                String line ="";
                while ((line = reader.readLine())!= null){
                    buffer.append(line);
                    break;
                }
                reader.close();
                return buffer.toString();
            } else {
                return new String(""+requestCode);
            }
        } catch (MalformedURLException e) {
            return e.toString();
        } catch (IOException e) {
            return e.toString();
        }
    }

    public String get(String url, String authKey){
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try{
            URL url1 = new URL(url);
            connection = (HttpURLConnection)url1.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(15000);
            connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestProperty("Authorization",authKey);

            InputStream inputStream = new BufferedInputStream(connection.getInputStream());

            if (inputStream == null)
                return "No data found";
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null){
                buffer.append(line+"\n");
            }
            return buffer.toString();
        } catch (MalformedURLException e) {
            return e.toString();
        } catch (IOException e) {
            return e.toString();
        } finally {
            if (connection !=null)
                connection.disconnect();
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                }
            }
        }
    }

    public String post(String url, JSONObject jsonObject, String token){
        try{
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(15000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("content-type","application/json");
            connection.setRequestProperty("authorization",token);

            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();
            outputStream.close();

            //For getting response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (SocketTimeoutException e){
            return e.toString();
        } catch (MalformedURLException e) {
            return e.toString();
        } catch (IOException e){
            return e.toString();
        }
    }

    public String postWithoutParameters(String url, String token){
        try{
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(15000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestProperty("Authorization",token);

//            OutputStream outputStream = connection.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
//            writer.flush();
//            writer.close();
//            outputStream.close();

            //For getting response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (SocketTimeoutException e){
            return e.toString();
        } catch (MalformedURLException e) {
            return e.toString();
        } catch (IOException e){
            return e.toString();
        }
    }


    private String getPostDataHashMapString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry: params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
        }
        return result.toString();
    }
}
