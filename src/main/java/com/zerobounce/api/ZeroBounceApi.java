package com.zerobounce.api;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ZeroBounceApi {
    private final String apiKey;
    private final String baseUrl = "https://api.zerobounce.net/v1";
    private final HttpClient httpClient;
    private final DateFormat dateTimeMillisFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS", Locale.ENGLISH);
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    /**
     * Get an instance of the API helper class
     * @param apiKey - your private API key
     */
    public ZeroBounceApi(String apiKey, int timeoutSeconds) {
        this.apiKey = apiKey;

        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10).build();
        this.httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
    }

    /**
     * @return - the number of credits remaining on your account
     * @throws IOException
     * @throws URISyntaxException
     */
    public int getCredits() throws IOException, URISyntaxException {
        URI uri = new URIBuilder(baseUrl + "/getcredits")
                .addParameter("apikey", this.apiKey)
                .build();
        HttpGet httpGet = new HttpGet(uri);

        HttpResponse httpResponse = httpClient.execute(httpGet);

        if (httpResponse.getStatusLine().getStatusCode() != 200) {
            throw new IllegalStateException(httpResponse.getStatusLine().getStatusCode() + " - " + EntityUtils.toString(httpResponse.getEntity()));
        } else {
            return new JSONObject(EntityUtils.toString(httpResponse.getEntity())).getInt("Credits");
        }
    }

    /**
     * @param email - the email you want to validate
     * @return - a JSONObject with all of the information for the specified email
     * @throws IOException
     * @throws URISyntaxException
     */
    public ZeroBounceResponse validate(String email) throws IOException, URISyntaxException {
        URI uri = new URIBuilder(baseUrl + "/validate")
                .addParameter("apikey", this.apiKey)
                .addParameter("email", email)
                .build();

        HttpGet httpGet = new HttpGet(uri);
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));

                String emailAddress = jsonObject.isNull("address") ? null : jsonObject.getString("address");
                String status = jsonObject.isNull("status") ? null : jsonObject.getString("status");
                String subStatus = jsonObject.isNull("sub_status") ? null : jsonObject.getString("sub_status");
                String account = jsonObject.isNull("account") ? null : jsonObject.getString("account");
                String domain = jsonObject.isNull("domain") ? null : jsonObject.getString("domain");
                Boolean disposable = jsonObject.isNull("disposable") ? null : jsonObject.getBoolean("disposable");
                Boolean toxic = jsonObject.isNull("toxic") ? null : jsonObject.getBoolean("toxic");
                String firstName = jsonObject.isNull("firstname") ? null : jsonObject.getString("firstname");
                String lastName = jsonObject.isNull("lastname") ? null : jsonObject.getString("lastname");
                String gender = jsonObject.isNull("gender") ? null : jsonObject.getString("gender");
                String location = jsonObject.isNull("location") ? null : jsonObject.getString("location");

                Date creationDate = null;
                try {
                    creationDate = jsonObject.isNull("creationdate") ? null : dateFormat.parse(jsonObject.getString("creationdate"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Date processedAt = null;
                try {
                    processedAt = jsonObject.isNull("processedat") ? null : dateTimeMillisFormat.parse(jsonObject.getString("processedat"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return new ZeroBounceResponse(emailAddress, status, subStatus, account, domain, disposable, toxic, firstName, lastName, gender, location, creationDate, processedAt);
            } else {
                throw new IllegalStateException(httpResponse.getStatusLine().getStatusCode() + " - " + EntityUtils.toString(httpResponse.getEntity()));
            }
        } catch (SocketTimeoutException ex) {
            return new ZeroBounceResponse(email.toLowerCase(), "Unknown", "timeout_exceeded", null, null, null, null, null, null, null, null, null, null);
        } catch (Exception ex) {
            return new ZeroBounceResponse(email.toLowerCase(), "Unknown", "exception_occurred", null, null, null, null, null, null, null, null, null, null);
        }
    }

    /**
     * @param email - the email you want to validate
     * @param ip - the ip to be use for this validation (advanced)
     * @return - a JSONObject with all of the information for the specified email
     * @throws IOException
     * @throws URISyntaxException
     */
    public ZeroBounceResponseWithIp validateWithIpAddress(String email, String ip) throws IOException, URISyntaxException {
        URI uri = new URIBuilder(baseUrl + "/validatewithip")
                .addParameter("apikey", this.apiKey)
                .addParameter("email", email)
                .addParameter("ipAddress", ip)
                .build();

        HttpGet httpGet = new HttpGet(uri);
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
            JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));

            String emailAddress = jsonObject.isNull("address") ? null : jsonObject.getString("address");
            String status = jsonObject.isNull("status") ? null : jsonObject.getString("status");
            String subStatus = jsonObject.isNull("sub_status") ? null : jsonObject.getString("sub_status");
            String account = jsonObject.isNull("account") ? null : jsonObject.getString("account");
            String domain = jsonObject.isNull("domain") ? null : jsonObject.getString("domain");
            Boolean disposable = jsonObject.isNull("disposable") ? null : jsonObject.getBoolean("disposable");
            Boolean toxic = jsonObject.isNull("toxic") ? null : jsonObject.getBoolean("toxic");
            String firstName = jsonObject.isNull("firstname") ? null : jsonObject.getString("firstname");
            String lastName = jsonObject.isNull("lastname") ? null : jsonObject.getString("lastname");
            String gender = jsonObject.isNull("gender") ? null : jsonObject.getString("gender");
            String location = jsonObject.isNull("location") ? null : jsonObject.getString("location");
            String country = jsonObject.isNull("country") ? null : jsonObject.getString("country");
            String city = jsonObject.isNull("city") ? null : jsonObject.getString("city");
            String zipcode = jsonObject.isNull("zipcode") ? null : jsonObject.getString("zipcode");
            String region = jsonObject.isNull("region") ? null : jsonObject.getString("region");

            Date creationDate = null;
            try {
                creationDate = jsonObject.isNull("creationdate") ? null : dateFormat.parse(jsonObject.getString("creationdate"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Date processedAt = null;
            try {
                processedAt = jsonObject.isNull("processedat") ? null : dateTimeMillisFormat.parse(jsonObject.getString("processedat"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return new ZeroBounceResponseWithIp(emailAddress, status, subStatus, account, domain, disposable, toxic, firstName, lastName, gender, location, creationDate, processedAt, country, city, zipcode, region);
            } else {
                throw new IllegalStateException(httpResponse.getStatusLine().getStatusCode() + " - " + EntityUtils.toString(httpResponse.getEntity()));
            }
        } catch (SocketTimeoutException ex) {
            return new ZeroBounceResponseWithIp(email.toLowerCase(), "Unknown", "timeout_exceeded", null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        } catch (Exception ex) {
            return new ZeroBounceResponseWithIp(email.toLowerCase(), "Unknown", "exception_occurred", null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        }
    }

    /**
     * Response wrapper for /validate endpoint
     */
    protected class ZeroBounceResponse {
        private final String emailAddress;
        private final String status;
        private final String subStatus;
        private final String account;
        private final String domain;
        private final Boolean disposable;
        private final Boolean toxic;
        private final String firstName;
        private final String lastName;
        private final String gender;
        private final String location;
        private final Date creationDate;
        private final Date processedAt;

        public ZeroBounceResponse(String emailAddress, String status, String subStatus, String account, String domain, Boolean disposable, Boolean toxic, String firstName, String lastName, String gender, String location, Date creationDate, Date processedAt) {
            this.emailAddress = emailAddress;
            this.status = status;
            this.subStatus = subStatus;
            this.account = account;
            this.domain = domain;
            this.disposable = disposable;
            this.toxic = toxic;
            this.firstName = firstName;
            this.lastName = lastName;
            this.gender = gender;
            this.location = location;
            this.creationDate = creationDate;
            this.processedAt = processedAt;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public String getStatus() {
            return status;
        }

        public String getSubStatus() {
            return subStatus;
        }

        public String getAccount() {
            return account;
        }

        public String getDomain() {
            return domain;
        }

        public Boolean isDisposable() {
            return disposable;
        }

        public Boolean isToxic() {
            return toxic;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getGender() {
            return gender;
        }

        public String getLocation() {
            return location;
        }

        public Date getCreationDate() {
            return creationDate;
        }

        public Date getProcessedAt() {
            return processedAt;
        }

        @Override
        public String toString() {
            return "ZeroBounceResponse{" +
                    "emailAddress='" + emailAddress + '\'' +
                    ", status='" + status + '\'' +
                    ", subStatus='" + subStatus + '\'' +
                    ", account='" + account + '\'' +
                    ", domain='" + domain + '\'' +
                    ", disposable=" + disposable +
                    ", toxic=" + toxic +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", gender='" + gender + '\'' +
                    ", location='" + location + '\'' +
                    ", creationDate=" + creationDate +
                    ", processedAt=" + processedAt +
                    '}';
        }
    }

    /**
     * Response wrapper for /validatewithip endpoint
     */
    protected class ZeroBounceResponseWithIp {
        private final String emailAddress;
        private final String status;
        private final String subStatus;
        private final String account;
        private final String domain;
        private final Boolean disposable;
        private final Boolean toxic;
        private final String firstName;
        private final String lastName;
        private final String gender;
        private final String location;
        private final Date creationDate;
        private final Date processedAt;
        private final String country;
        private final String city;
        private final String zipcode;
        private final String region;

        public ZeroBounceResponseWithIp(String emailAddress, String status, String subStatus, String account, String domain, Boolean disposable, Boolean toxic, String firstName, String lastName, String gender, String location, Date creationDate, Date processedAt, String country, String city, String zipcode, String region) {
            this.emailAddress = emailAddress;
            this.status = status;
            this.subStatus = subStatus;
            this.account = account;
            this.domain = domain;
            this.disposable = disposable;
            this.toxic = toxic;
            this.firstName = firstName;
            this.lastName = lastName;
            this.gender = gender;
            this.location = location;
            this.creationDate = creationDate;
            this.processedAt = processedAt;
            this.country = country;
            this.city = city;
            this.zipcode = zipcode;
            this.region = region;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public String getStatus() {
            return status;
        }

        public String getSubStatus() {
            return subStatus;
        }

        public String getAccount() {
            return account;
        }

        public String getDomain() {
            return domain;
        }

        public Boolean isDisposable() {
            return disposable;
        }

        public Boolean isToxic() {
            return toxic;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getGender() {
            return gender;
        }

        public String getLocation() {
            return location;
        }

        public Date getCreationDate() {
            return creationDate;
        }

        public Date getProcessedAt() {
            return processedAt;
        }

        public String getCountry() {
            return country;
        }

        public String getCity() {
            return city;
        }

        public String getZipcode() {
            return zipcode;
        }

        public String getRegion() {
            return region;
        }

        @Override
        public String toString() {
            return "ZeroBounceResponseWithIp{" +
                    "emailAddress='" + emailAddress + '\'' +
                    ", status='" + status + '\'' +
                    ", subStatus='" + subStatus + '\'' +
                    ", account='" + account + '\'' +
                    ", domain='" + domain + '\'' +
                    ", disposable=" + disposable +
                    ", toxic=" + toxic +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", gender='" + gender + '\'' +
                    ", location='" + location + '\'' +
                    ", creationDate=" + creationDate +
                    ", processedAt=" + processedAt +
                    ", country='" + country + '\'' +
                    ", city='" + city + '\'' +
                    ", zipcode='" + zipcode + '\'' +
                    ", region='" + region + '\'' +
                    '}';
        }
    }
}
